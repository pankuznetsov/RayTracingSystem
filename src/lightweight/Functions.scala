package lightweight

import lightweight.geometry.Vector3D
import lightweight.nodes.Color

object Functions {

  def rotate(mesh: Array[Vector3D], pitch: Double, roll: Double, yaw: Double): IndexedSeq[Vector3D] = {
    val cosYaw = Math.cos(yaw)
    val sinYaw = Math.sin(yaw)

    val cosPitch = Math.cos(pitch)
    val sinPitch = Math.sin(pitch)

    val cosRoll = Math.cos(roll)
    val sinRoll = Math.sin(roll)

    val axisXX = cosYaw * cosPitch
    val axisXY = cosYaw * sinPitch * sinRoll - sinYaw * cosRoll
    val axisXZ = cosYaw * sinPitch * cosRoll + sinYaw * sinRoll

    val axisYX = sinYaw * cosPitch
    val axisYY = sinYaw * sinPitch * sinRoll + cosYaw * cosRoll
    val axisYZ = sinYaw * sinPitch * cosRoll - cosYaw * sinRoll

    val axisZX = -sinPitch
    val axisZY = cosPitch * sinRoll
    val axisZZ = cosPitch * cosRoll

    val rotatedMesh: IndexedSeq[Vector3D] = for {
      index <- mesh.indices
      vertex = Vector3D(axisXX * mesh(index).x + axisXY * mesh(index).y + axisXZ * mesh(index).z,
        axisYX * mesh(index).x + axisYY * mesh(index).y + axisYZ * mesh(index).z,
        axisZX * mesh(index).x + axisZY * mesh(index).y + axisZZ * mesh(index).z)
    } yield vertex
    rotatedMesh
  }
}
