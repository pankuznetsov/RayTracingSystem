package lightweight

import java.awt.image.BufferedImage

import lightweight.geometry._
import lightweight.nodes.{Color, RootType}

object Functions {

  @inline def clamp[@specialized(Int, Double) T: Ordering](value: T, low: T, high: T): T = {
    import Ordered._
    if (value < low) low else if (value > high) high else value
  }

  def interpolate(image: BufferedImage, hitPoint: Vector2D): Color = {
    val x = Math.abs(Math.floor(hitPoint.x).asInstanceOf[Int] % image.getWidth)
    val y = Math.abs(Math.floor(hitPoint.y).asInstanceOf[Int] % image.getHeight)
    /* if (x < 0 || y < 0 || x >= image.getWidth || y >= image.getHeight)
      return lightweight.nodes.Color(1, 1, 1) */
    val rgb = image.getRGB(x, y)
    val red = (rgb & 0x00ff0000) >> 16
    val green = (rgb & 0x0000ff00) >> 8
    val blue = (rgb & 0x000000ff) >> 0
    lightweight.nodes.Color(red, green, blue) / 256
  }

  def toColor(colorOrVector: RootType): Color = colorOrVector match {
    case vector: Vector3D => vector.toColor
    case color: Color => color
    case null => null
    case _ => null
  }

  def toVector(colorOrVector: RootType): Vector3D = colorOrVector match {
    case color: Color => color.colorToVector
    case vector: Vector3D => vector
    case null => null
    case _ => null
  }

  def toNumeric(colorOrNumeric: RootType): lightweight.nodes.Numeric = colorOrNumeric match {
    case color: Color => color.toNumeric
    case numeric: lightweight.nodes.Numeric => numeric
    case null => null
    case _ => null
  }

  def getNormal(triangle: Triangle, ray: Ray): Vector3D = if (triangle.supportingPlane.normal.sameDirection(ray.direction))
    triangle.supportingPlane.normal.invert else triangle.supportingPlane.normal

  def barycentricToCartesian(uwCoordinates: UVCoordinates, uvw: (Float, Float, Float)): Vector2D =
    Vector2D(uvw._1 * uwCoordinates.a.x + uvw._2 * uwCoordinates.b.x + uvw._3 * uwCoordinates.c.x,
      uvw._1 * uwCoordinates.a.y + uvw._2 * uwCoordinates.b.y + uvw._3 * uwCoordinates.c.y)

  def rotate(mesh: Array[Vector3D], pitch: Float, roll: Float, yaw: Float): IndexedSeq[Vector3D] = {
    val cosYaw = Math.cos(yaw)
    val sinYaw = Math.sin(yaw)

    val cosPitch = Math.cos(pitch)
    val sinPitch = Math.sin(pitch)

    val cosRoll = Math.cos(roll)
    val sinRoll = Math.sin(roll)

    val axisXX = cosYaw * cosPitch
    val axisXY = cosYaw * sinPitch * sinRoll - sinYaw * cosRoll
    val axisXZ = cosYaw * sinPitch * cosRoll + sinYaw * sinRoll

    val axisYX = sinYaw * cosPitch
    val axisYY = sinYaw * sinPitch * sinRoll + cosYaw * cosRoll
    val axisYZ = sinYaw * sinPitch * cosRoll - cosYaw * sinRoll

    val axisZX = -sinPitch
    val axisZY = cosPitch * sinRoll
    val axisZZ = cosPitch * cosRoll

    val rotatedMesh: IndexedSeq[Vector3D] = for {
      index <- mesh.indices
      vertex = Vector3D((axisXX * mesh(index).x + axisXY * mesh(index).y + axisXZ * mesh(index).z).asInstanceOf[Float],
        (axisYX * mesh(index).x + axisYY * mesh(index).y + axisYZ * mesh(index).z).asInstanceOf[Float],
        (axisZX * mesh(index).x + axisZY * mesh(index).y + axisZZ * mesh(index).z).asInstanceOf[Float])
    } yield vertex
    rotatedMesh
  }
}
