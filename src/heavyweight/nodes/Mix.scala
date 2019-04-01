package heavyweight.nodes

import lightweight.{Functions, RayOriginInfo, World}
import lightweight.geometry.{Constants, Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class Mix(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def run(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    if (shadersLeft >= 0 && !complite) {
      complite = true
      inputs(0).parentNode.run(mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft, rayOriginInfo)
      val factor: Float = Functions.toNumeric(inputs(0).content).value
      if (factor + Constants.EPSILON <= 0) {
        inputs(1).parentNode.run(mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft, rayOriginInfo)
        outputs(0).content = inputs(0).parentNode.outputs(0).content
        return
      } else if (factor - Constants.EPSILON >= 1) {
        inputs(2).parentNode.run(mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft, rayOriginInfo)
        outputs(0).content = inputs(1).parentNode.outputs(0).content
        return
      } else {
        inputs(1).parentNode.run(mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft, rayOriginInfo)
        inputs(2).parentNode.run(mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft, rayOriginInfo)
        doThings(mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft, rayOriginInfo)
      }
    }
  }

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    val factor: Float = inputs(0).content.asInstanceOf[lightweight.nodes.Numeric].value
    val firstComponent: Color = Functions.toColor(inputs(1).content)
    val secondComponent: Color = Functions.toColor(inputs(2).content)
    outputs(0).content = firstComponent.linearInterpolation(secondComponent, if (factor > 1) { 1 } else { if (factor < 0) { 0 } else { factor } })

  }
}
