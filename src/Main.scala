import java.awt.image.BufferedImage
import java.awt.{Graphics, Image}
import java.io.File
import java.nio.file._

import heavyweight.lights.PointLight
import heavyweight.nodes._
import javax.imageio.ImageIO
import javax.swing.{JFrame, JPanel}
import lightweight.{Camera, Functions, Lamp, World}
import lightweight.geometry._
import lightweight.nodes._

import scala.io.Source._
import scala.collection.immutable.HashMap
import scala.io.Source

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
    RayDistributor.generate(128)

    val skyEmission = Factories.newEmission(Container(null, Color(0.1f, 0.1f, 0.6f)))
    val skyEmissionSurface = Factories.newSkySurface(skyEmission.outputs(0))
    val world = World(skyEmissionSurface, null)

    val map = UVMap("map_zero")
    val pathToPicture = new File("C:\\Users\\Kuznetsov Sergey\\Documents\\Ray Tracing\\test_normal_map.png")
    val picture: BufferedImage = ImageIO.read(pathToPicture)

    val getUV = Factories.newGetUV(map)
    val texture = Factories.newImageTexture(getUV.outputs(0), picture)
    val diffuse = Factories.newDiffuse(Container(null, Color(0.94f, 0.94f, 0.94f)), Container(null, Numeric(1)), Container(null, Numeric(4)), null)
    val glossy = Factories.newGlossy(Container(null, Color(0.52f, 0.98f, 0.64f)), Container(null, Numeric(0)), Container(null, Numeric(1)), null)
    val emission = Factories.newEmission(Container(null, Color(0.9f, 0.2f, 0.1f)))
    val surface = Factories.newSurfaceOutput(diffuse.outputs(0))

    val lampEmissionRed = Factories.newEmission(Container(null, Color(12f, 1.5f, 1.5f)))
    val lampSurfaceRed = Factories.newLampOutput(lampEmissionRed.outputs(0))

    val lampEmissionGreen = Factories.newEmission(Container(null, Color(1f, 45f, 1f)))
    val lampSurfaceGreen = Factories.newLampOutput(lampEmissionGreen.outputs(0))

    val stringOBJ: String = Source.fromFile("C:\\Users\\Kuznetsov Sergey\\Documents\\Ray Tracing\\OBJ Test.obj").getLines.mkString("\n")
    println(stringOBJ)
    val loader = Loader(stringOBJ, Array(surface), Array(), world)
    loader.loadObj()
    println("triangles: " + loader.triangles.length)

    val firstTriangle = Triangle(Vector3D(10, 10, 100),
      Vector3D(10 + 200, 10, 100),
      Vector3D(10, 10 + 150, 90),
      true, surface, null,
      HashMap[UVMap, UVCoordinates](map -> UVCoordinates(Vector2D(0, 0), Vector2D(1150, 0), Vector2D(0, 640))))

    // Mesh
    val mesh = Mesh(loader.triangles.toArray.map((f: Triangle) => f.scale(1).move(Vector3D(25, 25, 10))),  Array(PointLight(Vector3D(110, 80, 180), 32, Double.MaxValue, lampSurfaceRed, true, 0)
      /* , PointLight(Vector3D(25, 25, 50), 12, Double.MaxValue, lampSurfaceGreen, true, 2) */ ))
    val camera = Camera(null, null, 1, 380, 320)
    val image = camera.render(mesh, world, 2)
    displayImage(image, 380, 320)
  }
}
