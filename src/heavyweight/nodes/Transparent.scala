package heavyweight.nodes

import lightweight.{Functions, World}
import lightweight.geometry.{Mesh, Ray, RayDistributor, Vector3D}
import lightweight.nodes.{Color, Container, LampIlluminationOutput, Node, Numeric}

case class Transparent(override val inputs: Array[Container], override val outputs: Array[Container]) extends ScatteringSurface(inputs, outputs) {

  override def scatter(mesh: Mesh, world: World, triangleIndex: Int,
                       ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, shadersLeft: Int,
                       color: Color, roughness: Double, rays: Int,
                       normalMap: Vector3D, flatNormal: Vector3D,
                       integral: LampIlluminationOutput): Array[Ray] = RayDistributor.getRandomRays(
      ray.direction.normalized,
      ray.direction.normalized,
      rays,
      roughness).map(x => Ray(hitPoint, x))
}
