package lightweight.geometry

case class Plane(origin: Vector3D, normal: Vector3D) {

  @inline def intersectionTime(ray: Ray): Double = {
    val temporary = ray.direction dotProduct normal
    if (temporary != 0) ((origin - ray.origin) dotProduct normal) / temporary else 0
  }

  def intersectionWithSphere(sphere: Sphere): Boolean = Math.abs((origin - sphere.center) dotProduct normal) < sphere.radius

  override def toString = s"[origin: $origin, normal: $normal]"
}
