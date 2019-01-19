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
    frame.setSize(width + 50, height + 50)
    val panel = new JPanel() {
      override def paint(g: Graphics): Unit = {
        g.setColor(java.awt.Color.BLACK)
        g.fillRect(0, 0, getWidth, getHeight)
        for (y <- 0 until height)
          for (x <- 0 until width) {
            val color = new java.awt.Color(if ((image(x)(y).red * 255).asInstanceOf[Int] > 255) 255 else (image(x)(y).red * 255).asInstanceOf[Int],
              if ((image(x)(y).green * 255).asInstanceOf[Int] > 255) 255 else (image(x)(y).green * 255).asInstanceOf[Int],
            if ((image(x)(y).blue * 255).asInstanceOf[Int] > 255) 255 else (image(x)(y).blue * 255).asInstanceOf[Int])
            g.setColor(color)
            g.fillRect(x, y, 1, 1)
          }
      }
    }
    frame.add(panel)
    frame.show()
  }

  def main(args: Array[String]): Unit = {
    /*
    val triangle = Triangle(Vector3D(0, 0, 0), Vector3D(1, 0, 0), Vector3D(0, 1, 0), false, null, null)
    println(s"normal: ${triangle.getNormal()}")
    println(s"\t#1: random: ${triangle.scatter(0)}")
    println(s"\t#2: random: ${triangle.scatter(0.25)}")
    println(s"\t#3: random: ${triangle.scatter(0.5)}")
    println(s"\t#4: random: ${triangle.scatter(0.75)}")
    println(s"\t#5: random: ${triangle.scatter(1.0)}")
    */
    RayDistributor.generate(256)

    // World
    val worldEmissionInput = Array.ofDim[Container](1)
    worldEmissionInput(0) = Container(null, lightweight.nodes.Color(0.1f, 0.2f, 0.7f))
    // Emission Output
    val worldEmissionOut = Array.ofDim[Container](1)
    // Emission
    val worldEmission = Emission(worldEmissionInput, worldEmissionOut)
    worldEmissionOut(0) = Container(worldEmission, lightweight.nodes.Color(0, 0, 0))
    // Surface output
    val worldEmissionSurfaceOut = Array.ofDim[Container](1)
    // Surface
    val worldEmissionSurface = SurfaceOutput(worldEmissionOut, worldEmissionSurfaceOut)
    worldEmissionSurfaceOut(0) = Container(worldEmission, null)

    // World
    val world = World(worldEmissionSurface, null)

    // Emission triangle
    // Emission Input
    val emissionInput = Array.ofDim[Container](1)
    emissionInput(0) = Container(null, lightweight.nodes.Color(0, 0.95f, 0))
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

    // Second Emission triangle
    // Emission Input
    val secondEmissionInput = Array.ofDim[Container](1)
    secondEmissionInput(0) = Container(null, lightweight.nodes.Color(2.5f, 0, 0))
    // Emission Output
    val secondEmissionOut = Array.ofDim[Container](1)
    // Emission
    val secondEmission = Emission(secondEmissionInput, secondEmissionOut)
    secondEmissionOut(0) = Container(secondEmission, lightweight.nodes.Color(0, 0, 0))
    // Surface output
    val secondEmissionSurfaceOut = Array.ofDim[Container](1)
    // Surface
    val secondEmissionSurface = SurfaceOutput(secondEmissionOut, secondEmissionSurfaceOut)
    secondEmissionSurfaceOut(0) = Container(secondEmissionSurface, null)

    // Glossy triangle
    // Emission Input
    val glossyInput = Array.ofDim[Container](4)
    glossyInput(0) = Container(null, lightweight.nodes.Color(1, 1, 1))
    glossyInput(1) = Container(null, lightweight.nodes.Numeric(0))
    glossyInput(2) = Container(null, lightweight.nodes.Numeric(1))
    glossyInput(3) = Container(null, null)
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
    val firstTriangle = Triangle(Vector3D(0, -60, 100), Vector3D(-20, -40, 60), Vector3D(20, -40, 60), true, emissionSurface, null).move(Vector3D(40, 70, 0))
    val secondTriangle = Triangle(Vector3D(-40, 20, 40), Vector3D(0, -40, 120), Vector3D(40, 20, 40), true, surface, null).move(Vector3D(40, 70, 0))
    val thirdTriangle = Triangle(Vector3D(0, -60, 100), Vector3D(-20, -40, 60), Vector3D(20, -40, 60), true, secondEmissionSurface, null).move(Vector3D(10, 70, 1))
    // Mesh
    val mesh = Mesh(Array(firstTriangle, secondTriangle, thirdTriangle), Array[Lamp]())
    val camera = Camera(null, null, 1, 480, 320)
    val image = camera.render(mesh, world)
    displayImage(image, 480, 320)
  }
}
