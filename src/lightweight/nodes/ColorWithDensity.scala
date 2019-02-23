package lightweight.nodes

case class ColorWithDensity(red: Float, green: Float, blue: Float, density: Float) extends RootType {

  def +(other: ColorWithDensity): ColorWithDensity = ColorWithDensity(red + other.red, green + other.green, blue + other.blue, density + other.density)

  def *(other: ColorWithDensity): ColorWithDensity = ColorWithDensity(red * other.red, green * other.green, blue * other.blue, density * other.density)

  def *(other: Float): ColorWithDensity = ColorWithDensity(red * other, green * other, blue * other, density * other)

  def /(other: ColorWithDensity): ColorWithDensity = ColorWithDensity(red / other.red, green / other.green, blue / other.blue, density / other.density)

  def /(other: Float): ColorWithDensity = ColorWithDensity(red / other, green / other, blue / other, density / other)

  def mix(other: ColorWithDensity, mix: Float): ColorWithDensity = (this * (1 - mix)) + (other * mix)

  override def toString: String = s"[$red, $green, $blue, d = $density]"
}