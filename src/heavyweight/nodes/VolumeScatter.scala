package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, RayDistributor, Vector3D}
import lightweight.nodes.{Color, Container, LampIlluminationOutput, Node}

case class VolumeScatter(override val inputs: Array[Container], override val outputs: Array[Container], rayQuantity: Int) extends Node(inputs, outputs) {

  /*
    1. Color
    2. Density
    3. Anisotropic
  */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
    val volumeColor: Color = inputs(0).content.asInstanceOf[Color]
    val volumeDensity: Float = inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value
    val volumeAnisotropic: Float = inputs(2).content.asInstanceOf[lightweight.nodes.Numeric].value
    val volumeNormal: Vector3D = if (inputs(3) == null || inputs(3).content == null) {
      Vector3D(0, 0, 0)
    } else {
      inputs(3).content.asInstanceOf[Vector3D]
    }
    val integral: LampIlluminationOutput = if (inputs(4) != null) inputs(4).content.asInstanceOf[LampIlluminationOutput] else LampIlluminationOutput(Color(0, 0, 0), 0)
    var color = Color(0, 0, 0)
    for (i: Int <- 0 until rayQuantity) {
      // val outRay: Ray = Ray(coordinates, lightweight.geometry.RayDistributor.newRandomVector3D() + ray.direction / (1 - volumeAnisotropic) + volumeNormal * volumeAnisotropic)
      val outRay = Ray(coordinates,
        (lightweight.geometry.RayDistributor.newRandomVector3D() + volumeNormal + ray.direction * (volumeAnisotropic / Float.MinPositiveValue * 2)).normalized)
      val renderSampleResult = outRay.renderSample(mesh: Mesh, world: World, -1, shadersLeft)._2
      if (renderSampleResult != null)
        color += renderSampleResult
    }
    color = color / rayQuantity
    // val newDensity = Math.max(1, volumeDensity)
    // val result = (backColor).linearInterpolation(color * volumeColor + integral.color, newDensity)  // + backColor / Math.max(1, volumeDensity)
    val result = (color + integral.color) * volumeColor * Math.min(volumeDensity, 1) + backColor / (1 + volumeDensity)
    outputs(0).content = result.asInstanceOf[Color]
  }
}
