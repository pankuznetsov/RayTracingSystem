package heavyweight.nodes

import lightweight.{Functions, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class RGBToBlackWhite(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    val color = Functions.toColor(inputs(0).content)
    val blackWhiteDouble = (color.red + color.green + color.blue) / 3
    val blackWhiteColor: Color = Color(blackWhiteDouble, blackWhiteDouble, blackWhiteDouble)
    outputs(0).content = blackWhiteColor
    outputs(1).content = lightweight.nodes.Numeric(blackWhiteDouble)
  }
}
