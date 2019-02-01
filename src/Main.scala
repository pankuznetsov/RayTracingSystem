import java.awt.image.BufferedImage
import java.awt.{Graphics, Image}
import java.io.{File, IOException}

import heavyweight.lights.PointLight
import heavyweight.nodes._
import javax.imageio.ImageIO
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

    // BSDF rays generation
    RayDistributor.generate(2048)

    val skyEmission = Factories.newEmission(Container(null, Color(0.1f, 0.1f, 0.6f)))
    val skyEmissionSurface = Factories.newSkySurface(skyEmission.outputs(0))
    val world = World(skyEmissionSurface, null)

    val map = UVMap("map_zero")
    val pathToFile = new File("C:\\Users\\Kuznetsov Sergey\\Downloads\\palpatine.jpg")
    val picture: BufferedImage = ImageIO.read(pathToFile)

    val getUV = Factories.newGetUV(map)
    val texture = Factories.newImageTexture(getUV.outputs(0), picture)
    val emission = Factories.newEmission(texture.outputs(0))
    val emissionSurface = Factories.newSurfaceOutput(emission.outputs(0))

    val firstTriangle = Triangle(Vector3D(10, 10, 100),
      Vector3D(10 + 160, 10, 100),
      Vector3D(10, 10 + 128, 60),
      true, emissionSurface, null,
      HashMap[UVMap, UVCoordinates](map -> UVCoordinates(Vector2D(20, 20), Vector2D(20 + 420, 0), Vector2D(0, 20 + 310))))

    // Mesh
    val mesh = Mesh(Array(firstTriangle), Array.ofDim[Lamp](0))
    val camera = Camera(null, null, 1, 380, 320)
    val image = camera.render(mesh, world, 12)
    displayImage(image, 380, 320)
  }
}
