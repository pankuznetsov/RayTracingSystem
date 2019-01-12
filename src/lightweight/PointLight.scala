package lightweight

import lightweight.geometry.{Ray, Vector3D}
import lightweight.nodes.LampOutput

class PointLight(location: Vector3D, size: Double, maxDistance: Double, output: LampOutput, directlyVisible: Boolean)
  extends Lamp(location, size, output, directlyVisible) {

  /* Tuple: Vector3D for projection location,
  first Double for distance from ray origin to lamp
  and second Double for distance from lamp to the projection.
   */
  def projectToLamp(ray: Ray): (Vector3D, Double, Double) = {
    val originToLamp = location - ray.origin
    val originToLampMagnitude = originToLamp.magnitude
    val projection = ray.origin + ray.direction * originToLampMagnitude
    (projection, originToLampMagnitude, (projection - location).magnitude)
  }

  override def isCollide(ray: Ray): (Vector3D, Double, Double) = projectToLamp(ray)
}
