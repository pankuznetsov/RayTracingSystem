package lightweight.geometry

import lightweight.Lamp

case class Mesh(mesh: Array[Triangle], lamps: Array[Lamp]) {
  for (t <- mesh) println(t)
}