package lightweight

import lightweight.geometry.{Mesh, Vector3D}
import lightweight.nodes.Color

case class MulticoreCamera(location: Vector3D, direction: Vector3D, scale: Double, width: Int, height: Int) {

  def render(mesh: Mesh, world: World, samples: Int, shadersLeft: Int, segSize: Int): Array[Array[Color]] = {
    val widthSegments: Int = width / segSize + width % segSize
    val heightSegments: Int = height / segSize + height % segSize
    val totalSegments: Int = widthSegments * heightSegments
    val segTable: Array[Array[Boolean]] = Array.ofDim(widthSegments, heightSegments)
    segTable.map(a => a.map(b => false))
    val imageRatio = ratio(width, height)
    val image = Array.ofDim[Color](width, height)
    val cores = Runtime.getRuntime.availableProcessors()
    val threadsPool = Array.tabulate[RenderingSegment](cores)(x => RenderingSegment(mesh, world, samples, shadersLeft,
      location, direction, image, scale, width, height))
    var exit: Boolean = false
    var candidateX: Int = 0
    var candidateY: Int = 0
    val startTime = System.currentTimeMillis()
    while (candidateY < height) {
      for (thread: Int <- 0 until threadsPool.length) {
        if (threadsPool(thread).isFinished()) {
          threadsPool(thread) = RenderingSegment(mesh, world, samples, shadersLeft,
            location, direction, image, scale, width, height)
          threadsPool(thread).segX = candidateX
          threadsPool(thread).segY = candidateY
          threadsPool(thread).segWidth = segSize
          threadsPool(thread).segHeight = segSize
          candidateX = candidateX + segSize
          if (candidateX >= width) {
            candidateX = 0
            candidateY = candidateY + segSize
          }
          threadsPool(thread).reset()
          threadsPool(thread).start()
        }
      }
    }
    val endTime = System.currentTimeMillis()
    val time = (endTime - startTime).asInstanceOf[Double] / 1000.0
    println(s"\tmulticore - time: %.2f".format(time))
    return image
  }

  def ratio(width: Int, height: Int): (Double, Double) = {
    val max = Math.max(height, width)
    val widthRatio = this.width / max
    val heightRatio = this.height / max
    (widthRatio, heightRatio)
  }

}
