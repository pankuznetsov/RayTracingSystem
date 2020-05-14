package lightweight

import lightweight.nodes.{SurfaceOutput, VolumeOutput}

/* The world is much like skybox. It has surface which is distant sky or possibly space,
volume which can be fog or smoke and global IOR - Index Of Refraction.
*/
case class World(skySurface: SurfaceOutput, skyVolume: VolumeOutput, worldIOR: Float)
