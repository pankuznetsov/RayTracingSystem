package lightweight

import java.awt.image.BufferedImage

import lightweight.geometry.{Triangle, UVCoordinates, Vector2D, Vector3D}
import lightweight.nodes.Color

object Functions {

  def interpolate(image: BufferedImage, hitPoint: Vector2D): Color = {
    val rgb = image.getRGB(Math.floor(hitPoint.x).asInstanceOf[Int], Math.floor(hitPoint.y).asInstanceOf[Int])
    val red = (rgb & 0x00ff0000) >> 16
    val green = (rgb & 0x0000ff00) >> 8
    val blue = (rgb & 0x000000ff) >> 0
    return lightweight.nodes.Color(red, green, blue) / 256
  }

  def barycentricToCartesian(uwCoordinates: UVCoordinates, uvw: (Double, Double, Double)): Vector2D =
    Vector2D(uvw._1 * uwCoordinates.a.x + uvw._2 * uwCoordinates.b.x + uvw._3 * uwCoordinates.c.x,
      uvw._1 * uwCoordinates.a.y + uvw._2 * uwCoordinates.b.y + uvw._3 * uwCoordinates.c.y)

  def rotate(mesh: Array[Vector3D], pitch: Double, roll: Double, yaw: Double): IndexedSeq[Vector3D] = {
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
      vertex = Vector3D(axisXX * mesh(index).x + axisXY * mesh(index).y + axisXZ * mesh(index).z,
        axisYX * mesh(index).x + axisYY * mesh(index).y + axisYZ * mesh(index).z,
        axisZX * mesh(index).x + axisZY * mesh(index).y + axisZZ * mesh(index).z)
    } yield vertex
    rotatedMesh
  }
}
