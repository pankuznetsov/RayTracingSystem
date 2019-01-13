package lightweight

import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, LampOutput}

abstract class Lamp(val location: Vector3D, val size: Double, val maxDistance: Double, val output: LampOutput, val directlyVisible: Boolean) {

  /* If ray hits the lamp the tuple will be returned containing collision point,
  distance to the collision point and multiplier coefficient for force of color form
  LampOutput. Returns null if no collision.
   */
  def isCollide(ray: Ray): (Vector3D, Double, Double)
}