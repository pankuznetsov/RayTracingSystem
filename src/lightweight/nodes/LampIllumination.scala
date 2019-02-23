package lightweight.nodes

import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.{Functions, World}

case class LampIllumination(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, shadersLeft: Int): Unit = {
    val integral = throwToLights(mesh, world, triangleIndex, ray, hitPoint, Functions.getNormal(mesh.mesh(triangleIndex), ray), shadersLeft)
    outputs(0).content = LampIlluminationOutput(integral._2, integral._1)
  }
}
