package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Container, Node}

case class VectorMath(override val inputs: Array[Container], override val outputs: Array[Container], val optionIndex: Int) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    if (optionIndex < 9 || optionIndex > 0) null
    if (optionIndex == 0){
      inputs(0).asInstanceOf[Vector3D] + inputs(1).asInstanceOf[Vector3D]
    }
    if (optionIndex == 1){
      inputs(0).asInstanceOf[Vector3D] - inputs(1).asInstanceOf[Vector3D]
    }
    if (optionIndex == 2){
      inputs(0).asInstanceOf[Vector3D].magnitude.asInstanceOf[lightweight.nodes.Numeric].value
    }
    if (optionIndex == 3){
      inputs(0).asInstanceOf[Vector3D].normalized
    }
    if (optionIndex == 4){
      inputs(0).asInstanceOf[Vector3D].invert()
    }
    if (optionIndex == 5){
      (inputs(0).asInstanceOf[Vector3D] dotProduct inputs(1).asInstanceOf[Vector3D]).asInstanceOf[lightweight.nodes.Numeric].value
    }
    if (optionIndex == 6){
      (inputs(0).asInstanceOf[Vector3D] crossProduct inputs(1).asInstanceOf[Vector3D]).asInstanceOf[lightweight.nodes.Numeric].value
    }
    if (optionIndex == 7){
      inputs(0).asInstanceOf[Vector3D] * inputs(1).asInstanceOf[lightweight.nodes.Numeric].value
    }
    if (optionIndex == 8){
      inputs(0).asInstanceOf[Vector3D] / inputs(1).asInstanceOf[lightweight.nodes.Numeric].value
    }
  }
}
