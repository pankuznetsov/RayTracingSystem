package heavyweight.nodes

import java.awt.image.BufferedImage

import lightweight.{Functions, World}
import lightweight.geometry._
import lightweight.nodes.{Color, Container, Node}

case class ImageTexture(override val inputs: Array[Container], override val outputs: Array[Container], image: BufferedImage) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
    val location = Vector2D(inputs(0).content.asInstanceOf[Vector2D].x * image.getWidth(), inputs(0).content.asInstanceOf[Vector2D].y * image.getHeight)
    val color = Functions.interpolate(image, location)
    // println(s"texture color ${color}, \tlocation: ${location}")
    outputs(0).content = color
  }
}
