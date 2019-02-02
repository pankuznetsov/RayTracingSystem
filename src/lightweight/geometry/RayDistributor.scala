package lightweight.geometry

import scala.collection.mutable.ListBuffer
import scala.util.Random

object RayDistributor {

  var rays: Array[Vector3D] = _

  def generate(n: Int): Unit = {
    rays = Array.ofDim[Vector3D](n)
    for (i <- 0 until n) {
      rays(i) = Vector3D(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalized
    }
  }

  def getRandomRays(normal: Vector3D, vector: Vector3D, n: Int, scattering: Double): Array[Vector3D] = {
    var list: ListBuffer[Vector3D] = ListBuffer[Vector3D]()
    val random = new Random()
    while (list.length < n) {
      val random = vector.linearInterpolation(getRandomVector3D(), scattering).normalized
      if (random.sameDirection(normal))
        list += random
    }
    list.toArray[Vector3D]
  }

  def getRandomVector3D(): Vector3D = rays((Math.random() * rays.length).asInstanceOf[Int])

  def newRandomVector3D(): Vector3D = Vector3D(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalized
}
