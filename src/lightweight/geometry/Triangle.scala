package lightweight.geometry

import java.lang

import lightweight.nodes.{SurfaceOutput, VolumeOutput}

import scala.collection.immutable.HashMap

case class Triangle(a: Vector3D, b: Vector3D, c: Vector3D, dualfacing: Boolean, surface: SurfaceOutput, volume: VolumeOutput, uwCoordinates: HashMap[UVMap, UVCoordinates]) {

  val supportingPlane: Plane = Plane(a, getNormal())

  def move(displacement: Vector3D) = new Triangle(a + displacement, b + displacement, c + displacement, dualfacing, surface, volume, uwCoordinates)

  def scale(zoom: Double) = new Triangle(a * zoom, b * zoom, c * zoom, dualfacing, surface, volume, uwCoordinates)

  def scale(zoom: Vector3D) = new Triangle(a * zoom, b * zoom, c * zoom, dualfacing, surface, volume, uwCoordinates)

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
    if (time <= Constants.EPSILON) return null
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
    val lambdaZero = ((b.y - c.y) * (point.x - c.x) + (c.x - b.x) * (point.y - c.y)) / ((b.y - c.y) * (a.x - c.x) + (c.x - b.x) * (a.y - c.y))
    val lambdaOne = ((c.y - a.y) * (point.x - c.x) + (a.x - c.x) * (point.y - c.y)) / ((b.y - c.y) * (a.x - c.x) + (c.x - b.x) * (a.y - c.y))
    val lambdaTwo = 1 - lambdaZero - lambdaOne
    (lambdaZero, lambdaOne, lambdaTwo)
  }

  override def toString = s"[a: \t$a, b: \t$b, c: \t$c] -> \t${supportingPlane.normal}"
}
