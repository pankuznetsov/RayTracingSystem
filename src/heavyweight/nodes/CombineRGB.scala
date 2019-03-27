package heavyweight.nodes

import lightweight.{RayOriginInfo, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class CombineRGB(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    val iZero: Double = inputs(0).content.asInstanceOf[lightweight.nodes.Numeric].value
    val iOne: Double = inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value
    val iTwo: Double = inputs(2).content.asInstanceOf[lightweight.nodes.Numeric].value
    outputs(0).content = Color(iZero.asInstanceOf[Float], iOne.asInstanceOf[Float], iTwo.asInstanceOf[Float])
  }
}
