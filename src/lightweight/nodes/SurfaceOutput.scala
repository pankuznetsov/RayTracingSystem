package lightweight.nodes

import lightweight.geometry.{Mesh, Ray, Vector3D}

case class SurfaceOutput(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  override def doThings(mesh: Mesh, skySurface: SurfaceOutput, skyVolume: VolumeOutput, triangleIndex: Int, ray: Ray, hitPoint: Vector3D): Unit = {
    outputs(0).content = inputs(0).content
  }
}
