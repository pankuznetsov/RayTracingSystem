package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D, RayDistributor}
import lightweight.nodes.{Color, Container, Node}

case class VolumeScatter(override val inputs: Array[Container], override val outputs: Array[Container], rayQuantity: Int) extends Node(inputs, outputs) {

  /*
    1. Color
    2. Density
    3. Anisotropic
  */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
    val volumeColor: Color = inputs(0).content.asInstanceOf[Color]
    val volumeDensity: Double = inputs(1).content.asInstanceOf[lightweight.nodes.Numeric].value
    val volumeAnisotropic: Double = inputs(2).content.asInstanceOf[lightweight.nodes.Numeric].value
    val volumeNormal: Vector3D = inputs(3).content.asInstanceOf[Vector3D]
    var color = Color(0, 0, 0)
    for (i: Int <- 0 until rayQuantity) {
      val outRay: Ray = Ray(coordinates, lightweight.geometry.RayDistributor.newRandomVector3D() + ray.direction / (1 - volumeAnisotropic) + volumeNormal * volumeAnisotropic)
      val renderSampleResult = outRay.renderSample(mesh: Mesh, world: World, -1, shadersLeft)._2
      if (renderSampleResult != null)
        color += renderSampleResult
    }
    color = color / rayQuantity
    val result = color * volumeColor + backColor / Math.max(1, volumeDensity)
    outputs(0).content = result.asInstanceOf[Color]
  }
}
