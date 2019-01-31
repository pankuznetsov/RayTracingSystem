import java.awt.Graphics

import heavyweight.lights.PointLight
import heavyweight.nodes.{Diffuse, Emission, GetUV, Glossy}
import javax.swing.{JFrame, JPanel}
import lightweight.{Camera, Functions, Lamp, World}
import lightweight.geometry._
import lightweight.nodes._

import scala.io.Source._
import scala.collection.immutable.HashMap

object Main {

  def quad(firstPoint: Vector3D, secondPoint: Vector3D, thirdPoint: Vector3D, binormal: Boolean, surface: SurfaceOutput, volume: VolumeOutput): (Triangle, Triangle) = {
    val firstTriangle = Triangle(firstPoint, secondPoint, thirdPoint, binormal, surface, volume, null)
    val secondTriangle = Triangle(firstPoint, firstPoint + thirdPoint - secondPoint, thirdPoint, binormal, surface, volume, null)
    (firstTriangle, secondTriangle)
  }

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
            // println(image(x)(y))
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

  def openObj(path: String): String = io.Source.fromFile(path).mkString

  def main(args: Array[String]): Unit = {

    RayDistributor.generate(2048)

    // UV Map
    val map = UVMap("map_zero")
    val uvw = UVCoordinates(Vector2D(0,0),Vector2D(1,0),Vector2D(0,1))
    // Surface output
    val testSurfaceOut = Array.ofDim[Container](1)
    // GetUV node
    val getUVInputs = null
    val getUVOutputs = Array.ofDim[Container](1)
    val getUVNode = GetUV(getUVInputs, getUVOutputs, map)
     getUVOutputs(0) = Container(getUVNode, null)
    // Surface
    val testSurface = SurfaceOutput(getUVOutputs, testSurfaceOut)
    testSurfaceOut(0) = Container(testSurface, null)

    val uvCoordinates = UVCoordinates(Vector2D(0, 0), Vector2D(1, 0), Vector2D(0, 1))
    // val triangle = Triangle(Vector3D(1, 1, 0), Vector3D(1, 2, 0), Vector3D(2, 1, 0), true, testSurface, null, HashMap(UVMap("map_0") -> uvw))

    // World
    val worldEmissionInput = Array.ofDim[Container](1)
    worldEmissionInput(0) = Container(null, lightweight.nodes.Color(0.1f, 0.2f, 0.3f))
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
    emissionInput(0) = Container(null, lightweight.nodes.Color(0, 1.95f, 0))
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
    secondEmissionInput(0) = Container(null, lightweight.nodes.Color(32.25f, 0, 0))
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

    // Diffuse material
    val diffuseInput = Array.ofDim[Container](4)
    diffuseInput(0) = Container(null, lightweight.nodes.Color(1.5f, 0.5f, 0.5f))
    diffuseInput(1) = Container(null, lightweight.nodes.Numeric(1))
    diffuseInput(2) = Container(null, lightweight.nodes.Numeric(4))
    diffuseInput(3) = Container(null, null)
    // Diffuse Output
    val diffuseOut = Array.ofDim[Container](1)
    // Emission
    val diffuse = Diffuse(diffuseInput, diffuseOut)
    diffuseOut(0) = Container(diffuse, lightweight.nodes.Color(0, 0, 0))
    // Surface output
    val difuseSurfaceOut = Array.ofDim[Container](1)
    // Surface
    val difusalSurface = SurfaceOutput(diffuseOut, difuseSurfaceOut)
    difuseSurfaceOut(0) = Container(diffuse, null)

    // Glossy triangle
    // Glossy Input
    val glossyInput = Array.ofDim[Container](4)
    glossyInput(0) = Container(null, lightweight.nodes.Color(1, 1, 1))
    glossyInput(1) = Container(null, lightweight.nodes.Numeric(0.1f))
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
    val firstTriangle = Triangle(Vector3D(0, -60, 100), Vector3D(-20, -40, 60), Vector3D(20, -40, 60), true, emissionSurface, null, null).move(Vector3D(40, 140, -55))
    val plane = quad(Vector3D(-40, 20, 40), Vector3D(0, -40, 120), Vector3D(40, 20, 40), true, difusalSurface, null)
    //val secondTriangle = Triangle(Vector3D(-40, 20, 40), Vector3D(0, -40, 120), Vector3D(40, 20, 40), true, surface, null, HashMap(map -> uvCoordinates)).move(Vector3D(40, 70, 0))
    val secondTriangle: Triangle = plane._1.move(Vector3D(30, 70, 0))
    val thirdTriangle: Triangle = plane._2.move(Vector3D(30, 70, 0))
    val fourthTriangle = Triangle(Vector3D(0, -60, 100), Vector3D(-20, -40, 60), Vector3D(20, -40, 60), true, secondEmissionSurface, null, HashMap(map -> uvCoordinates)).move(Vector3D(20, 70, -5))
    // Mesh
    val mesh = Mesh(Array(firstTriangle, secondTriangle, thirdTriangle), Array[Lamp](PointLight(Vector3D(105, 30, 5), 32, 1000, LampOutput(secondEmissionOut, secondEmissionSurfaceOut), true, 4)))
    val camera = Camera(null, null, 1, 380, 320)
    val image = camera.render(mesh, world, 12)
    displayImage(image, 380, 320)
  }
}
