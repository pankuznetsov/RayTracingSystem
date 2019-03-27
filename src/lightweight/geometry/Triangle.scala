package lightweight.geometry

import java.lang

import lightweight.nodes.{SurfaceOutput, VolumeOutput}

import scala.collection.immutable.HashMap

case class Triangle(a: Vector3D, b: Vector3D, c: Vector3D, dualfacing: Boolean, surface: SurfaceOutput, volume: VolumeOutput, uwCoordinates: HashMap[UVMap, UVCoordinates]) {

  val supportingPlane: Plane = Plane(a, getNormal)

  def move(displacement: Vector3D) = Triangle(a + displacement, b + displacement, c + displacement, dualfacing, surface, volume, uwCoordinates)

  def scale(zoom: Float) = Triangle(a * zoom, b * zoom, c * zoom, dualfacing, surface, volume, uwCoordinates)

  def scale(zoom: Vector3D) = Triangle(a * zoom, b * zoom, c * zoom, dualfacing, surface, volume, uwCoordinates)

  @inline def getNormal = ((b - a) crossProduct (c - a)).normalized

  @inline def intersectionTime(ray: Ray): Double = {
    val temporary = ray.direction dotProduct supportingPlane.normal.normalized
    if (temporary != 0) ((supportingPlane.origin - ray.origin) dotProduct supportingPlane.normal) / temporary else
    if (temporary != 0)
      ((supportingPlane.origin - ray.origin) dotProduct supportingPlane.normal) / temporary
    else
      Double.MaxValue
  }

  @inline def backQuadrant(ray: Ray): Boolean = {
    var back: Boolean = false
    var i: Int = 0
    while (i < 3) { back = back && (getVertex(i) - ray.origin).backQuadrant(ray.direction); i += 1; }
    return back
  }

  @inline def otherQuadrant(ray: Ray): Boolean = {
    val first = a.getQuadrant(ray.direction)
    val second = b.getQuadrant(ray.direction)
    if (first != second) return false
    val third = c.getQuadrant(ray.direction)
    if (first != third) return false
    return true
  }

  @inline def intersectionWithRay(ray: Ray): (Vector3D, Float) = {
    val temporary = ray.direction dotProduct supportingPlane.normal
    var time: Float = 0
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

  @inline def getVertex(number: Int): Vector3D = {
    number match {
      case 0 => a
      case 1 => b
      case 2 => c
      case _ => null
    }
  }

  def barycentricToCartesian3D(uvw: (Float, Float, Float)): Vector3D =
    Vector3D(uvw._1 * a.x + uvw._2 * b.x + uvw._3 * a.x, uvw._1 * a.y + uvw._2 * b.y + uvw._3 * c.y, uvw._1 * a.z + uvw._2 * b.z + uvw._3 * c.z)

  def getPerimeter: Double = (b - a).magnitude + (c - b).magnitude + (a - c).magnitude

  def getArea: Double = {
    val ab = (b - a).magnitude
    val bc = (c - b).magnitude
    val ca = (a - c).magnitude
    val semiPerimeter = (ab + bc + ca) / 2
    Math.sqrt(semiPerimeter * (semiPerimeter - ab) * (semiPerimeter - bc) * (semiPerimeter - ca))
  }

  def getBarycentric(point: Vector3D): (Float, Float, Float) = {
    val lambdaZero = ((b.y - c.y) * (point.x - c.x) + (c.x - b.x) * (point.y - c.y)) / ((b.y - c.y) * (a.x - c.x) + (c.x - b.x) * (a.y - c.y))
    val lambdaOne = ((c.y - a.y) * (point.x - c.x) + (a.x - c.x) * (point.y - c.y)) / ((b.y - c.y) * (a.x - c.x) + (c.x - b.x) * (a.y - c.y))
    val lambdaTwo = 1 - lambdaZero - lambdaOne
    (lambdaZero, lambdaOne, lambdaTwo)
  }

  override def toString = s"[a: \t$a, b: \t$b, c: \t$c] -> \t${supportingPlane.normal}"
}
