package lightweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}

case class VolumeOutput(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def run(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    for (field <- inputs)
      if (field != null)
        field.parentNode.run(mesh: Mesh, world, triangleIndex, ray, hitPoint, shadersLeft - 1)
    doThings(mesh, world, triangleIndex, ray, hitPoint, shadersLeft - 1)
  }

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    outputs(0).content = inputs(0).content
  }
}
