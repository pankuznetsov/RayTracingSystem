package lightweight.nodes

import lightweight.{RayOriginInfo, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}

case class SurfaceOutput(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def run(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    if (shadersLeft > 0) {
      for (field <- inputs)
        if (field != null)
          field.parentNode.run(mesh: Mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft - 1, rayOriginInfo)
      doThings(mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft - 1, rayOriginInfo)
      newFrame
    } else outputs(0).content = Color(0, 0, 0)
  }

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    outputs(0).content = if (inputs(0).content != null) { inputs(0).content } else { Color(0, 0, 0) }
  }
}
