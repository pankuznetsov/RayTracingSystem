package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Container, Node, Color}
import util.control.Breaks._

case class ColorRamp(override val inputs: Array[Container],
                     override val outputs: Array[Container],
                     val colors: Array[lightweight.nodes.Color],
                     val plases: Array[Double],
                     val interpolateTipe: Int) extends Node(inputs, outputs) {
  /*
    Inputs
    0. Fac

    iterpolateTipe
    0. lineal
    1. const
    2. const2
   */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    if (interpolateTipe == 0) {
      var foredIndex = 0
      var niarSupportingColorPlase: Double = 0
      breakable {
        for (i: Int <- 0 until colors.length) {
          if (inputs(0).asInstanceOf[lightweight.nodes.Numeric].value >= plases(foredIndex)) {
            niarSupportingColorPlase = plases(foredIndex).asInstanceOf[lightweight.nodes.Numeric].value
            break
          }
          foredIndex += 1
        }
      }
      val nextSupportingColorPlase = plases(foredIndex + 1)
      val difference = nextSupportingColorPlase - niarSupportingColorPlase
      val newFac = inputs(0).asInstanceOf[lightweight.nodes.Numeric].value - niarSupportingColorPlase
      val colorsRatio = newFac / nextSupportingColorPlase
      colors(foredIndex)linearInterpolation(colors(foredIndex), colorsRatio)
    }
    if (interpolateTipe == 1) {
      var foredIndex = 0
      val niarSupportingColorPlase =
        for (i: Int <- 0 until colors.length) {
          if (inputs(0).asInstanceOf[lightweight.nodes.Numeric].value >= plases(foredIndex)) {
            plases(foredIndex).asInstanceOf[lightweight.nodes.Numeric].value
          }
          foredIndex += 1
        }
      val resultColor = colors(foredIndex + 1)
      resultColor
    }
  }
}
