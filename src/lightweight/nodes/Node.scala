package lightweight.nodes

import lightweight.{Lamp, RayOriginInfo, World}
import lightweight.geometry.{Mesh, Ray, Triangle, Vector3D}

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

abstract class Node(val inputs: Array[Container], val outputs: Array[Container], var complite: Boolean = false) {

  def newFrame: Unit = {
    if (inputs != null) {
      complite = false
      for (field <- inputs; if field != null && field.parentNode != null)
        field.parentNode.newFrame
    }
  }

  def run(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    if (shadersLeft >= 0 && !complite) {
      for (field <- inputs)
        if (field != null && field.parentNode != null)
          field.parentNode.run(mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft, rayOriginInfo)
      doThings(mesh, world, triangleIndex, ray, hitPoint, coordinates, backColor, shadersLeft, rayOriginInfo)
      complite = true
    }
  }

  def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit

  def throwToLights(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, normal: Vector3D, shadersLeft: Int): (Int, Color) = {
    var integral: Color = Color(0, 0, 0)
    var lights: Int = 0
    val spawn = if (hitPoint != null) hitPoint else coordinates
    val triangle = if (triangleIndex >= 0) mesh.mesh(triangleIndex) else null
    for (lamp <- mesh.lamps) {
      var sampleIntegral: Color  = Color(0, 0, 0)
      for (i <- 0 until lamp.samples) {
        val toLight: Ray = lamp.throwRay(triangle, spawn)
        if (coordinates != null || toLight.direction.sameDirection(normal)) {
          val collision = lamp.isCollide(mesh, triangleIndex, toLight)
          if (collision != null) {
            val renderSample = toLight.renderSample(mesh, world, triangleIndex, shadersLeft, RayOriginInfo(this, false))
            if  (renderSample._2 != null) sampleIntegral += renderSample._2 / (collision._2 * collision._2 * lamp.samples)
          } else {
            val r = toLight.renderSample(mesh, world, triangleIndex, shadersLeft, RayOriginInfo(this, false))._2
            if (r != null)
              sampleIntegral += r / lamp.samples
          }
        }
      }
      lights += 1
      integral += sampleIntegral
    }
    (lights, integral)
  }
}
