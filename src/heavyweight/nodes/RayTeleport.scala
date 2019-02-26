package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Triangle, Vector3D}
import lightweight.nodes.{Color, Container, LampIlluminationOutput, Node}

case class RayTeleport(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int): Unit = {
    if (mesh.binder.contains(triangleIndex)) {
      val bind: Option[Int] = mesh.binder.get(triangleIndex)
      if (bind.nonEmpty) {
        val barycentric: (Double, Double, Double) = mesh.mesh(triangleIndex).getBarycentric(hitPoint)
        val out: Triangle = mesh.mesh(bind.get)
        val outCartesian = out.barycentricToCartesian3D(barycentric)
        val teleportedRay = Ray(outCartesian, ray.direction)
        val rayColor = teleportedRay.renderSample(mesh, world, bind.get, shadersLeft)
        outputs(0).content = rayColor._2
        return
      }
    }
    outputs(0).content = Color(1.0f, 0.0f, 1.0f)
  }
}
