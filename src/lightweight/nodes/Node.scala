package lightweight.nodes

import lightweight.{Lamp, World}
import lightweight.geometry.{Mesh, Ray, Triangle, Vector3D}

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

abstract class Node(val inputs: Array[Container], val outputs: Array[Container]) {

  def run(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    if (shadersLeft >= 0) {
      for (field <- inputs)
        if (field != null && field.parentNode != null)
          field.parentNode.run(mesh: Mesh, world, triangleIndex, ray, hitPoint, shadersLeft)
      doThings(mesh, world, triangleIndex, ray, hitPoint, shadersLeft)
    }
  }

  def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit

  def throwToLights(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, normal: Vector3D, shadersLeft: Int): (Int, Color) = {
    var red: Float = 0f
    var green: Float = 0f
    var blue: Float = 0f
    var integral: Color = Color(0, 0, 0)
    var lights: Int = 0
    for (lamp <- mesh.lamps) {
      var sampleIntegral: Color  = Color(0, 0, 0)
      for (i <- 0 until lamp.samples) {
        val toLight: Ray = lamp.throwRay(mesh.mesh(triangleIndex), hitPoint)
        if (toLight.direction.sameDirection(normal)) {
          val collision = lamp.isCollide(mesh, triangleIndex, toLight)
          if (collision != null) {
            val renderSample = toLight.renderSample(mesh, world, triangleIndex, shadersLeft)
            if  (renderSample._2 != null) sampleIntegral += renderSample._2 / collision._2 / lamp.samples
          } else {
            val r = toLight.renderSample(mesh, world, triangleIndex, shadersLeft)._2
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
