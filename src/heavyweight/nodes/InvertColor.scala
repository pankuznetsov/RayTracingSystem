package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class InvertColor(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
    val iZero = inputs(0).content.asInstanceOf[lightweight.nodes.Color]
    outputs(0).content = Color(1 - iZero.red, 1 - iZero.green, 1 - iZero.blue)
    outputs
  }
}
