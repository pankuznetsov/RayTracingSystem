package heavyweight.nodes

import lightweight.{RayOriginInfo, World}
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node}

case class LightPath(override val inputs: Array[Container], override val outputs: Array[Container]) extends Node(inputs, outputs) {

  /* The LightPath node outputs are:
      0. is ray from Camera
      1. is ray from Diffuse
      2. is ray from Glossy
      3. is ray from RayTeleport
      4. is ray from Refraction
      5. is ray from Translucent
      6. is ray from Transparent
      7. is ray from VolumeScatter
   */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, coordinates: Vector3D, backColor: Color, shadersLeft: Int, rayOriginInfo: RayOriginInfo): Unit = {
    for (i :Int <- 1 until 7) outputs(i).content = lightweight.nodes.Numeric(0)
    require(rayOriginInfo != null, "RayOriginInfo is null, Skywalker")
    outputs(0).content = if (
      rayOriginInfo.isCameraRay
    ) {
      lightweight.nodes.Numeric(1)
    }
    else
    {
      lightweight.nodes.Numeric(0)
    }
    if (rayOriginInfo == null || rayOriginInfo.generator == null || rayOriginInfo.isCameraRay) return
    outputs(1).content = if (!rayOriginInfo.generator.isInstanceOf[Diffuse]) { lightweight.nodes.Numeric(0) } else { lightweight.nodes.Numeric(1) }
    outputs(2).content = if (!rayOriginInfo.generator.isInstanceOf[Glossy]) { lightweight.nodes.Numeric(0) } else { lightweight.nodes.Numeric(1) }
    outputs(3).content = if (!rayOriginInfo.generator.isInstanceOf[RayTeleport]) { lightweight.nodes.Numeric(0) } else { lightweight.nodes.Numeric(1) }
    outputs(4).content = if (!rayOriginInfo.generator.isInstanceOf[Refraction]) { lightweight.nodes.Numeric(0) } else { lightweight.nodes.Numeric(1) }
    outputs(5).content = if (!rayOriginInfo.generator.isInstanceOf[Translucent]) { lightweight.nodes.Numeric(0) } else { lightweight.nodes.Numeric(1) }
    outputs(6).content = if (!rayOriginInfo.generator.isInstanceOf[Transparent]) { lightweight.nodes.Numeric(0) } else { lightweight.nodes.Numeric(1) }
    outputs(7).content = if (!rayOriginInfo.generator.isInstanceOf[VolumeScatter]) { lightweight.nodes.Numeric(0) } else { lightweight.nodes.Numeric(1) }
  }
}
