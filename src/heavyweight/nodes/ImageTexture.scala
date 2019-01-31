package heavyweight.nodes

import java.awt.image.BufferedImage

import lightweight.{Functions, World}
import lightweight.geometry._
import lightweight.nodes.{Color, Container, Node}

case class ImageTexture(override val inputs: Array[Container], override val outputs: Array[Container], image: BufferedImage) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    outputs(0).content = Functions.interpolate(image, inputs(0).content.asInstanceOf[Vector2D])
  }
}
