package lightweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}

case class LampOutput(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def run(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, shadersLeft: Int): Unit = {
    if (shadersLeft >= 0) {
      for (field <- inputs)
        if (field != null)
          field.parentNode.run(mesh: Mesh, world, triangleIndex, ray, hitPoint, coordinates, shadersLeft - 1)
      doThings(mesh, world, triangleIndex, ray, hitPoint, coordinates, shadersLeft - 1)
      newFrame
    }
  }

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, shadersLeft: Int): Unit = {
    outputs(0).content = inputs(0).content
  }
}
