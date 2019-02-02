package lightweight.geometry

import lightweight.World
import lightweight.nodes.{SurfaceOutput, VolumeOutput}

import scala.collection.mutable.ListBuffer

private object Strings {

  val COMMENT: String = "#"
  val VERTEX: String = "v"
  val VERTEX_NORMALS: String = "vn"
  val TEXTURE_COORDINATES: String = "vt"
  val FACE: String = "f"
  val USE_MATERIAL = "usemtl"

  val SLASH: String = "/"
}

case class Loader(obj: String, surfaces: Array[SurfaceOutput], volumes: Array[VolumeOutput], world: World) {

  val rawObj: String = obj
  var pointer: Int = 0

  var vetices = ListBuffer[Vector3D]()
  var normals = ListBuffer[Vector3D]()
  var triangles = ListBuffer[Triangle]()

  var surfaceMaterial: Int = 0
  var volumeMaterial: Int = 0

  private def skipTrash(): Unit = {
    while (pointer < rawObj.length && !rawObj.charAt(pointer).toString.matches("[a-z0-9\\-\\/\n\r]"))
      pointer += 1
  }

  private def skipToNewLine(): Unit = {
    while (pointer < rawObj.length && !rawObj.charAt(pointer).toString.matches("[\n\r]"))
      pointer += 1
    pointer += 1
  }

  private def parseInt(): Int = {
    val number: StringBuilder = new StringBuilder()
    while (pointer < rawObj.size && (rawObj.charAt(pointer).toString.matches("[0-9\\-]") || rawObj.charAt(pointer).toString.equals("-"))) {
      number.append(rawObj.charAt(pointer))
      pointer += 1
    }
    if (number.length > 0) number.toString.toInt else 0
  }

  private def parseFloat(): Double = {
    val number: StringBuilder = new StringBuilder()
    while (pointer < rawObj.size && (rawObj.charAt(pointer).toString.matches("[0-9.\\-]") || rawObj.charAt(pointer).toString.equals("-"))) {
      number.append(rawObj.charAt(pointer))
      pointer += 1
    }
    if (number.length > 0) number.toString.toDouble else 0
  }

  private def parseVertex(): Vector3D = {
    skipTrash()
    val x = parseFloat()
    skipTrash()
    val y = parseFloat()
    skipTrash()
    val z = parseFloat()
    skipToNewLine()
    vetices += Vector3D(x, y, z)
    println(s"$x, $y, $z")
    return Vector3D(x, y, z)
  }

  private def parseTriangleVertex(): (Int, Int, Int) = {
    skipTrash()
    val first = parseInt()
    if (pointer < rawObj.length && rawObj.charAt(pointer).toString.equals(Strings.SLASH)) {
      pointer += 1
      val second = parseInt()
      if (pointer < rawObj.length && rawObj.charAt(pointer).toString.equals(Strings.SLASH)) {
        pointer += 1
        val third = parseInt()
        skipTrash()
        return (first, second, third)
      } else {
        skipTrash()
        return (first, second, 0)
      }
    } else {
      skipTrash()
      return (first, 0, 0)
    }
  }

  private def parseUseMaterial(): Unit = {
    pointer += Strings.USE_MATERIAL.length
    skipTrash()
    val isSurface: Boolean = rawObj.charAt(pointer).toString.equals("s")
    pointer += 1
    val materialNumber = parseInt()
    if (isSurface)
      surfaceMaterial = materialNumber
    else
      volumeMaterial = materialNumber
    skipToNewLine()
  }

  private def parseVertexNormal(): (Double, Double, Double) = {
    skipToNewLine()
    null
  }

  private def parseTriangle(): Triangle = {
    skipTrash()
    val first = parseTriangleVertex()
    skipTrash()
    val second = parseTriangleVertex()
    skipTrash()
    val third = parseTriangleVertex()
    skipToNewLine()
    val triangle = Triangle(vetices(first._1 - 1) * 50 + Vector3D(70, 70, 190), vetices(second._1 - 1) * 50 + Vector3D(70, 70, 190), vetices(third._1 - 1) * 50 + Vector3D(70, 70, 190), true,
      if (surfaces.length > 0 && surfaceMaterial >= 0) surfaces(surfaceMaterial) else null,
      if (volumes.length > 0 && volumeMaterial >= 0) volumes(volumeMaterial) else null, null)
    triangles += triangle
    println(s"$triangle")
    return triangle
  }

  private def parseLine(): Unit = {
    if (rawObj.charAt(pointer).equals(Strings.COMMENT.charAt(0))) {
      pointer += 1
      skipToNewLine()
    } else if (rawObj.charAt(pointer).equals(Strings.VERTEX.charAt(0)) && rawObj.charAt(pointer + 1).equals(Strings.VERTEX_NORMALS.charAt(1))) {
      pointer += 1
      parseVertex()
    } else if (rawObj.charAt(pointer).equals(Strings.VERTEX.charAt(0))) {
      pointer += 1
      parseVertex()
    } else if (rawObj.charAt(pointer).equals(Strings.FACE.charAt(0))) {
      pointer += 1
      parseTriangle()
    } else if (rawObj.charAt(pointer).equals(Strings.USE_MATERIAL.charAt(0)) && rawObj.charAt(pointer + 1).equals(Strings.USE_MATERIAL.charAt(1))) {
      pointer += 1
      parseUseMaterial()
    } else skipToNewLine()
  }

  def loadObj(): Unit = {
    while (pointer < rawObj.size)
      parseLine()
  }
}
