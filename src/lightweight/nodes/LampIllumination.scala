package lightweight.nodes

import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.{Functions, RayOriginInfo, World}

case class LampIllumination(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    val integral = throwToLights(mesh, world, triangleIndex, ray, hitPoint, coordinates, if (triangleIndex >= 0) Functions.getNormal(mesh.mesh(triangleIndex), ray) else null, shadersLeft)
    outputs(0).content = LampIlluminationOutput(integral._2, integral._1)
  }
}
