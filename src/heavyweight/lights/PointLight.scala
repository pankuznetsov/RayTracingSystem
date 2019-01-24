package heavyweight.lights

import lightweight.Lamp
import lightweight.geometry._
import lightweight.nodes.LampOutput

case class PointLight(override val location: Vector3D, override val size: Double, override val maxDistance: Double, override val output: LampOutput, override val directlyVisible: Boolean, override val samples: Int) extends Lamp(location, size, maxDistance, output, directlyVisible, samples) {

  /* Tuple: Vector3D for projection location,
  first Double for distance from ray origin to hit point
  and second Double for distance from lamp to the projection.
   */
  def projectToLamp(ray: Ray): (Vector3D, Double, Double) = {
    val originToLamp = location - ray.origin
    val originToLampMagnitude = originToLamp.magnitude
    val projection = ray.origin + ray.direction * originToLampMagnitude
    (projection, originToLampMagnitude, (projection - location).magnitude)
  }

  override def isCollide(mesh: Mesh, triangleIndex: Int, ray: Ray): (Vector3D, Double, Double) = {
    val projection = projectToLamp(ray)
    if (projection._2 < maxDistance && projection._3 < size) projection else null
  }

  // override def throwRay(triangle: Triangle, position: Vector3D): Ray = Ray(position, (location - position).normalized)

  override def throwRay(triangle: Triangle, position: Vector3D): Ray = Ray(position,
    (location - position).normalized + RayDistributor.getRandomVector3D() * (size / (location - position).magnitude))
}
