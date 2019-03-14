package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class GetAngle(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
    val normal = if (mesh.mesh(triangleIndex).supportingPlane.normal.sameDirection(ray.direction)) {
      mesh.mesh(triangleIndex).supportingPlane.normal.invert()
    } else {
      mesh.mesh(triangleIndex).supportingPlane.normal
    }
    val angle: Float = Math.acos(ray.direction dotProduct normal).asInstanceOf[Float] * lightweight.geometry.Constants.ONE_DIVIDE_ON_RADIANS_TURNOVER
    outputs(0).content = Color(angle.asInstanceOf[Float], angle.asInstanceOf[Float], angle.asInstanceOf[Float])
    outputs(1).content = lightweight.nodes.Numeric(angle)
  }
}
