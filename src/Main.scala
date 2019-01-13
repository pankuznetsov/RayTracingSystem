import lightweight.{PointLight, World}
import lightweight.geometry._
import lightweight.nodes.{SurfaceOutput, VolumeOutput}

import scala.collection.immutable.HashMap

object Main {

  def main(args: Array[String]): Unit = {
    println("I am not Alex, this is my second commit")
    val lamp = PointLight(Vector3D(0.5, 0.5, 6), 1.0, 100.0, null, true)
    val triangle = Triangle(Vector3D(1, -1, 2), Vector3D(0, 1, 3), Vector3D(-1, -1, 3), true, new SurfaceOutput(null, null), null).move(Vector3D(0, 0, 10))
    val ray = Ray(Vector3D(0, 0, 0), Vector3D(0, 0, 1))
    val mesh = Mesh(Array(triangle), Array(lamp))
    val resultTraceRay = ray.traceRay(mesh, null, -1)
    println(resultTraceRay)
  }
}
