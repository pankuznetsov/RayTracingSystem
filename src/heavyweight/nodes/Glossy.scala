package heavyweight.nodes

import lightweight.{Functions, World}
import lightweight.geometry._
import lightweight.nodes.{Color, Container, Node, Numeric}

case class Glossy(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  /* The Glossy shader's inputs are:
    1. Color
    2. Roughness
    3. Number of rays scattered
    4. Normal vector
   */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    val color = inputs(0).content.asInstanceOf[Color]
    val roughness: Numeric = inputs(1).content.asInstanceOf[Numeric]
    val rays: Numeric = inputs(2).content.asInstanceOf[Numeric]
    var normalMap: Vector3D = if (inputs(3) != null) Functions.toVector(inputs(3).content) else null  // Take normal
    val triangleNormal: Vector3D = if (mesh.mesh(triangleIndex).supportingPlane.normal.sameDirection(ray.direction))
      mesh.mesh(triangleIndex).supportingPlane.normal.invert() else mesh.mesh(triangleIndex).supportingPlane.normal
    if (normalMap == null) normalMap = triangleNormal
    val reflectedRay = Ray(hitPoint, ray.direction.reflect(Plane(Vector3D(0, 0, 0), triangleNormal)).invert())
    var scattering: Array[Vector3D] = null
    var red: Float = 0f
    var green: Float = 0f
    var blue: Float = 0f
    scattering = RayDistributor.getRandomRays(
      triangleNormal,
      reflectedRay.direction,
      rays.value.asInstanceOf[Int],
      if (roughness == null) 0 else roughness.value)
    for (i <- scattering) {
      val rayColor = Ray(hitPoint, i).renderSample(mesh, world, triangleIndex, shadersLeft)
      red = red + (rayColor._2.red / scattering.length)
      green = green + (rayColor._2.green / scattering.length)
      blue = blue + (rayColor._2.blue / scattering.length)
    }
    val finalColor = Color(red, green, blue)
    outputs(0).content = if (inputs(0).content != null) finalColor * color else finalColor
  }
}
