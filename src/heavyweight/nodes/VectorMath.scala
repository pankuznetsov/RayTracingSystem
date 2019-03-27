package heavyweight.nodes

import lightweight.{RayOriginInfo, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class VectorMath(override val inputs: Array[Container], override val outputs: Array[Container], optionIndex: Int) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    if (optionIndex < 9 || optionIndex > 0) null
    if (optionIndex == 0){
      inputs(0).content.asInstanceOf[Vector3D] + inputs(1).content.asInstanceOf[Vector3D]
    }
    if (optionIndex == 1){
      inputs(0).content.asInstanceOf[Vector3D] - inputs(1).content.asInstanceOf[Vector3D]
    }
    if (optionIndex == 2){
      inputs(0).content.asInstanceOf[Vector3D].magnitude.asInstanceOf[lightweight.nodes.Numeric].value
    }
    if (optionIndex == 3){
      inputs(0).content.asInstanceOf[Vector3D].normalized
    }
    if (optionIndex == 4){
      inputs(0).content.asInstanceOf[Vector3D].invert()
    }
    if (optionIndex == 5){
      (inputs(0).content.asInstanceOf[Vector3D] dotProduct inputs(1).content.asInstanceOf[Vector3D]).asInstanceOf[lightweight.nodes.Numeric].value
    }
    if (optionIndex == 6){
      (inputs(0).content.asInstanceOf[Vector3D] crossProduct inputs(1).content.asInstanceOf[Vector3D]).asInstanceOf[lightweight.nodes.Numeric].value
    }
    if (optionIndex == 7){
      inputs(0).content.asInstanceOf[Vector3D] * inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value
    }
    if (optionIndex == 8){
      inputs(0).content.asInstanceOf[Vector3D] / inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value
    }
  }
}
