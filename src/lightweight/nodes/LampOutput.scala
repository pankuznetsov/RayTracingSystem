package lightweight.nodes

import lightweight.{RayOriginInfo, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}

case class LampOutput(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def run(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    if (shadersLeft >= 0) {
      for (field <- inputs)
        if (field != null)
          field.parentNode.run(mesh: Mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft - 1, null)
      doThings(mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft - 1, null)
      newFrame
    }
  }

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    outputs(0).content = inputs(0).content
  }
}
