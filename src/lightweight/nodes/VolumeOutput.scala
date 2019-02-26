package lightweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}

case class VolumeOutput(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def run(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
    if (shadersLeft >= 0) {
      for (field <- inputs)
        if (field != null)
          field.parentNode.run(mesh: Mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft - 1)
      doThings(mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft - 1)
      newFrame
    } else return
  }

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
    outputs(0).content = inputs(0).content
  }
}
