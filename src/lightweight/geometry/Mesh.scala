package lightweight.geometry

import lightweight.Lamp

import scala.collection.immutable.HashMap

case class Mesh(mesh: Array[Triangle], lamps: Array[Lamp], binder: HashMap[Int, Int]) {
  for (t <- mesh) println(t)
}