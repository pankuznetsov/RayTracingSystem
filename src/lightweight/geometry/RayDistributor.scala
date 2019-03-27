package lightweight.geometry

import java.lang.Math._

import lightweight.RayOriginInfo

import scala.collection.mutable.ListBuffer
import scala.util.Random

object RayDistributor {

  var rays: Array[Vector3D] = _

  def generate(n: Int): Unit = {
    rays = Array.ofDim[Vector3D](n)
    for (i <- 0 until n) {
      rays(i) = Vector3D(random().asInstanceOf[Float] - 0.5f, random().asInstanceOf[Float] - 0.5f, random().asInstanceOf[Float] - 0.5f).normalized
    }
  }

  def getRandomRays(normal: Vector3D, vector: Vector3D, n: Int, scattering: Float, rayOriginInfo: RayOriginInfo): Array[Vector3D] =
    (for (i <- 0 until n)
          yield {
            val r = (vector + (newRandomVector3D() * (scattering))).normalized
            if (normal != null) {if (r.sameDirection(normal)) r else vector } else r
          }).toArray

  def getRandomVector3D(): Vector3D = rays((random() * rays.length).asInstanceOf[Int])

  def newRandomVector3D(): Vector3D = Vector3D(Math.random().asInstanceOf[Float] - 0.5f, Math.random().asInstanceOf[Float] - 0.5f, Math.random().asInstanceOf[Float] - 0.5f).normalized
}
