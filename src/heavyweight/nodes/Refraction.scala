package heavyweight.nodes

import lightweight.{Functions, World}
import lightweight.geometry._
import lightweight.nodes.{Color, Container, LampIlluminationOutput, Numeric}

case class Refraction(override val inputs: Array[Container], override val outputs: Array[Container]) extends ScatteringSurface(inputs, outputs) {

  override def scatter(mesh: Mesh, world: World, triangleIndex: Int,
                       ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, shadersLeft: Int,
                       color: Color, roughness: Double, rays: Int,
                       normalMap: Vector3D, flatNormal: Vector3D,
                       integral: LampIlluminationOutput): Array[Ray] = {
    val thisIor = inputs(5).content.asInstanceOf[Numeric].value
    val worldIor = world.worldIOR
    RayDistributor.getRandomRays(
      flatNormal,
      if (ray.direction.sameDirection(mesh.mesh(triangleIndex).supportingPlane.normal)) {
        val criticalAngle = Math.asin(worldIor / thisIor) * Constants.ONE_DIVIDE_ON_RADIANS_TURNOVER
        val angle = Math.acos(ray.direction.invert().dotProduct(mesh.mesh(triangleIndex).supportingPlane.normal)) * Constants.ONE_DIVIDE_ON_RADIANS_TURNOVER
        if (angle > criticalAngle) {
          ray.direction.refract(mesh.mesh(triangleIndex).supportingPlane.normal, thisIor, worldIor)
        } else {
          ray.direction.reflect(Plane(Vector3D(0, 0, 0), (flatNormal + normalMap).normalized)).invert()
        }
      } else {
        val criticalAngle = Math.asin(thisIor / worldIor) * Constants.ONE_DIVIDE_ON_RADIANS_TURNOVER
        val angle = Math.acos(ray.direction.dotProduct(mesh.mesh(triangleIndex).supportingPlane.normal)) * Constants.ONE_DIVIDE_ON_RADIANS_TURNOVER
        if (angle > criticalAngle) {
          ray.direction.refract(mesh.mesh(triangleIndex).supportingPlane.normal, worldIor, thisIor)
        } else {
          ray.direction.reflect(Plane(Vector3D(0, 0, 0), (flatNormal + normalMap).normalized)).invert()
        }
      },
      rays.asInstanceOf[Int],
      roughness).map(x => Ray(hitPoint, x))
  }
}
