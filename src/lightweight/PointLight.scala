package lightweight

import lightweight.geometry.{Ray, Vector3D}
import lightweight.nodes.LampOutput

case class PointLight(location: Vector3D, size: Double, maxDistance: Double, output: LampOutput, directlyVisible: Boolean) extends Lamp(location, size, maxDistance, output, directlyVisible) {

  /* Tuple: Vector3D for projection location,
  first Double for distance from ray origin to hit point
  and second Double for distance from lamp to the projection.
   */
  private def projectToLamp(ray: Ray): (Vector3D, Double, Double) = {
    val originToLamp = location - ray.origin
    val originToLampMagnitude = originToLamp.magnitude
    val projection = ray.origin + ray.direction * originToLampMagnitude
    (projection, originToLampMagnitude, (projection - location).magnitude)
  }

  override def isCollide(ray: Ray): (Vector3D, Double, Double) = {
    val hit = projectToLamp(ray)
    if (hit._3 < size && hit._2 < maxDistance) hit else null
  }
}
