package heavyweight.nodes

import lightweight.{Functions, World}
import lightweight.geometry.{Mesh, Ray, RayDistributor, Vector3D}
import lightweight.nodes.{Color, Container, LampIlluminationOutput, Node, Numeric}

case class Diffuse(override val inputs: Array[Container], override val outputs: Array[Container]) extends ScatteringSurface(inputs, outputs) {

  override def scatter(mesh: Mesh, world: World, triangleIndex: Int,
                       ray: Ray, hitPoint: Vector3D, shadersLeft: Int,
                       color: Color, roughness: Double, rays: Int,
                       normalMap: Vector3D, flatNormal: Vector3D,
                       integral: LampIlluminationOutput): Array[Ray] = RayDistributor.getRandomRays(
    flatNormal,
    (flatNormal + normalMap).normalized,
    rays.asInstanceOf[Int],
    roughness).map(x => Ray(hitPoint, x))

  /* The Diffuse shader's inputs are:
    1. Color
    2. Roughness
    3. Number of rays scattered
    4. Normal vector
    5. Lamp Illumination
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    val color = Functions.toColor(inputs(0).content)
    val roughness: Numeric = inputs(1).content.asInstanceOf[Numeric]
    val rays: Numeric = inputs(2).content.asInstanceOf[Numeric]
    var normalMap: Vector3D = if (inputs(3) != null) Functions.toVector(inputs(3).content) else null  // Take normal
    val integral: LampIlluminationOutput = inputs(4).content.asInstanceOf[LampIlluminationOutput]
    val triangleNormal: Vector3D = Functions.getNormal(mesh.mesh(triangleIndex), ray)
    if (normalMap == null) normalMap = triangleNormal
    var scattering: Array[Vector3D] = RayDistributor.getRandomRays(
      triangleNormal,
      (triangleNormal + normalMap).normalized,
      rays.value.asInstanceOf[Int],
      if (roughness == null) 0 else roughness.value
    )
    var red: Float = 0f
    var green: Float = 0f
    var blue: Float = 0f
    for (scatterdRay <- scattering) {
      val rayColor = Ray(hitPoint, scatterdRay).renderSample(mesh, world, triangleIndex, shadersLeft)
      if (rayColor._2 != null) {
        red += (rayColor._2.red / scattering.length)
        green += (rayColor._2.green / scattering.length)
        blue += (rayColor._2.blue / scattering.length)
      }
    }
    val finalColor = (Color(red, green, blue) * scattering.length + integral.color * integral.lamps) / (scattering.length + integral.lamps)
    outputs(0).content = if (inputs(0).content != null) finalColor * color else finalColor
  }
  */
}
