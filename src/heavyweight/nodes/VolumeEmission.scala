package heavyweight.nodes

import lightweight.{Functions, RayOriginInfo, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, ColorWithDensity, Container, Node}

case class VolumeEmission(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  /*
    1. Color
    2. Intensity
  */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    val color = Functions.toColor(inputs(0).content)
    val intensity: Float = Functions.toNumeric(inputs(1).content).value.asInstanceOf[Float]
    if (color != Color(0, 0, 0) || intensity != 0) {
      outputs(0).content = backColor + color * intensity * lightweight.geometry.Constants.VOLUME_STEP_SIZE
    } else {
      outputs(0).content = backColor
    }
  }
}
