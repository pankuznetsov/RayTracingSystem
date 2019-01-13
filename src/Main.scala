import lightweight.{PointLight, World}
import lightweight.geometry._
import lightweight.nodes.{Color, SurfaceOutput, VolumeOutput}

import scala.collection.immutable.HashMap

object Main {

  def mix(colors: Array[Color]): Color = {
    var resultRed: Float = 0
    var resultGreen: Float = 0
    var resultBlue: Float = 0
    for (i <- colors.indices) {
      resultRed = resultRed + colors(i).red
      resultGreen = resultGreen + colors(i).green
      resultBlue = resultBlue + colors(i).blue
    }
    Color(resultRed, resultGreen, resultBlue)
  }

  def main(args: Array[String]): Unit = {
    /* val lamp = PointLight(Vector3D(0.5, 0.5, 6), 1.0, 100.0, null, true)
    val triangle = Triangle(Vector3D(1, -1, 2), Vector3D(0, 1, 3), Vector3D(-1, -1, 3), true, new SurfaceOutput(null, null), null).move(Vector3D(0, 0, 10))
    val ray = Ray(Vector3D(0, 0, 0), Vector3D(0, 0, 1))
    val mesh = Mesh(Array(triangle), Array(lamp))
    val resultTraceRay = ray.traceRay(mesh, null, -1)
    println(resultTraceRay)
    */
    val colors = Array.ofDim[Color](2)
    colors(0) = Color(0, 0, 1)
    colors(1) = Color(1, 0, 0)
    println(mix(colors))
  }
}
