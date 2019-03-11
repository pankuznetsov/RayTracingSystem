package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class Mix(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
    val factor: Double = inputs(0).content.asInstanceOf[lightweight.nodes.Numeric].value
    val firstComponent: Color = inputs(1).content.asInstanceOf[Color]
    val secondComponent: Color = inputs(2).content.asInstanceOf[Color]
    firstComponent.linearInterpolation(secondComponent, factor)
  }
}
