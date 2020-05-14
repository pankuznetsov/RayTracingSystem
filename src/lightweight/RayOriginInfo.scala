package lightweight

import lightweight.nodes.Node

// Tells about the origin of ray and thus, it's importance
case class RayOriginInfo(generator: Node, isCameraRay: Boolean = true)
