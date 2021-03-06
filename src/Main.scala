import java.awt.image.BufferedImage
import java.awt.{Graphics, Image}
import java.io.File
import java.nio.file._

import heavyweight.lights.PointLight
import heavyweight.nodes._
import javax.imageio.ImageIO
import javax.swing.{JFrame, JPanel}
import lightweight._
import lightweight.geometry._
import lightweight.nodes._

import scala.io.Source._
import scala.collection.immutable.HashMap
import scala.io.Source

object Main {

  def quad(firstPoint: Vector3D, secondPoint: Vector3D, thirdPoint: Vector3D, binormal: Boolean, surface: SurfaceOutput, volume: VolumeOutput): (Triangle, Triangle) = {
    val firstTriangle = Triangle(firstPoint, secondPoint, thirdPoint, binormal, surface, volume, null, null, null, null)
    val secondTriangle = Triangle(firstPoint, firstPoint + thirdPoint - secondPoint, thirdPoint, binormal, surface, volume, null, null, null, null)
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

  def copyDirectionForRayTeleport(inTriangle: Triangle, outTriangle: Triangle, ray: Ray): Vector3D = {
    val inTriangleNormal = if (inTriangle.supportingPlane.normal.sameDirection(ray.direction)){inTriangle.supportingPlane.normal} else {inTriangle.supportingPlane.normal.invert()}
    val outTriangleNormal = if (outTriangle.supportingPlane.normal.sameDirection(ray.direction)){outTriangle.supportingPlane.normal} else {outTriangle.supportingPlane.normal.invert()}
    val firstProgection: Float =  (inTriangle.a - inTriangle.b).normalized dotProduct(ray.direction)
    val secondProgection: Float = (inTriangle.b - inTriangle.c).normalized dotProduct(ray.direction)
    //val thirdProgection: Float =  (inTriangle.c - inTriangle.a).normalized dotProduct(ray.direction)
    val progectionToNormal: Float = inTriangleNormal dotProduct(ray.direction)
    val result =
      (outTriangle.a - outTriangle.b).normalized * firstProgection +
        (outTriangle.b - outTriangle.c).normalized * secondProgection +
        //(outTriangle.c - outTriangle.a).normalized * thirdProgection +
        outTriangleNormal * progectionToNormal
    result.normalized
  }

  def openObj(path: String): String = io.Source.fromFile(path).mkString


  def main(args: Array[String]): Unit = {
    val skyEmission = Factories.newEmission(Container(null, Color(0.3f, 0.5f, 0.75f)), Container(null, Numeric(1.3f)))
    val skyEmissionSurface = Factories.newSkySurface(skyEmission.outputs(0))
    val world = World(skyEmissionSurface, null, 1)

    val map = Strings.defaultUWMap
    val pathToPicture = new File("C:\\Users\\Kuznetsov S. A\\Documents\\alex\\blender\\images\\textures\\apple.jpg")
    val picture: BufferedImage = ImageIO.read(pathToPicture.getCanonicalFile)

    val getUV = Factories.newGetUV(map)
    val texture = Factories.newImageTexture(getUV.outputs(0), picture)
    val light = Factories.newLampIllumination()
    val diffuseZero = Factories.newDiffuse(Container(null, Color(0.06f, 0.04f, 0.03f)), Container(null, Numeric(0.9f)), Container(null, Numeric(2)), null, light.outputs(0))
    val diffuseOne = Factories.newDiffuse(Container(null, Color(0.9f, 0.9f, 0.9f)), Container(null, Numeric(0.9f)), Container(null, Numeric(2)), null, light.outputs(0))
    val diffuseTwo = Factories.newDiffuse(Container(null, Color(2.3f, 5.03f, 0.24f)), Container(null, Numeric(0.9f)), Container(null, Numeric(2)), null, light.outputs(0))
    val diffuseThree = Factories.newDiffuse(Container(null, Color(0.3f, 0.03f, 0.24f)), Container(null, Numeric(0.9f)), Container(null, Numeric(2)), null, light.outputs(0))
    val transparent = Factories.newTransparent(Container(null, Color(1f, 1f, 1f)), Container(null, Numeric(0.0f)), Container(null, Numeric(1)), null, null)
    val translucent = Factories.newTranslucent(Container(null, Color(0.94f, 0.92f, 0.90f)), Container(null, Numeric(1)), Container(null, Numeric(2)), null, null)
    val glossy = Factories.newGlossy(Container(null, Color(1.0f, 1.0f, 1.0f)), Container(null, Numeric(0.0f)), Container(null, Numeric(1)), null, light.outputs(0))
    val emission = Factories.newEmission(Container(null, Color(0.3f, 0.2f, 0.05f)), Container(null, Numeric(2f)))
    val rgbToGrayscale = Factories.newRGBToBW(glossy.outputs(0))
    val rayTeleport = Factories.newRayTeleport()
    val refraction = Factories.newRefraction(Container(null, Color(0.9f, 0.8f, 0.7f)), Container(null, Numeric(0.05f)), Container(null, Numeric(2)), null, null, Container(null, Numeric(5)))

    val appleDiffuse = Factories.newDiffuse(texture.outputs(0), Container(null, Numeric(1f)), Container(null, Numeric(4f)), null, light.outputs(0))


    val lampEmissionRed = Factories.newEmission(Container(null, Color(1.3f, 0.95f, 0.65f)), Container(null, Numeric(19f)))
    val lampSurfaceRed = Factories.newLampOutput(lampEmissionRed.outputs(0))
    val lampEmissionGreen = Factories.newEmission(Container(null, Color(2.5f, 3.5f, 0.5f)), Container(null, Numeric(0f)))
    val lampSurfaceGreen = Factories.newLampOutput(lampEmissionGreen.outputs(0))

    /* House material
    * */

    val pathToChecerPicture = new File("C:\\Users\\Kuznetsov S. A\\Documents\\alex\\blender\\images\\textures\\checer.jpg")
    val checerImage: BufferedImage = ImageIO.read(pathToChecerPicture.getCanonicalFile)
    val checerTexture = Factories.newImageTexture(getUV.outputs(0), checerImage)
    val checerDiffuse = Factories.newDiffuse(checerTexture.outputs(0), Container(null, Numeric(0.9f)), Container(null, Numeric(1)), null, light.outputs(0))

    val angle = Factories.newGetAngle()

    val checerGlossy = Factories.newGlossy(Container(null, Color(0.97f, 0.97f, 0.97f)), Container(null, Numeric(0.06f)), Container(null, Numeric(2f)), null, null)
    val mixGlossyAndDiffuse = Factories.newMix(angle.outputs(1), checerDiffuse.outputs(0), checerGlossy.outputs(0))

    val checerSurfaceOutput = Factories.newSurfaceOutput(mixGlossyAndDiffuse.outputs(0))

    val horseDiffuse = Factories.newDiffuse(Container(null, Color(0.027f, 0.014f, 0.005f)), Container(null, Numeric(0.9f)), Container(null, Numeric(1)), null, light.outputs(0))
    val horseGlossy = Factories.newGlossy(Container(null, Color(0.97f, 0.97f, 0.97f)), Container(null, Numeric(0.06f)), Container(null, Numeric(1f)), null, null)
    val mixHorseGlossyAndDiffuse = Factories.newMix(angle.outputs(1), horseGlossy.outputs(0), horseDiffuse.outputs(0))
    val horseSurface = Factories.newSurfaceOutput(mixHorseGlossyAndDiffuse.outputs(0))
    /*
    *  Home material! Home material! Home material! Home material! Home material! Home material! Home material! Home material!
    * */ /*
    val pathToWoodPicture = new File("C:\\Users\\Kuznetsov S. A\\Documents\\alex\\blender\\images\\textures\\wood.jpg")
    val woodImage: BufferedImage = ImageIO.read(pathToWoodPicture.getCanonicalFile)
    val woodTexture = Factories.newImageTexture(getUV.outputs(0), woodImage)

    val pathToRoofPicture = new File("C:\\Users\\Kuznetsov S. A\\Documents\\alex\\blender\\images\\textures\\roof_1.jpg")
    val roofImage: BufferedImage = ImageIO.read(pathToRoofPicture.getCanonicalFile)
    val roofTexture = Factories.newImageTexture(getUV.outputs(0), roofImage)

    val pathToBrickPicture = new File("C:\\Users\\Kuznetsov S. A\\Documents\\alex\\blender\\images\\textures\\white stone WOSH.jpg")
    val brickImage: BufferedImage = ImageIO.read(pathToBrickPicture.getCanonicalFile)
    val brickTexture = Factories.newImageTexture(getUV.outputs(0), brickImage)

    val angle = Factories.newGetAngle()

    val wallDiffuse = Factories.newDiffuse(Container(null, Color(0.95f, 0.95f, 0.95f)), Container(null, Numeric(0.9f)), Container(null, Numeric(2)), null, light.outputs(0))
    val woodDiffuse = Factories.newDiffuse(woodTexture.outputs(0), Container(null, Numeric(0.9f)), Container(null, Numeric(2)), null, light.outputs(0))
    val glass = Factories.newGlossy(angle.outputs(0), Container(null, Numeric(0.02f)), Container(null, Numeric(1f)), null, null)
    val roof = Factories.newDiffuse(roofTexture.outputs(0), Container(null, Numeric(0.9f)), Container(null, Numeric(2)), null, light.outputs(0))
    val brickWall = Factories.newDiffuse(brickTexture.outputs(0), Container(null, Numeric(0.9f)), Container(null, Numeric(2)), null, light.outputs(0))

    val wallSurface = Factories.newSurfaceOutput(wallDiffuse.outputs(0))
    val woodSurface = Factories.newSurfaceOutput(woodDiffuse.outputs(0))
    val glassSurface = Factories.newSurfaceOutput(glass.outputs(0))
    val roofSurface = Factories.newSurfaceOutput(roof.outputs(0))
    val brickSurface = Factories.newSurfaceOutput(brickWall.outputs(0))

*/
    /*
    * Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh! Mesh!
    * */
    // val stringOBJ: String = Source.fromFile("C:\\Users\\Kuznetsov Sergey\\Documents\\Ray Tracing\\cube.obj").getLines.mkString("\n")
    val stringOBJ: String = Source.fromFile("C:\\Users\\Kuznetsov S. A\\Documents\\alex\\Ray Tracer\\checer.obj").getLines.mkString("\n")
    // val stringOBJ: String = Source.fromFile("C:\\Users\\Kuznetsov Sergey\\Documents\\Ray Tracing\\x_wing.obj").getLines.mkString("\n")
    println(stringOBJ)
    val loader = Loader(stringOBJ, Array(horseSurface, checerSurfaceOutput), Array(), world)
    loader.loadObj()
    println("triangles: " + loader.triangles.length)

    /* val firstTriangle = Triangle(Vector3D(10, 10, 100),
      Vector3D(10 + 200, 10, 100),
      Vector3D(10, 10 + 150, 50),
      true, surface, null,
      HashMap[UVMap, UVCoordinates](map -> UVCoordinates(Vector2D(0, 0), Vector2D(1600, 0), Vector2D(0, 1050)))) */

    // Mesh
    val mesh = Mesh(loader.triangles.toArray.map((f: Triangle) => f.scale(1.25f).move(Vector3D(140, 120, 110))), Array(/*PointLight(Vector3D(10, 110, 490), 35, Double.MaxValue, lampSurfaceRed, true, 1),*/
      PointLight(Vector3D(200, 20, 50), 1, Float.MaxValue, lampSurfaceRed, true, 0),
      PointLight(Vector3D(-90, -151, 40), 200, Float.MaxValue, lampSurfaceRed, true, 2)), HashMap(0 -> 2, 1 -> 3))
    // val mesh = Mesh(Array(firstTriangle),  Array(PointLight(Vector3D(70, 70, 10), 8, Double.MaxValue, lampSurfaceRed, true, 0)))
    val camera = lightweight.MulticoreCamera(Vector3D(0, 0, 0), Vector3D(0, 0, 1), 1f, 640, 480)
    println("started")
    val image = camera.render(mesh, world, 3, 3, 16)
    displayImage(image, 640, 480)
  }
}