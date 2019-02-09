package lightweight.geometry

import lightweight.Functions
import lightweight.nodes.{Color, RootType}

import scala.math.{pow, sqrt}

case class Vector3D(x: Double, y: Double, z: Double) extends RootType() {

  def +(other: Vector3D) = Vector3D(x + other.x, y + other.y, z + other.z)
  def -(other: Vector3D) = Vector3D(x - other.x, y - other.y, z - other.z)
  def *(other: Vector3D) = Vector3D(x * other.x, y * other.y, z * other.z)
  def *(scalar: Double) = Vector3D(x * scalar, y * scalar, z * scalar)
  def /(other: Vector3D) = Vector3D(x / other.x, y / other.y, z / other.z)
  def /(scalar: Double) = Vector3D(x / scalar, y / scalar, z / scalar)

  def dotProduct(other: Vector3D) = x * other.x + y * other.y + z * other.z

  def crossProduct(other: Vector3D) = Vector3D(
    y * other.z - z * other.y,
    z * other.x - x * other.z,
    x * other.y - y * other.x
  )

  def magnitude = sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2))

  def normalized: Vector3D = {
    val module: Double = magnitude
    Vector3D(x / module, y / module, z / module)
  }

  def reflect(plane: Plane): Vector3D = {
    val noramlizedV = this.normalized
    plane.normal * ((plane.normal dotProduct noramlizedV) * 2) - noramlizedV
  }

  def invert(): Vector3D = this * -1

  def sameDirection(other: Vector3D): Boolean = {
    val c = this + other
    val cLength: Double = pow(c.x, 2) + pow(c.y, 2) + pow(c.z, 2)
    cLength > 2.0 + Constants.EPSILON
  }

  def toColor: Color = Color(Functions.clamp[Float](x.asInstanceOf[Float], 0, 1),
    Functions.clamp[Float](y.asInstanceOf[Float], 0, 1),
    Functions.clamp[Float](z.asInstanceOf[Float], 0, 1))

  def linearInterpolation(other: Vector3D, mix: Double): Vector3D = (this * (1 - mix)) + (other * mix)

  override def toString = s"[$x, $y, $z]"
}
