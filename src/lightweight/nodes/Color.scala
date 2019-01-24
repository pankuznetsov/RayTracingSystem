package lightweight.nodes

case class Color(val red: scala.Float, val green: scala.Float, val blue: scala.Float) extends RootType() {

  def +(other: Color): Color = Color(this.red + other.red, this.green + other.green, this.blue + other.blue)

  def -(other: Color): Color = Color(this.red - other.red, this.green - other.green, this.blue - other.blue)

  def *(other: Color): Color = Color(this.red * other.red, this.green * other.green, this.blue * other.blue)

  def *(other: Float): Color = Color(this.red * other, this.green * other, this.blue * other)

  def *(other: Double): Color = Color(this.red * other.asInstanceOf[Float], this.green * other.asInstanceOf[Float], this.blue * other.asInstanceOf[Float])

  def /(other: Color): Color = Color(this.red / other.red, this.green / other.green, this.blue / other.blue)

  def /(other: Double): Color = Color(this.red / other.asInstanceOf[Float], this.green / other.asInstanceOf[Float], this.blue / other.asInstanceOf[Float])
}