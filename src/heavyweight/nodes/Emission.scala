package heavyweight.nodes

import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Container, Node, SurfaceOutput, VolumeOutput}

class Emission(inputs: Array[Container], outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, skySurface: SurfaceOutput, skyVolume: VolumeOutput, triangleIndex: Int, ray: Ray, hitPoint: Vector3D): Unit = {
    outputs(0).content = inputs(0).content
  }
}
