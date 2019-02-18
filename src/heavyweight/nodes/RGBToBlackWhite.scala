package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Container, Node, Color}

case class RGBToBlackWhite(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    val blackWhiteDouble =
      (inputs(0).asInstanceOf[lightweight.nodes.Color].red +
      inputs(0).asInstanceOf[lightweight.nodes.Color].green +
      inputs(0).asInstanceOf[lightweight.nodes.Color].blue) / 3
    val blackWhiteColor = Color(blackWhiteDouble, blackWhiteDouble, blackWhiteDouble)
    outputs(0).content = blackWhiteDouble.asInstanceOf[lightweight.nodes.Numeric]
    outputs(1).content = blackWhiteColor.asInstanceOf[Color]
  }
}
