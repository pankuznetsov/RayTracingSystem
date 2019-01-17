package lightweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}

abstract class Node(val inputs: Array[Container], val outputs: Array[Container]) {

  def run(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    for (field <- inputs)
      if (field != null && field.parentNode != null)
        field.parentNode.run(mesh: Mesh, world, triangleIndex, ray, hitPoint, shadersLeft)
    doThings(mesh, world, triangleIndex, ray, hitPoint, shadersLeft)
  }

  def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit
}
