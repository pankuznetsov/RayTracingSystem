package lightweight

import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.Color

/* Single threaded implementation of camera. It iterates throw all lines and pixels.
As work is done, the amount of time is printed. Returns the final image.
*/
case class Camera(location: Vector3D, direction: Vector3D, scale: Double, width: Int, height: Int) {

  def render(mesh: Mesh, world: World, samples: Int, shadersLeft: Int): Array[Array[Color]] = {
    val imageRatio = ratio(width, height)
    val image = Array.ofDim[Color](width, height)
    val startTime = System.currentTimeMillis()
    for(y: Int <- 0 until height){
      for(x: Int <- 0 until width){
        var pixel = Color(0, 0, 0)
        for (sampleY: Int <- 0 until samples)
          for (sampleX: Int <- 0 until samples) {
            val rayFromCamera = Ray(Vector3D((x / 2) + sampleX.asInstanceOf[Float] / samples + Math.random().asInstanceOf[Float] / samples,
              (y / 2) + sampleY.asInstanceOf[Float] / samples + Math.random().asInstanceOf[Float] / samples, 0), Vector3D(0, 0, 1))
            pixel += rayFromCamera.renderSample(mesh: Mesh, world: World, -1, shadersLeft, RayOriginInfo(null, true))._2
          }
        image(x)(y) = pixel / (samples * samples)
      }
      println(s"Line rendered: ${y}")
    }
    val endTime = System.currentTimeMillis()
    val time: Double = (endTime - startTime).asInstanceOf[Double] / 1000.0
    println("Time: %.2f".format(time))
    image
  }

  def ratio(width: Int, height: Int): (Double, Double) = {
    val widthRatio = this.width / Math.max(height, width)
    val heightRatio = this.height / Math.max(height, width)
    (widthRatio, heightRatio)
  }
}
