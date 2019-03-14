package heavyweight.nodes

import lightweight.{Functions, World}
import lightweight.geometry._
import lightweight.nodes.{Color, Container, LampIlluminationOutput, Node, Numeric}

case class Glossy(override val inputs: Array[Container], override val outputs: Array[Container]) extends ScatteringSurface(inputs, outputs) {

  override def scatter(mesh: Mesh, world: World, triangleIndex: Int,
                       ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, shadersLeft: Int,
                       color: Color, roughness: Float, rays: Int,
                       normalMap: Vector3D, flatNormal: Vector3D,
                       integral: LampIlluminationOutput): Array[Ray] = {
      RayDistributor.getRandomRays(
      flatNormal,
      ray.direction.reflect(Plane(Vector3D(0, 0, 0), (flatNormal + normalMap).normalized)).invert(),
      rays.asInstanceOf[Int],
      roughness).map(x => Ray(hitPoint, x))
  }
}
