package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class Glossy(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  /* The Glossy shader's inputs are:
    1. Color
    2. Roughness
    3. Number of rays scattered
    4. Normal vector
   */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    val reflectedRay = Ray(hitPoint, ray.direction.reflect(mesh.mesh(triangleIndex).supportingPlane).invert)
    val resultRenderSample = reflectedRay.renderSample(mesh, world, triangleIndex, shadersLeft)
    // println(resultRenderSample._1)
    /*
    if (resultRenderSample._1) {
      println("reflection")
      outputs(0).content = Color(1.0f, 1.0f, 0.0f)
    } else outputs(0).content = Color(0.0f, 1.0f, 1.0f)
    */
    outputs(0).content = if (inputs(0).content != null) resultRenderSample._2 / inputs(0).content.asInstanceOf[Color] else resultRenderSample._2
  }
}
