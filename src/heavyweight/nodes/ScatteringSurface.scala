package heavyweight.nodes

import lightweight.{Functions, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, LampIlluminationOutput, Node, Numeric}

/* The Scattering Surface shader's group inputs are:
    1. Color
    2. Roughness
    3. Number of rays scattered
    4. Normal vector
    5. Lamp Illumination
   */
abstract class ScatteringSurface(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  def scatter(mesh: Mesh, world: World, triangleIndex: Int,
                       ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, shadersLeft: Int,
                       color: Color, roughness: Double, rays: Int,
                       normalMap: Vector3D, flatNormal: Vector3D,
                       integral: LampIlluminationOutput): Array[Ray]

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, shadersLeft: Int): Unit = {
    val color = Functions.toColor(inputs(0).content)
    val roughness: Double = inputs(1).content.asInstanceOf[Numeric].value
    val rays: Int = inputs(2).content.asInstanceOf[Numeric].value.asInstanceOf[Int]
    var normalMap: Vector3D = if (inputs(3) != null) Functions.toVector(inputs(3).content) else null  // Take normal
    val integral: LampIlluminationOutput = if (inputs(4) != null) inputs(4).content.asInstanceOf[LampIlluminationOutput] else LampIlluminationOutput(Color(0, 0, 0), 0)
    val triangleNormal: Vector3D = Functions.getNormal(mesh.mesh(triangleIndex), ray)
    if (normalMap == null) normalMap = triangleNormal
    val scatteredRays: Array[Ray] = scatter(mesh, world, triangleIndex,
      ray, hitPoint, coordinates, shadersLeft,
      color, roughness: Double,
      rays, normalMap, triangleNormal, integral)
    var red: Float = 0f
    var green: Float = 0f
    var blue: Float = 0f
    for (scatterdRay <- scatteredRays) {
      val rayColor = scatterdRay.renderSample(mesh, world, triangleIndex, shadersLeft)
      if (rayColor._2 != null) {
        red += (rayColor._2.red / scatteredRays.length)
        green += (rayColor._2.green / scatteredRays.length)
        blue += (rayColor._2.blue / scatteredRays.length)
      }
    }
    val finalColor = (Color(red, green, blue) * scatteredRays.length + integral.color * integral.lamps) / (scatteredRays.length + integral.lamps)
    outputs(0).content = if (inputs(0).content != null) finalColor * color else finalColor
  }
}
