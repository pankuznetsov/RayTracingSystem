package lightweight.geometry

object Strings {

  val COMMENT: String = "#"
  val VERTEX: String = "v"
  val TEXTURE_COORDINATES: String = "v"
  val VERTEX_NORMALS: String = "v"
  val FACE: String = "f"
}

class Loader(obj: String) {

  val rawObj: String = obj.clone().asInstanceOf[String]
  var pointer: Int = 0

  private def skipTrash(): Unit = {
    while (pointer < rawObj.length && !rawObj.charAt(pointer).asInstanceOf[String].matches("[a-z0-9\\/\.]"))
      pointer += 1
  }

  private def skipToNewLine(): Unit = {
    while (pointer < rawObj.length && !rawObj.charAt(pointer).asInstanceOf[String].matches("[\n\r]"))
      pointer += 1
  }

  private def parseLine(): Unit = {
    if (rawObj.charAt(pointer).eq(Strings.COMMENT.charAt(0))) {
      pointer += 1
      skipToNewLine()
    }
  }

  def loadObj(): Mesh = {

  }
}
