package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Container, Node}

case class Geomerty(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  /* The Geometry node outputs are:
    0. Hit point location
    1. Incoming ray
    2. Flat normal
    3. Smooth normal
    4. Back facing
   */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    outputs(0).content = hitPoint
    outputs(1).content = ray.direction
    outputs(2).content = if (mesh.mesh(triangleIndex).supportingPlane.normal.sameDirection(ray.direction))
      mesh.mesh(triangleIndex).supportingPlane.normal.invert() else mesh.mesh(triangleIndex).supportingPlane.normal
    outputs(3).content = if (mesh.mesh(triangleIndex).supportingPlane.normal.sameDirection(ray.direction))
      mesh.mesh(triangleIndex).supportingPlane.normal.invert() else mesh.mesh(triangleIndex).supportingPlane.normal
    outputs(4).content = if (mesh.mesh(triangleIndex).supportingPlane.normal.sameDirection(ray.direction)) lightweight.nodes.Numeric(0) else lightweight.nodes.Numeric(1)
  }
}