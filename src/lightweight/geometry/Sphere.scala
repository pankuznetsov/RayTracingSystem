package lightweight.geometry

case class Sphere(center: Vector3D, radius: Float) {

  def getSphereAndSphereIntersection(otherSphere: Sphere): Boolean = {
    (center - otherSphere.center).magnitude < radius + otherSphere.radius
  }

  def getRayAndSphereIntersection(ray: Ray): Boolean = {
    (center - (ray.origin + ray.direction * (ray.origin - center).dotProduct(ray.direction))).magnitude < radius
  }

  override def toString: String = s"[center: $center, radius: $radius]"
}
