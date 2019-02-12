package heavyweight.nodes

import lightweight.{World, geometry}
import lightweight.geometry.{Constants, Mesh, Ray, Vector3D}
import lightweight.nodes.{Container, Node}

case class Fresnel(override val inputs: Array[Container], override val outputs: Array[Container], val worldIOR: Double) extends Node(inputs, outputs) {

  /*
    0.  material IOR
  */

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    val angle: Double = if (mesh.mesh(triangleIndex).supportingPlane.normal.sameDirection(ray.direction)) {
      val temporary = 1 - (ray.direction + mesh.mesh(triangleIndex).supportingPlane.normal).magnitude / 0.7071067811865476
      outputs(0).content = lightweight.nodes.Numeric(Math.asin((inputs(0).asInstanceOf[lightweight.nodes.Numeric].value / worldIOR) * Constants.RADIANS_TURNOVER) + temporary)
      temporary
    } else {
      val temporary = (ray.direction + mesh.mesh(triangleIndex).supportingPlane.normal).magnitude / 0.7071067811865476
      outputs(0).content = lightweight.nodes.Numeric(Math.asin((worldIOR / inputs(0).asInstanceOf[lightweight.nodes.Numeric].value) * Constants.RADIANS_TURNOVER) + temporary)
      temporary
    }
    outputs
  }
}
