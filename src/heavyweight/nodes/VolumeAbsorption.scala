package heavyweight.nodes

import lightweight.{Functions, RayOriginInfo, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, ColorWithDensity, Container, Node}

case class VolumeAbsorption(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  /*
    1. Color
    2. Density
  */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    val color = Functions.toColor(inputs(0).content)
    val density: Float = Functions.toNumeric(inputs(1).content).value
    if (backColor == null) {
      println("Fail Back Color")
    }
    if (color != Color(0, 0, 0) || density != 0) {
      val resultColor: Color =
        backColor -
          (color *
            density *
            lightweight.geometry.Constants.VOLUME_STEP_SIZE)
      val result: Color = Color(Math.max(resultColor.red, 0), Math.max(resultColor.green, 0), Math.max(resultColor.blue, 0))
      outputs(0).content = result
    } else {
      outputs(0).content = if (backColor == null) Color(0, 0, 0) else backColor
    }
  }
}

