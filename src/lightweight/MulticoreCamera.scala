package lightweight

import lightweight.geometry.{Mesh, Vector3D}
import lightweight.nodes.Color

case class MulticoreCamera(location: Vector3D, direction: Vector3D, scale: Double, width: Int, height: Int) {

  def render(mesh: Mesh, world: World, samples: Int, shadersLeft: Int): Array[Array[Color]] = {
    val imageRatio = ratio(width, height)
    val image = Array.ofDim[Color](width, height)
    val startTime = System.currentTimeMillis()
    val cores = Runtime.getRuntime.availableProcessors()
    val threadsPool = Array.tabulate[RenderingThread](cores)(n => {
      val t = RenderingThread(mesh, world, samples, shadersLeft, location, direction, image, scale, width, height, cores, n)
      t.setPriority(Thread.MAX_PRIORITY)
      t
    })
    for (thread <- threadsPool)
      thread.start()
    var exit: Boolean = false
    while (!exit) {
      exit = true
      for (thread <- threadsPool)
        exit = exit && thread.isFinished
    }
    return image
  }

  def ratio(width: Int, height: Int): (Double, Double) = {
    val max = Math.max(height, width)
    val widthRatio = this.width / max
    val heightRatio = this.height / max
    (widthRatio, heightRatio)
  }

}
