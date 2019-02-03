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
    val v = ray.origin - location
    val b = (ray.direction dotProduct v) * 2
    val c = (v dotProduct v) - (size * size)
    val d = (b * b) - (c * 4)
    if (d > Constants.EPSILON) {
      val x1 = (-b - Math.sqrt(d)) / 2
      val x2 = (-b + Math.sqrt(d)) / 2
      var t = if (x1 >= 0 && x2 >= 0) x1 else x2
      val projection = ray.origin + ray.direction * t
      return (projection, t, (location - projection).magnitude)
    } else return null
  }

  override def isCollide(mesh: Mesh, triangleIndex: Int, ray: Ray): (Vector3D, Double, Double) = {
    val projection = projectToLamp(ray)
    if (projection != null && projection._2 <= maxDistance && projection._3 <= size) projection else null
  }

  override def throwRay(triangle: Triangle, position: Vector3D): Ray = Ray(position,
    (location - position).normalized + RayDistributor.newRandomVector3D() * Math.random() * (size / (location - position).magnitude))
}