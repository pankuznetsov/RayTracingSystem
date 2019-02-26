package heavyweight.nodes

import lightweight.{Functions, World}
import lightweight.geometry._
import lightweight.nodes.{Color, Container, Node}

case class GetUV(override val inputs: Array[Container], override val outputs: Array[Container], uwMap: UVMap) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
    val triangle = mesh.mesh(triangleIndex)
    val triangleUVCoordinatesOnTheMap: Option[UVCoordinates] = triangle.uwCoordinates.get(uwMap)
    if (triangleUVCoordinatesOnTheMap.nonEmpty) {
      val uvCoordinates: UVCoordinates = triangleUVCoordinatesOnTheMap.get
      val barycentric = triangle.getBarycentric(hitPoint)
      val cartesian = Functions.barycentricToCartesian(uvCoordinates, barycentric)
      outputs(0).content = cartesian
    } else {
      throw new NullPointerException
    }
  }
}
