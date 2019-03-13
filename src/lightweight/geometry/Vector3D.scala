package lightweight.geometry

import lightweight.Functions
import lightweight.nodes.{Color, RootType}

import scala.math.{pow, sqrt}

case class Vector3D(x: Double, y: Double, z: Double) extends RootType() {

  @inline def +(other: Vector3D) = Vector3D(x + other.x, y + other.y, z + other.z)
  @inline def -(other: Vector3D) = Vector3D(x - other.x, y - other.y, z - other.z)
  @inline def *(other: Vector3D) = Vector3D(x * other.x, y * other.y, z * other.z)
  @inline def *(scalar: Double) = Vector3D(x * scalar, y * scalar, z * scalar)
  @inline def /(other: Vector3D) = Vector3D(x / other.x, y / other.y, z / other.z)
  @inline def /(scalar: Double) = Vector3D(x / scalar, y / scalar, z / scalar)

  @inline def dotProduct(other: Vector3D) = x * other.x + y * other.y + z * other.z

  @inline def crossProduct(other: Vector3D) = Vector3D(
    y * other.z - z * other.y,
    z * other.x - x * other.z,
    x * other.y - y * other.x
  )

  @inline def magnitude = sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2))

  @inline def normalized: Vector3D = {
    val module: Double = magnitude
    Vector3D(x / module, y / module, z / module)
  }

  @inline def reflect(plane: Plane): Vector3D = {
    val noramlizedV = this.normalized
    plane.normal * ((plane.normal dotProduct noramlizedV) * 2) - noramlizedV
  }

  @inline def refract(normal: Vector3D, inIOR: Double, outIOR: Double): Vector3D = {
    this + normal * this.dotProduct(normal) * (sqrt((outIOR - inIOR) / (this.dotProduct(normal)) + 1) - 1)
  }

  @inline def sameQuadrant(reference: Vector3D): Boolean = {
    ((x > 0) == (reference.x > 0)) && ((y > 0) == (reference.y > 0)) && ((z > 0) == (reference.z > 0))
  }

  @inline def invert(): Vector3D = this * -1

  @inline def sameDirection(other: Vector3D): Boolean = {
    val c = this + other
    val cLength: Double = pow(c.x, 2) + pow(c.y, 2) + pow(c.z, 2)
    cLength > 2.0 + Constants.EPSILON
  }

  @inline def backQuadrant(other: Vector3D): Boolean = (x > 0) != (other.x > 0) && (y > 0) != (other.y > 0) && (z > 0) != (other.z > 0)

  def toColor: Color = Color(Functions.clamp[Float](x.asInstanceOf[Float], 0, 1),
    Functions.clamp[Float](y.asInstanceOf[Float], 0, 1),
    Functions.clamp[Float](z.asInstanceOf[Float], 0, 1))

  @inline def linearInterpolation(other: Vector3D, mix: Double): Vector3D = (this * (1 - mix)) + (other * mix)

  override def toString = s"[$x, $y, $z]"
}
