package heavyweight.nodes

import lightweight.{Functions, RayOriginInfo, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes._

case class Emission(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    outputs(0).content = Functions.toColor(inputs(0).content) * inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value
  }
}
