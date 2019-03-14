package lightweight.geometry

import lightweight.{Lamp, World}
import lightweight.nodes.{Color, ColorWithDensity, VolumeOutput}

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

case class Ray(origin: Vector3D, direction: Vector3D) {

  @inline def traceRay(mesh: Mesh, world: World, triangleIndex: Int): ((Int, Vector3D, Double), HashMap[VolumeOutput, Int], (Int, Vector3D, Double, Double)) = {
    var volumes: HashMap[VolumeOutput, Int] = HashMap.empty[VolumeOutput, Int]
    var lamps: HashMap[Int, (Vector3D, Double, Double)] = HashMap.empty[Int, (Vector3D, Double , Double)]
    var index: Int = 0
    var hitPoint: Vector3D = null
    var time: Double = Double.PositiveInfinity
    for (triangle <- 0 until mesh.mesh.length) {
      if (triangle != triangleIndex && !mesh.mesh(triangle).backQuadrant(this)) {
        val intersection = mesh.mesh(triangle).intersectionWithRay(this)
        if (intersection != null && intersection._2 < time) {
          index = triangle
          hitPoint = intersection._1
          time = intersection._2
        }
        if (intersection != null && mesh.mesh(triangle).volume != null) {
          if (!volumes.contains(mesh.mesh(triangle).volume)) {
            volumes += (mesh.mesh(triangle).volume -> (if (mesh.mesh(triangle).supportingPlane.normal.sameDirection(direction)) { 1 } else { -1 }))
          } else {
            volumes += (mesh.mesh(triangle).volume -> (if (mesh.mesh(triangle).supportingPlane.normal.sameDirection(direction)) { volumes(mesh.mesh(triangle).volume) + 1 } else { volumes(mesh.mesh(triangle).volume) - 1 }))
          }
        }
      }
    }
    var nearLamp = Double.PositiveInfinity
    var lampProxima: (Int, Vector3D, Double, Double) = null
    for (lamp <- 0 until mesh.lamps.length) {
      val lampCollision = mesh.lamps(lamp).isCollide(mesh, triangleIndex, this)
      if (lampCollision != null && lampCollision._2 < time && lampCollision._2 < nearLamp && lampCollision._2 > Constants.EPSILON) {
        nearLamp = lampCollision._2
        lampProxima = (lamp, lampCollision._1, lampCollision._2, lampCollision._3)
      }
    }
    ((index, hitPoint, time), volumes, lampProxima)
  }

  @inline def renderVolume(mesh: Mesh, world: World, triangleIndex: Int, shadersLeft: Int, afterColor: Color, tracingResults: ((Int, Vector3D, Double), HashMap[VolumeOutput, Int], (Int, Vector3D, Double, Double))): Color = {
    val hitTheSurface = tracingResults._1
    val smokes: HashMap[VolumeOutput, Int] = tracingResults._2
    var result: Color = afterColor
    val stepNumber: Int = Math.min(Constants.VOLUME_MAX_STEPS, (tracingResults._1._3 / Constants.VOLUME_STEP_SIZE).asInstanceOf[Int])
    if (tracingResults._2 != null && tracingResults._2.nonEmpty)
      for (i <- (0 until stepNumber).reverse) {
        for ((material: VolumeOutput, quantity: Int) <- tracingResults._2) {
          val coordinates: Vector3D = origin + direction * (Constants.VOLUME_STEP_SIZE * i) + (direction * Math.random().asInstanceOf[Float] * Constants.VOLUME_STEP_SIZE)
          for (q <- 0 until quantity) {
            material.run(mesh, world, triangleIndex, this, hitTheSurface._2, coordinates, result, shadersLeft)
            result = material.outputs(0).content.asInstanceOf[Color]
          }
        }
      }
    result
  }

  def renderSample(mesh: Mesh, world: World, triangleIndex: Int, shadersLeft: Int): (Boolean, Color) = {
    val tracingResults = traceRay(mesh, world, triangleIndex)
    val hitTheSurface = tracingResults._1
    val smokes: HashMap[VolumeOutput, Int] = tracingResults._2
    val lamp = tracingResults._3
    val newTriangleIndex = if (hitTheSurface != null && hitTheSurface._3 > Constants.EPSILON) hitTheSurface._1 else -1
    if (hitTheSurface._2 != null && mesh.mesh(hitTheSurface._1).surface != null && hitTheSurface._3 > Constants.EPSILON) {
      mesh.mesh(tracingResults._1._1).surface.run(mesh, world, newTriangleIndex, this, hitTheSurface._2, null, null, shadersLeft)
    }
    if (lamp != null) {
      mesh.lamps(lamp._1).output.run(mesh, world, newTriangleIndex, this, hitTheSurface._2, null, null, shadersLeft)
      val result = (false, renderVolume(mesh, world, triangleIndex, shadersLeft, mesh.lamps(lamp._1).output.outputs(0).content.asInstanceOf[Color], tracingResults))
      return result
    }
    if (hitTheSurface._2 != null && hitTheSurface._3 > Constants.EPSILON) {
      (true, renderVolume(mesh, world, triangleIndex, shadersLeft, mesh.mesh(tracingResults._1._1).surface.outputs(0).content.asInstanceOf[Color], tracingResults))
    } else {
      world.skySurface.run(mesh, world, -1, this, null, null, null, shadersLeft)
      (false, world.skySurface.outputs(0).content.asInstanceOf[Color])
    }
  }

  override def toString = s"[origin: $origin, direction: $direction]"
}