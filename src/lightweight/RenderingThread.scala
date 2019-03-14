package lightweight

import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.Color

case class RenderingThread(mesh: Mesh, world: World, samples: Int, shadersLeft: Int, location: Vector3D,
                           direction: Vector3D, image: Array[Array[lightweight.nodes.Color]],
                           scale: Double, width: Int, height: Int, cores: Int, everyN: Int) extends Thread {

  private var finished: Boolean = false
  private var time: Double = Double.NaN

  def isFinished(): Boolean = finished

  override def run(): Unit = {
    val startTime = System.currentTimeMillis()
    for(y: Int <- 0 until height) {
      for (x: Int <- 0 until width) {
        var pixel = Color(0, 0, 0)
        for (sample <- everyN until samples * samples by cores) {
          val sampleX: Int = sample % samples
          val sampleY: Int = sample / samples
          val rayFromCamera = Ray(Vector3D((x / 2) + sampleX.asInstanceOf[Float] / samples + Math.random().asInstanceOf[Float] / samples,
            (y / 2) + sampleY.asInstanceOf[Float] / samples + Math.random().asInstanceOf[Float] / samples, 0), Vector3D(0, 0, 1))
          pixel += rayFromCamera.renderSample(mesh: Mesh, world: World, -1, shadersLeft)._2
        }
        if (image(x)(y) != null)
          image(x)(y) = image(x)(y) + pixel / (samples * samples)
        else
          image(x)(y) = (pixel / (samples * samples))
      }
      println(s"#${everyN} - line rendered: ${y}")
    }
    val endTime = System.currentTimeMillis()
    time = (endTime - startTime).asInstanceOf[Double] / 1000.0
    println(s"\ttime: ${time}")
    finished = true
  }
}
