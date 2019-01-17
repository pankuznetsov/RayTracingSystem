package lightweight.nodes

case class Color(red: scala.Float, green: scala.Float, blue: scala.Float) extends RootType() {

  def +(other: Color): Color = Color(this.red + other.red, this.green + other.green, this.blue + other.blue)

  def -(other: Color): Color = Color(this.red - other.red, this.green - other.green, this.blue - other.blue)

  def *(other: Color): Color = Color(this.red * other.red, this.green * other.green, this.blue * other.blue)

  def /(other: Color): Color = Color(this.red / other.red, this.green / other.green, this.blue / other.blue)
}