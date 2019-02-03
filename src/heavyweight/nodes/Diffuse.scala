package heavyweight.nodes

import lightweight.{Functions, World}
import lightweight.geometry.{Mesh, Ray, RayDistributor, Vector3D}
import lightweight.nodes.{Color, Container, Node, Numeric}

case class Diffuse(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  /* The Diffuse shader's inputs are:
    1. Color
    2. Roughness
    3. Number of rays scattered
    4. Normal vector
   */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    /*
    val color = inputs(0).content.asInstanceOf[Color]
    val roughness: Numeric = inputs(1).content.asInstanceOf[Numeric]
    val rays: Numeric = inputs(2).content.asInstanceOf[Numeric]
    var normal: Vector3D = if (inputs(3) != null) Functions.toVector(inputs(3).content) else null // Take normal
    if (normal == null) normal = mesh.mesh(triangleIndex).supportingPlane.normal //Set normal to triangles normal in case that normal input is null
    if (!ray.direction.sameDirection(mesh.mesh(triangleIndex).supportingPlane.normal)) normal = normal.invert()
    var scattering: Array[Vector3D] = null
    var red: Float = 0f
    var green: Float = 0f
    var blue: Float = 0f
    scattering = RayDistributor.getRandomRays(normal,
      normal, rays.value.asInstanceOf[Int],
      if (roughness == null) 0 else roughness.value)
    var lights: Int = 0
    for (i <- scattering) {
      val rayColor = Ray(hitPoint, i).renderSample(mesh, world, triangleIndex, shadersLeft)
      if (rayColor != null && rayColor._2 != null) {
        lights += 1
        red = red + (rayColor._2.red / scattering.length)
        green = green + (rayColor._2.green / scattering.length)
        blue = blue + (rayColor._2.blue / scattering.length)
      }
    }
    // println(s"normal: ${normal}")
    val integral = throwToLights(mesh, world, triangleIndex, ray, hitPoint, mesh.mesh(triangleIndex).supportingPlane.normal, shadersLeft)
    val finalColor = (Color(red, green, blue) * scattering.length + integral._2 * integral._1) / (scattering.length + integral._1)
    outputs(0).content = if (inputs(0).content != null) finalColor * color else finalColor
    */
    val color = inputs(0).content.asInstanceOf[Color]
    val roughness: Numeric = inputs(1).content.asInstanceOf[Numeric]
    val rays: Numeric = inputs(2).content.asInstanceOf[Numeric]
    var normalMap: Vector3D = if (inputs(3) != null) Functions.toVector(inputs(3).content) else null  // Take normal
    val triangleNormal: Vector3D = if (mesh.mesh(triangleIndex).supportingPlane.normal.sameDirection(ray.direction))
      mesh.mesh(triangleIndex).supportingPlane.normal.invert() else mesh.mesh(triangleIndex).supportingPlane.normal
    if (normalMap == null) normalMap = triangleNormal
    var scattering: Array[Vector3D] = RayDistributor.getRandomRays(
      triangleNormal,
      normalMap,
      rays.value.asInstanceOf[Int],
      roughness.value
    )
    var red: Float = 0f
    var green: Float = 0f
    var blue: Float = 0f
    for (scatterdRay <- scattering) {
      val color = Ray(hitPoint, scatterdRay).renderSample(mesh, world, triangleIndex, shadersLeft)
      if (color._2 != null) {
        red += (color._2.red / scattering.length)
        green += (color._2.green / scattering.length)
        blue += (color._2.blue / scattering.length)
      }
    }
    val integral = throwToLights(mesh, world, triangleIndex, ray, hitPoint, normalMap, shadersLeft)
    val finalColor = (Color(red, green, blue) * scattering.length + integral._2 * integral._1) / (scattering.length + integral._1)
    outputs(0).content = if (inputs(0).content != null) finalColor * color else finalColor
  }
}
