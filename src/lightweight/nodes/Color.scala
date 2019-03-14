package lightweight.nodes

import lightweight.geometry.Vector3D

case class Color(red: Float, green: Float, blue: Float) extends RootType() {

  def +(other: Color): Color = Color(this.red + other.red, this.green + other.green, this.blue + other.blue)

  def -(other: Color): Color = Color(this.red - other.red, this.green - other.green, this.blue - other.blue)

  def *(other: Color): Color = Color(this.red * other.red, this.green * other.green, this.blue * other.blue)

  def *(other: Float): Color = Color(this.red * other, this.green * other, this.blue * other)

  def /(other: Color): Color = Color(this.red / other.red, this.green / other.green, this.blue / other.blue)

  def /(other: Float): Color = Color(this.red / other, this.green / other, this.blue / other)

  def linearInterpolation(other: Color, mix: Float): Color = (this * (1 - mix)) + (other * mix)

  def colorToVector: Vector3D = Vector3D(red, green, blue).normalized

  def toNumeric: Numeric = Numeric((red + green + blue) / 3)
}