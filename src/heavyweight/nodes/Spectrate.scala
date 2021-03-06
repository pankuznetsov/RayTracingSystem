package heavyweight.nodes

import lightweight.{Functions, RayOriginInfo, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class Spectrate(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    val vector = Functions.toVector(inputs(0).content)
    outputs(0).content = lightweight.nodes.Numeric(vector.x)
    outputs(1).content = lightweight.nodes.Numeric(vector.y)
    outputs(2).content = lightweight.nodes.Numeric(vector.z)
  }
}
