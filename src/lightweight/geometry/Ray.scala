package lightweight.geometry

import lightweight.{Lamp, PointLight, World}
import lightweight.nodes.{Color, VolumeOutput}

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
        if (intersection != null && mesh.mesh(triangle).surface != null && intersection._2 < time) {
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
      val lampCollision = mesh.lamps(lamp).isCollide(this)
      if (lampCollision != null && lampCollision._2 < time && lampCollision._2 < nearLamp) {
        nearLamp = lampCollision._2
        lampProxima = (lamp, lampCollision._1, lampCollision._2, lampCollision._3)
      }
    }
    ((index, hitPoint, time), volumes, lampProxima)
  }

  def renderSample(mesh: Mesh, world: World, triangleIndex: Int, shadersLeft: Int): Color = {
    val tracingResults = traceRay(mesh, world, triangleIndex)
    val hitTheSurface = tracingResults._1
    val smokes: HashMap[VolumeOutput, Int] = tracingResults._2
    val lamp = tracingResults._3
    // Lamps
    if (hitTheSurface != null)
      mesh.mesh(tracingResults._1._1).surface.run(mesh, world, triangleIndex, this, hitTheSurface._2, shadersLeft)
    if (lamp != null)
      return mesh.lamps(tracingResults._1._1).output.outputs(0).content.asInstanceOf[Color]
    if (hitTheSurface._2 != null) {
      // println(s"hit: ${mesh.mesh(hitTheSurface._1).surface.outputs(0).content}")
      return mesh.mesh(hitTheSurface._1).surface.outputs(0).content.asInstanceOf[Color]
    } else
      return Color(0, 0, 0)
  }

  override def toString = s"[origin: $origin, direction: $direction]"
}