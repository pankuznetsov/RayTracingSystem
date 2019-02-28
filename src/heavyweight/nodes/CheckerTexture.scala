package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class CheckerTexture(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  /* Checker texture inputs are:
    0. Vector
    1. Scale
    2. First Color
    3. Second Color
   */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
    val supportVector: Vector3D = inputs(0).content.asInstanceOf[Vector3D]
    var blackOrWhite = true
    if ((supportVector.x * inputs(1).content.asInstanceOf[Double]) % 2 == 1) {
      blackOrWhite = !blackOrWhite
    }
    if ((supportVector.y * inputs(1).content.asInstanceOf[Double]) % 2 == 1) {
      blackOrWhite = !blackOrWhite
    }
    if ((supportVector.z * inputs(1).content.asInstanceOf[Double]) % 2 == 1) {
      blackOrWhite = !blackOrWhite
    }
    outputs(0).content = if (blackOrWhite) { lightweight.nodes.Numeric(0) } else { lightweight.nodes.Numeric(1) }
    outputs(1).content = if (blackOrWhite) { inputs(2).content } else { inputs(3).content }
  }
}
