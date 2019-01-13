package lightweight.nodes

import lightweight.geometry.{Mesh, Ray, Vector3D}

abstract class Node(val inputs: Array[Container], val outputs: Array[Container]) {

  final def run(mesh: Mesh, skySurface: SurfaceOutput, skyVolume: VolumeOutput, triangleIndex: Int, ray: Ray, hitPoint: Vector3D): Unit = {
    for (field <- inputs)
      if (field != null)
        field.parentNode.run(mesh: Mesh, skySurface, skyVolume, triangleIndex: Int, ray: Ray, hitPoint: Vector3D)
    doThings(mesh, skySurface, skyVolume, triangleIndex, ray, hitPoint)
  }

  def doThings(mesh: Mesh, skySurface: SurfaceOutput, skyVolume: VolumeOutput, triangleIndex: Int, ray: Ray, hitPoint: Vector3D): Unit
}
