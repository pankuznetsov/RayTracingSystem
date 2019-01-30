package lightweight.geometry

import scala.math.{pow, sqrt}

case class Vector2D(x: Double, y: Double) {

  def +(other: Vector2D) = Vector2D(x + other.x, y + other.y)
  def -(other: Vector2D) = Vector2D(x - other.x, y - other.y)
  def *(other: Vector2D) = Vector2D(x * other.x, y * other.y)
  def *(scalar: Double) = Vector2D(x * scalar, y * scalar)
  def /(other: Vector2D) = Vector2D(x / other.x, y / other.y)

  def dotProduct(other: Vector2D) = x * other.x + y * other.y

  def magnitude = sqrt(pow(x, 2) + pow(y, 2))

  def normalized: Vector2D = {
    val module: Double = magnitude
    Vector2D(x / module, y / module)
  }

  def invert(): Vector2D = this * -1

  def sameDirection(other: Vector2D): Boolean = {
    val c = this + other
    val cLength: Double = pow(c.x, 2) + pow(c.y, 2)
    cLength > 2
  }

  def linearInterpolation(other: Vector2D, mix: Double): Vector2D = (this * (1 - mix)) + (other * mix)

  override def toString = s"[$x, $y]"
}
