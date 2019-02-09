package heavyweight.nodes

import lightweight.{Functions, World}
import lightweight.geometry.{Mesh, Ray, RayDistributor, Vector3D}
import lightweight.nodes.{Color, Container, LampIlluminationOutput, Node, Numeric}

case class Translucent(override val inputs: Array[Container], override val outputs: Array[Container]) extends ScatteringSurface(inputs, outputs) {

  override def scatter(mesh: Mesh, world: World, triangleIndex: Int,
                       ray: Ray, hitPoint: Vector3D, shadersLeft: Int,
                       color: Color, roughness: Double, rays: Int,
                       normalMap: Vector3D, flatNormal: Vector3D,
                       integral: LampIlluminationOutput): Array[Ray] = RayDistributor.getRandomRays(
    flatNormal.invert(),
    (flatNormal + normalMap).normalized.invert(),
    rays.asInstanceOf[Int],
    roughness).map(x => Ray(hitPoint, x))
}
