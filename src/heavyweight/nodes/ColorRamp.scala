package heavyweight.nodes

import lightweight.World
import lightweight.geometry.{Mesh, Ray, Vector3D}
import lightweight.nodes.{Color, Container, Node, Numeric}

case class ColorRamp(override val inputs: Array[Container],
                     override val outputs: Array[Container],
                     colorPoints: Array[(Double, lightweight.nodes.Color)],
                     interpolationType: Int) extends Node(inputs, outputs) {
  /* Inputs
    0. Facture
   */
  override def doThings(mesh: Mesh, world: World, triangleIndex: Int, ray: Ray, hitPoint: Vector3D, shadersLeft: Int): Unit = {
    val facture: Double = inputs(0).content.asInstanceOf[Numeric].value
    if (facture <= colorPoints(0)._1) {
      outputs(0).content = colorPoints(0)._2
      return
    }
    if (facture >= colorPoints.last._1) {
      outputs(0).content = colorPoints.last._2
      return
    }
    if (interpolationType == 0) {
      for (forIndex: Int <- colorPoints.length) {
        if (facture > colorPoints(forIndex)._1 && facture < colorPoints(forIndex + 1)._1) {
          val difference = colorPoints(forIndex + 1)._1 - colorPoints(forIndex)._1
          val newFacture = (facture - colorPoints(forIndex)._1) / difference
          outputs(0).content = colorPoints(forIndex)._2 linearInterpolation(colorPoints(forIndex + 1)._2, newFacture)
          return
        }
      }
    }
    if (interpolationType == 1) {
      for (forIndex: Int <- colorPoints.length) {
        if (facture > colorPoints(forIndex)._1 && facture < colorPoints(forIndex + 1)._1) {
          outputs(0).content = colorPoints(forIndex)._2
          return
        }
      }
    }
    if(interpolationType == 2) {
      for (forIndex: Int <- colorPoints.length) {
        if (facture > colorPoints(forIndex)._1 && facture < colorPoints(forIndex + 1)._1) {
          outputs(0).content = colorPoints(forIndex + 1)._2
          return
        }
      }
    }
    if (interpolationType == 3) {
      for (forIndex: Int <- colorPoints.length) {
        if (facture > colorPoints(forIndex)._1 && facture < colorPoints(forIndex + 1)._1) {
          val difference = colorPoints(forIndex + 1)._1 - colorPoints(forIndex)._1
          val newFacture = (facture - colorPoints(forIndex)._1) / difference
          outputs(0).content = colorPoints(forIndex)._2 linearInterpolation(colorPoints(forIndex + 1)._2, 0.5)
          return
        }
      }
    }
  }
}
