package heavyweight.nodes

import lightweight.{World, geometry}
import lightweight.geometry.{Constants, Mesh, Ray, Vector3D}
import lightweight.nodes.{Container, Node}

case class Fresnel(override val inputs: Array[Container], override val outputs: Array[Container], val worldIOR: Double) extends Node(inputs, outputs) {

  /*
    0.  material IOR
  */

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    val normal =  if (mesh.mesh(triangleIndex).supportingPlane.normal.sameDirection(ray.direction)) {
      mesh.mesh(triangleIndex).supportingPlane.normal.invert()} else {
      mesh.mesh(triangleIndex).supportingPlane.normal
    }
    val angle = Math.acos(ray.direction dotProduct mesh.mesh(triangleIndex).supportingPlane.normal)
    val comFromAtmofiare = !mesh.mesh(triangleIndex).supportingPlane.normal.sameDirection(ray.direction)
    val criticalAngle = Math.asin(if (comFromAtmofiare){inputs(0).asInstanceOf[Double] / worldIOR} else {worldIOR / inputs(0).asInstanceOf[Double]})
    outputs(0).content = lightweight.nodes.Numeric((angle + criticalAngle) * Constants.ONE_DIVIDE_ON_RADIANS_TURNOVER)
  }
}
