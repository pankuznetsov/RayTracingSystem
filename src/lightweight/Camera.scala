package lightweight

import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.Color

case class Camera(location: Vector3D, direction: Vector3D, scale: Double, width: Int, height: Int) {

  def render(mesh: Mesh, world: World): Array[Array[Color]] = {
    val imageRatio = ratio(width, height)
    for(y <- 0 until height){
      for(x <- 0 until width){
        val rayFromCamera = Ray(location + Vector3D(imageRatio._1 / width * x, imageRatio._2 / height * y, 0) - Vector3D(imageRatio._1 / 2, imageRatio._2, 0),
          Vector3D(0,0,1))
        // val resultOfTraceRay = rayFromCamera.traceRay(mesh: Mesh, world: World, -1)
      }
    }
  }

  def ratio(width: Int, height: Int): (Double, Double) = {
    val widthRatio = this.width / Math.max(height, width)
    val heightRatio = this.height / Math.max(height, width)
    (widthRatio, heightRatio)
  }
}
