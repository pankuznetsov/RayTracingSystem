package lightweight

import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, LampOutput}

abstract case class Lamp(location: Vector3D, size: Float, output: LampOutput) {

}
