import java.awt.Graphics

import heavyweight.nodes.Emission
import javax.swing.{JFrame, JPanel}
import lightweight.{Camera, Lamp, PointLight, World}
import lightweight.geometry._
import lightweight.nodes.{Color, Container, SurfaceOutput, VolumeOutput}

import scala.collection.immutable.HashMap

object Main {

  def displayImage(image: Array[Array[lightweight.nodes.Color]], width: Int, height: Int): Unit = {
    val frame = new JFrame()
    frame.setResizable(false)
    frame.setSize(width * 4 + 10, height * 4 + 10)
    val panel = new JPanel() {
      override def paint(g: Graphics): Unit = {
        g.setColor(java.awt.Color.BLACK)
        g.fillRect(0, 0, getWidth, getHeight)
        for (y <- 0 until height)
          for (x <- 0 until width) {
            // println(image(x)(y))
            val color = new java.awt.Color(if (image(x)(y).red.asInstanceOf[Int] * 255 > 255) 255 else (image(x)(y).red * 255).asInstanceOf[Int],
              if (image(x)(y).green.asInstanceOf[Int] * 255 > 255) 255 else (image(x)(y).green * 255).asInstanceOf[Int],
            if (image(x)(y).blue.asInstanceOf[Int] * 255 > 255) 255 else (image(x)(y).blue * 255).asInstanceOf[Int])
            g.setColor(color)
            g.fillRect(x * 4, y * 4, 4, 4)
          }
      }
    }
    frame.add(panel)
    frame.show()
  }

  def main(args: Array[String]): Unit = {
    // Emission Input
    val emissionInput = Array.ofDim[Container](1)
    emissionInput(0) = Container(null, lightweight.nodes.Color(0.8f, 0, 0))
    // Emission Output
    val emissionOut = Array.ofDim[Container](1)
    // Emission
    val emission = Emission(emissionInput, emissionOut)
    emissionOut(0) = Container(emission, lightweight.nodes.Color(0, 0, 0))
    // Surface output
    val surfaceOut = Array.ofDim[Container](1);
    // Surface
    val surface = SurfaceOutput(emissionOut, surfaceOut)
    surfaceOut(0) = Container(surface, null)
    val firstTriangle = Triangle(Vector3D(0, 0, 20), Vector3D(20, 0, 20), Vector3D(0, 20, 20), true, surface, null)
    val mesh = Mesh(Array(firstTriangle), Array[Lamp]())
    val camera = Camera(null, null, 1, 50, 50)
    val image = camera.render(mesh, null)
    displayImage(image, 50, 50)
  }
}
