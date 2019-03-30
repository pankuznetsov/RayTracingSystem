package lightweight.geometry

case class Sphere(center: Vector3D, radius: Float) {

  def getSphereAndSphereIntersection(otherSphere: Sphere): Boolean = {
    (center - otherSphere.center).magnitude < radius + otherSphere.radius
  }

  def getRayAndSphereIntersection(ray: Ray): Boolean = {
    (center - (ray.origin + ray.direction * (ray.origin - center).dotProduct(ray.direction))).magnitude < radius
  }

  def getTriangleBoundingSphere(triangle: Triangle): Sphere = {
    val sphereCenter = (triangle.a + triangle.b + triangle.c) / 3
    val distensyA = (triangle.a - sphereCenter).magnitude
    val distensyB = (triangle.b - sphereCenter).magnitude
    val distensyC = (triangle.c - sphereCenter).magnitude
    val farVertix =
      if (distensyA > distensyB && distensyA > distensyC) {
        distensyA
      } else {
        if (distensyB > distensyA && distensyB > distensyC) {
          distensyB
        } else {
          distensyC
        }
      }
    Sphere(sphereCenter, farVertix)
  }
}
