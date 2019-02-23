package lightweight.geometry

import lightweight.{Lamp, World}
import lightweight.nodes.{Color, ColorWithDensity, VolumeOutput}

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

case class Ray(origin: Vector3D, direction: Vector3D) {

  def traceRay(mesh: Mesh, world: World, triangleIndex: Int): ((Int, Vector3D, Double), HashMap[VolumeOutput, Int], (Int, Vector3D, Double, Double)) = {
    var volumes: HashMap[VolumeOutput, Int] = HashMap.empty[VolumeOutput, Int]
    var lamps: HashMap[Int, (Vector3D, Double, Double)] = HashMap.empty[Int, (Vector3D, Double , Double)]
    var index: Int = 0
    var hitPoint: Vector3D = null
    var time: Double = Double.PositiveInfinity
    for (triangle <- mesh.mesh.indices) {
      if (triangle != triangleIndex) {
        val intersection = mesh.mesh(triangle).intersectionWithRay(this)
        if (intersection != null && intersection._2 < time) {
          index = triangle
          hitPoint = intersection._1
          time = intersection._2
        }
        if (intersection != null && mesh.mesh(triangle).volume != null) {
          if (!volumes.contains(mesh.mesh(triangle).volume)) {
            volumes += (mesh.mesh(triangle).volume -> (if (mesh.mesh(triangle).supportingPlane.normal.sameDirection(direction)) { -1 } else { 1 }))
          } else {
            volumes += (mesh.mesh(triangle).volume -> (if (mesh.mesh(triangle).supportingPlane.normal.sameDirection(direction)) { volumes(mesh.mesh(triangle).volume) - 1 } else { volumes(mesh.mesh(triangle).volume) + 1 }))
          }
        }
      }
    }
    var nearLamp = Double.PositiveInfinity
    var lampProxima: (Int, Vector3D, Double, Double) = null
    for (lamp <- mesh.lamps.indices) {
      val lampCollision = mesh.lamps(lamp).isCollide(mesh, triangleIndex, this)
      if (lampCollision != null && lampCollision._2 < time && lampCollision._2 < nearLamp && lampCollision._2 > Constants.EPSILON) {
        nearLamp = lampCollision._2
        lampProxima = (lamp, lampCollision._1, lampCollision._2, lampCollision._3)
      }
    }
    ((index, hitPoint, time), volumes, lampProxima)
  }

  def renderVolume(mesh: Mesh, world: World, triangleIndex: Int, shadersLeft: Int, tracingResults: ((Int, Vector3D, Double), HashMap[VolumeOutput, Int], (Int, Vector3D, Double, Double))): ColorWithDensity = {
    val hitTheSurface = tracingResults._1
    val smokes: HashMap[VolumeOutput, Int] = tracingResults._2
    var densityAndColorResult: ColorWithDensity = ColorWithDensity(0, 0, 0, 0)
    // println("Volumes rendering")
    val steps: Int = (hitTheSurface._3 / Constants.VOLUME_STEP_SIZE).asInstanceOf[Int]
    for (point <- 0 until steps) {
      var pointResult: ColorWithDensity = ColorWithDensity(0, 0, 0, 0)
      val coordinates: Vector3D = origin + direction * (Constants.VOLUME_STEP_SIZE * point) + RayDistributor.newRandomVector3D() * Constants.VOLUME_STEP_SIZE
      for ((material, quantity) <- smokes) {
        material.run(mesh, world, triangleIndex, this, hitTheSurface._2, coordinates, shadersLeft)
        pointResult = pointResult + material.outputs(0).content.asInstanceOf[ColorWithDensity]
      }
      val densityForDivision: Float = Math.max(1, densityAndColorResult.density)
      densityAndColorResult = densityAndColorResult + pointResult / densityForDivision
    }
    // println("renderVolume: " + densityAndColorResult)
    densityAndColorResult
  }

  def renderSample(mesh: Mesh, world: World, triangleIndex: Int, shadersLeft: Int): (Boolean, Color) = {
    val tracingResults = traceRay(mesh, world, triangleIndex)
    val hitTheSurface = tracingResults._1
    val smokes: HashMap[VolumeOutput, Int] = tracingResults._2
    val lamp = tracingResults._3
    val newTriangleIndex = if (hitTheSurface != null && hitTheSurface._3 > Constants.EPSILON) hitTheSurface._1 else -1
    if (hitTheSurface._2 != null && mesh.mesh(hitTheSurface._1).surface != null && hitTheSurface._3 > Constants.EPSILON) {
      mesh.mesh(tracingResults._1._1).surface.run(mesh, world, newTriangleIndex, this, hitTheSurface._2, null, shadersLeft)
    }
    if (lamp != null) {
      val volumeColorWithDensity = if (tracingResults._2 != null && tracingResults._2.nonEmpty) renderVolume(mesh, world, triangleIndex, shadersLeft, tracingResults) else null
      mesh.lamps(lamp._1).output.run(mesh, world, newTriangleIndex, this, hitTheSurface._2, null, shadersLeft)
      if (volumeColorWithDensity == null)
        return (true, mesh.lamps(lamp._1).output.outputs(0).content.asInstanceOf[Color])
      else {
        val volumeColor = Color(volumeColorWithDensity.red, volumeColorWithDensity.green, volumeColorWithDensity.blue)
        return (true, volumeColor * Math.min(1, volumeColorWithDensity.density) + mesh.lamps(lamp._1).output.outputs(0).content.asInstanceOf[Color] / Math.max(1, volumeColorWithDensity.density))
      }
    }
    if (hitTheSurface._2 != null && hitTheSurface._3 > Constants.EPSILON) {
      val volumeColorWithDensity = if (tracingResults._2 != null && tracingResults._2.nonEmpty) renderVolume(mesh, world, triangleIndex, shadersLeft, tracingResults) else null
      if (volumeColorWithDensity == null)
        return (true, mesh.mesh(hitTheSurface._1).surface.outputs(0).content.asInstanceOf[Color])
      else {
        val volumeColor = Color(volumeColorWithDensity.red, volumeColorWithDensity.green, volumeColorWithDensity.blue)
        return (true, volumeColor * Math.min(1, volumeColorWithDensity.density) + mesh.mesh(hitTheSurface._1).surface.outputs(0).content.asInstanceOf[Color] / Math.max(1, volumeColorWithDensity.density))
      }
      // return (true, mesh.mesh(hitTheSurface._1).surface.outputs(0).content.asInstanceOf[Color])
    } else {
      world.skySurface.run(mesh, world, -1, this, null, null, shadersLeft)
      return (false, world.skySurface.outputs(0).content.asInstanceOf[Color])
    }
  }

  override def toString = s"[origin: $origin, direction: $direction]"
}