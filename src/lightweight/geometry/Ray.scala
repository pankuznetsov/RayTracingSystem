package lightweight.geometry

import lightweight.{Lamp, World}
import lightweight.nodes.{Color, VolumeOutput}

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

case class Ray(origin: Vector3D, direction: Vector3D) {

  def traceRay(mesh: Mesh, world: World, triangleIndex: Int): ((Int, Vector3D, Double), HashMap[VolumeOutput, Int], HashMap[Int, (Vector3D, Double, Double)]) = {
    var volumes: HashMap[VolumeOutput, Int] = HashMap.empty[VolumeOutput, Int]
    var lamps: HashMap[Int, (Vector3D, Double, Double)] = HashMap.empty[Int, (Vector3D, Double , Double)]
    var index: Int = 0
    var hitPoint: Vector3D = null
    var time: Double = Double.PositiveInfinity
    for (triangle <- mesh.mesh.indices) {
      if (triangle != triangleIndex) {
        val intersection = mesh.mesh(triangle).intersectionWithRay(this)
        println("intersection: " + intersection)
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
    for (lamp <- mesh.lamps.indices) {
      val lampCollision = mesh.lamps(lamp).isCollide(this)
      if (lampCollision != null && lampCollision._2 < time)
        lamps += (lamp -> lampCollision)
    }
    ((index, hitPoint, time), volumes, lamps)
  }

  def renderSample(mesh: Mesh, world: World, triangleIndex: Int, bouncesLeft: Int): Color = {
    val tracingResults = traceRay(mesh, world, triangleIndex)
    return null
  }

  override def toString = s"[origin: $origin, direction: $direction]"
}