package lightweight.geometry

import java.lang

import lightweight.nodes.{SurfaceOutput, VolumeOutput}

case class Triangle(a: Vector3D, b: Vector3D, c: Vector3D, dualfacing: Boolean, surface: SurfaceOutput, volume: VolumeOutput) {

  val supportingPlane: Plane = Plane(a, getNormal())

  def move(displacement: Vector3D) = new Triangle(a + displacement, b + displacement, c + displacement, dualfacing, surface, volume)

  def scale(zoom: Double) = new Triangle(a * zoom, b * zoom, c * zoom, dualfacing, surface, volume)

  def scale(zoom: Vector3D) = new Triangle(a * zoom, b * zoom, c * zoom, dualfacing, surface, volume)

  def getNormal(): Vector3D = ((b - a) crossProduct (c - a)).normalized

  def intersectionTime(ray: Ray): Double = {
    val temporary = ray.direction dotProduct supportingPlane.normal.normalized
    if (temporary != 0) ((supportingPlane.origin - ray.origin) dotProduct supportingPlane.normal) / temporary else
    if (temporary != 0)
      ((supportingPlane.origin - ray.origin) dotProduct supportingPlane.normal) / temporary
    else
      Double.MaxValue
  }

  def intersectionWithRay(ray: Ray): (Vector3D, Double) = {
    val temporary = ray.direction dotProduct supportingPlane.normal.normalized
    var time: Double = 0
    if (temporary > Constants.EPSILON || (temporary < -Constants.EPSILON && dualfacing))
      time = ((supportingPlane.origin - ray.origin) dotProduct supportingPlane.normal) / temporary
    else
      return null
    val hitPoint = ray.origin + (ray.direction * time)
    val zeroEdge = b - a
    val vZero = hitPoint - a
    var plane = zeroEdge.crossProduct(vZero)
    if (supportingPlane.normal.dotProduct(plane) < Constants.EPSILON)
      return null
    val firstEdge = c - b
    val vFirst = hitPoint - b
    plane = firstEdge.crossProduct(vFirst)
    if (supportingPlane.normal.dotProduct(plane) < Constants.EPSILON)
      return null
    val secondEdge = a - c
    val vSecond = hitPoint - c
    plane = secondEdge.crossProduct(vSecond)
    if (supportingPlane.normal.dotProduct(plane) < Constants.EPSILON)
      return null
    return (hitPoint, time)
  }

  def getVertex(number: Int): Vector3D = {
    number match {
      case 0 => a
      case 1 => b
      case 2 => c
      case _ => null
    }
  }

  def getPerimeter: Double = (b - a).magnitude + (c - b).magnitude + (a - c).magnitude

  def getArea: Double = {
    val ab = (b - a).magnitude
    val bc = (c - b).magnitude
    val ca = (a - c).magnitude
    val semiPerimeter = (ab + bc + ca) / 2
    Math.sqrt(semiPerimeter * (semiPerimeter - ab) * (semiPerimeter - bc) * (semiPerimeter - ca))
  }

  def scatter(scattering: Double): Vector3D = {
    val ab = (b - a).normalized
    val bc = (c - b).normalized
    val ca = (a - c).normalized
    return ((supportingPlane.normal * Math.random()).linearInterpolation(
      (supportingPlane.normal * Math.random() + ab * Math.random() + bc * Math.random() + ca * Math.random()).normalized, scattering)).normalized
  }

  def getBarycentric(point: Vector3D): (Double, Double, Double) = {
    val vZero = b - a
    val vFirst = c - b
    val vSecond = point - a
    val dZeroToZero = vZero dotProduct vZero
    val dZeroToFirst = vZero dotProduct vFirst
    val dFirstToFirst = vFirst dotProduct vFirst
    val dSecondToZero = vSecond dotProduct vZero
    val dSecondToFirst = vSecond dotProduct vFirst
    val denominator = dZeroToZero * dFirstToFirst - dZeroToFirst * dZeroToFirst
    val v = (dFirstToFirst * dSecondToZero - dZeroToFirst * dSecondToFirst) / denominator
    val w = (dZeroToZero * dSecondToFirst - dZeroToFirst * dSecondToZero) / denominator
    val u = 1 - v - w
    (u, v, w)
  }

  override def toString = s"[a: $a, b: $b, c: $c]"
}
