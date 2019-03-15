package lightweight

import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.Color

case class RenderingSegment(mesh: Mesh, world: World, samples: Int, shadersLeft: Int, location: Vector3D,
                            direction: Vector3D, image: Array[Array[lightweight.nodes.Color]],
                            scale: Double, width: Int, height: Int) extends Thread {

  var segId: Int = 0
  var segX: Int = 0
  var segY: Int = 0
  var segWidth: Int = 0
  var segHeight: Int = 0

  private var finished: Boolean = true
  private var time: Double = Double.NaN

  def isFinished(): Boolean = finished

  def reset(): Unit = finished = false

  override def run(): Unit = {
    val startTime = System.currentTimeMillis()
    println(s"segment: { x: \t${segX}, y: \t${segY} } ")
    for(y: Int <- segY until Math.min(segY + segHeight, height).asInstanceOf[Int]) {
      for (x: Int <- segX until Math.min(segX + segWidth, width).asInstanceOf[Int]) {
        var pixel = Color(0, 0, 0)
        for (sampleY: Int <- 0 until samples) {
          for (sampleX: Int <- 0 until samples) {
            val rayFromCamera = Ray(Vector3D((x / 2) + sampleX.asInstanceOf[Float] / samples + Math.random().asInstanceOf[Float] / samples,
              (y / 2) + sampleY.asInstanceOf[Float] / samples + Math.random().asInstanceOf[Float] / samples, 0), Vector3D(0, 0, 1))
            pixel += rayFromCamera.renderSample(mesh: Mesh, world: World, -1, shadersLeft)._2
          }
        }
        image(x)(y) = pixel / (samples * samples)
      }
      // println(s"#${segId} - line rendered: ${y}")
    }
    val endTime = System.currentTimeMillis()
    time = (endTime - startTime).asInstanceOf[Double] / 1000.0
    println(s"seg - time: %.2f".format(time))
    finished = true
  }
}
