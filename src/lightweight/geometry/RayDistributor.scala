package lightweight.geometry

import scala.collection.mutable.ListBuffer

object RayDistributor {

  var rays: Array[Vector3D] = null

  def generate(n: Int): Unit = {
    rays = Array.ofDim[Vector3D](n)
    for (i <- 0 until n) {
      rays(i) = Vector3D(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalized
    }
  }

  def getOne(normal: Vector3D): Array[Vector3D] = {
    var list: ListBuffer[Vector3D] = ListBuffer[Vector3D]()
    for (i <- rays)
      if (i.sameDirection(normal))
        list += i
    list.toArray[Vector3D]
  }

  def getRandomRays(normal: Vector3D, vector: Vector3D, n: Int, scattering: Double): Array[Vector3D] = {
    var list: ListBuffer[Vector3D] = ListBuffer[Vector3D]()
    while (list.length < n) {
      list += vector.linearInterpolation(rays((Math.random() * rays.length).asInstanceOf[Int]), scattering).normalized
    }
    list.toArray[Vector3D]
  }

  def getRandomVector3D(): Vector3D = rays((Math.random() * rays.length).asInstanceOf[Int])

  def newRandomVector3D(): Vector3D = Vector3D(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalized
}
