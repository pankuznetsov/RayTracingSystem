package lightweight

import lightweight.nodes.{SurfaceOutput, VolumeOutput}

case class World(skySurface: SurfaceOutput, skyVolume: VolumeOutput, worldIOR: Double)
