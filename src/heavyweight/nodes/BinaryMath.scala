package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class BinaryMath(override val inputs: Array[Container], override val outputs: Array[Container], optionIndex: Int) extends Node(inputs, outputs) {
  /*
    from code:
    0). Option index (
        0 - Addition
        1 - Substraction
        2 - Multiplication
        3 - Division
        4 - Maximal
        5 - Minimal)
    From container:
    0). FirstOperand
    1). SecondOperand */
    override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
      if (optionIndex > 5 || optionIndex < 0) null
      if (optionIndex == 0) {
        outputs(0).content = lightweight.nodes.Numeric(inputs(0).content.asInstanceOf[lightweight.nodes.Numeric].value + inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value)
      }
      if (optionIndex == 1) {
        outputs(0).content = lightweight.nodes.Numeric(inputs(0).content.asInstanceOf[lightweight.nodes.Numeric].value - inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value)
      }
      if (optionIndex == 2) {
        outputs(0).content = lightweight.nodes.Numeric(inputs(0).content.asInstanceOf[lightweight.nodes.Numeric].value * inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value)
      }
      if (optionIndex == 3) {
        outputs(0).content = lightweight.nodes.Numeric(inputs(0).content.asInstanceOf[lightweight.nodes.Numeric].value / inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value)
      }
      if (optionIndex == 4) {
        if (inputs(0).content.asInstanceOf[lightweight.nodes.Numeric].value > inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value){
          outputs(0).content = lightweight.nodes.Numeric(inputs(0).content.asInstanceOf[lightweight.nodes.Numeric].value)
        } else {
          outputs(0).content = lightweight.nodes.Numeric(inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value)
        }
      }
      if (optionIndex == 5) {
        if (inputs(0).asInstanceOf[lightweight.nodes.Numeric].value < inputs(1).asInstanceOf[lightweight.nodes.Numeric].value){
          outputs(0).content = lightweight.nodes.Numeric(inputs(0).asInstanceOf[lightweight.nodes.Numeric].value)
        } else {
          outputs(0).content = lightweight.nodes.Numeric(inputs(1).asInstanceOf[lightweight.nodes.Numeric].value)
        }
      }
    }
}
