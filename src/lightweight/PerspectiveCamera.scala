package lightweight

import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.Color

case class PerspectiveCamera(location: Vector3D, direction: Vector3D, viewingAngle: Float, width: Int, height: Int, sphereLens: Boolean) {

  def render(mesh: Mesh, world: World, samples: Int, shadersLeft: Int): Array[Array[Color]] = {
    val imageRatio = ratio(width, height)
    // Vector3D((imageRatio._1 / width * (x + sampleX / width) - imageRatio._1 / 2).asInstanceOf[Float], (imageRatio._2 / height * (y + sampleY / height) - imageRatio._2 / 2).asInstanceOf[Float], viewingAngle).normalized)
    val image = Array.ofDim[Color](width, height)
    val startTime = System.currentTimeMillis()
    for(y: Int <- 0 until height){
      //val yy = Vector3D(- width / 2, y * imageRatio._2 / height, viewingAngle)
      for(x: Int <- 0 until width){
        var pixel = Color(0, 0, 0)
        for (sampleY: Int <- 0 until samples)
          for (sampleX: Int <- 0 until samples) {
            val xDirection = x + sampleX / samples
            val yDirection = y + sampleY / samples
            val rayFromCamera = if (!sphereLens) {
              Ray(location, Vector3D(imageRatio._1 / height * xDirection - imageRatio._1 / 2, imageRatio._1 / width * yDirection - imageRatio._1 / 2,
                viewingAngle).normalized)
            } else {
              null
            }
            pixel += rayFromCamera.renderSample(mesh: Mesh, world: World, -1, shadersLeft)._2
          }
        image(x)(y) = pixel / (samples * samples)
        // println(s"pixel rendered - x: ${x}, y: ${y}")
      }
      println(s"line rendered: ${y}")
    }
    val endTime = System.currentTimeMillis()
    val time: Double = (endTime - startTime).asInstanceOf[Double] / 1000.0
    println("time: %.2f".format(time))
    image
  }

  def ratio(width: Int, height: Int): (Float, Float) = {
    val widthRatio = this.width / Math.max(height, width)
    val heightRatio = this.height / Math.max(height, width)
    (widthRatio, heightRatio)
  }
}
