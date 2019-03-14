package lightweight

import lightweight.geometry.{Mesh, Ray, Triangle, Vector3D}
import lightweight.nodes.{Color, LampOutput}

abstract class Lamp(val location: Vector3D, val size: Float, val maxDistance: Float, val output: LampOutput, val directlyVisible: Boolean, val samples: Int) {

  /* If ray hits the lamp the tuple will be returned containing collision point,
  distance to the collision point and multiplier coefficient for force of color form
  LampOutput. Returns null if no collision.
   */
  def isCollide(mesh: Mesh, triangleIndex: Int, ray: Ray): (Vector3D, Float, Float)

  def throwRay(triangle: Triangle, position: Vector3D): Ray
}