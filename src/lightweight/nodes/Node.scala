package lightweight.nodes

import lightweight.{Lamp, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}

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

  def throwToLights(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): (Int, Color) = {
    var red: Float = 0f
    var green: Float = 0f
    var blue: Float = 0f
    var integral: Color = Color(0, 0, 0)
    var lights: Int = 0
    for (lamp <- mesh.lamps) {
      var sampleIntegral: Color  = Color(0, 0, 0)
      for (i <- 0 until lamp.samples) {
        val result = lamp.throwRay(mesh.mesh(triangleIndex), hitPoint)
        val collision = lamp.isCollide(mesh, triangleIndex, result)
        if (collision != null)
          sampleIntegral += result.renderSample(mesh, world, triangleIndex, shadersLeft)._2 / collision._2 / lamp.samples
        else
          sampleIntegral += result.renderSample(mesh, world, triangleIndex, shadersLeft)._2 / lamp.samples
      }
      lights += 1
      integral += sampleIntegral
    }
    (lights, integral)
  }
}
