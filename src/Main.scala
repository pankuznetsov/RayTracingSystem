import java.awt.Graphics

import heavyweight.nodes.{Emission, Glossy}
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
    // Emission triangle
    // Emission Input
    val emissionInput = Array.ofDim[Container](1)
    emissionInput(0) = Container(null, lightweight.nodes.Color(0, 1.0f, 0))
    // Emission Output
    val emissionOut = Array.ofDim[Container](1)
    // Emission
    val emission = Emission(emissionInput, emissionOut)
    emissionOut(0) = Container(emission, lightweight.nodes.Color(0, 0, 0))
    // Surface output
    val emissionSurfaceOut = Array.ofDim[Container](1)
    // Surface
    val emissionSurface = SurfaceOutput(emissionOut, emissionSurfaceOut)
    emissionSurfaceOut(0) = Container(emissionSurface, null)

    // Glossy triangle
    // Emission Input
    val glossyInput = Array.ofDim[Container](1)
    glossyInput(0) = Container(null, null)
    // Glossy Output
    val glossyOut = Array.ofDim[Container](1)
    // Emission
    val glossy = Glossy(glossyInput, glossyOut)
    glossyOut(0) = Container(glossy, lightweight.nodes.Color(0, 0, 0))
    // Surface output
    val surfaceOut = Array.ofDim[Container](1)
    // Surface
    val surface = SurfaceOutput(glossyOut, surfaceOut)
    surfaceOut(0) = Container(surface, null)
    // Triangles
    val firstTriangle = Triangle(Vector3D(0, -60, 100), Vector3D(-20, -40, 60), Vector3D(20, -40, 60), true, emissionSurface, null).move(Vector3D(40,70,0))
    val secondTriangle = Triangle(Vector3D(-40, 20, 40), Vector3D(0, -40, 120), Vector3D(40, 20, 40), true, surface, null).move(Vector3D(40, 70,0))
    // Mesh
    val mesh = Mesh(Array(firstTriangle, secondTriangle), Array[Lamp]())
    val camera = Camera(null, null, 1, 160, 128)
    val image = camera.render(mesh, null)
    displayImage(image, 160, 128)
  }
}
