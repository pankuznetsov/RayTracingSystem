package heavyweight.nodes

import lightweight.{Functions, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, ColorWithDensity, Container, Node}

case class VolumeEmission(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  /*
    1. Color
    2. Intensity
    3. Density
  */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, shadersLeft: Int): Unit = {
    val color = Functions.toColor(inputs(0).content)
    val intensity: Float = Functions.toNumeric(inputs(1).content).value.asInstanceOf[Float]
    val density: Float = Functions.toNumeric(inputs(1).content).value.asInstanceOf[Float]
    outputs(0).content = ColorWithDensity(color.red * intensity, color.green * intensity, color.blue * intensity, density)
  }
}
