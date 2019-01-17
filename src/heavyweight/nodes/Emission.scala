package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Container, Node, SurfaceOutput, VolumeOutput}

case class Emission(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    outputs(0).content = inputs(0).content
  }
}
