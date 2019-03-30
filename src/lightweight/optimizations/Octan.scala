package lightweight.optimizations

import lightweight.geometry.Sphere

case class Octan(bounds: Sphere, inside: Array[OctanContent]) extends OctanContent
