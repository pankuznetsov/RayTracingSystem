package heavyweight.nodes

import java.awt.image.BufferedImage

import lightweight.geometry.UVMap
import lightweight.nodes._

object Factories {

  def newSurfaceOutput(color: Container): SurfaceOutput = {
    val inputs = Array.ofDim[Container](1)
    inputs(0) = color
    val outputs = Array.ofDim[Container](1)
    val surface = SurfaceOutput(inputs, outputs)
    outputs(0) = Container(surface, null)
    return surface
  }

  def newVolumeOutput(color: Container): VolumeOutput = {
    val inputs = Array.ofDim[Container](1)
    inputs(0) = color
    val outputs = Array.ofDim[Container](1)
    val volume = VolumeOutput(inputs, outputs)
    outputs(0) = Container(volume, null)
    return volume
  }

  def newSkySurface(color: Container): SurfaceOutput = {
    val inputs = Array.ofDim[Container](1)
    inputs(0) = color
    val outputs = Array.ofDim[Container](1)
    val skySurface = SurfaceOutput(inputs, outputs)
    outputs(0) = Container(skySurface, null)
    return skySurface
  }

  def newEmission(color: Container): Emission = {
    val emissionInput = Array.ofDim[Container](1)
    emissionInput(0) = color
    val emissionOut = Array.ofDim[Container](1)
    val emission = Emission(emissionInput, emissionOut)
    emissionOut(0) = Container(emission, null)
    return emission
  }

  /* The Diffuse shader's inputs are:
    1. Color
    2. Roughness
    3. Number of rays scattered
    4. Normal vector
   */
  def newDiffuse(color: Container, roughness: Container, numberOfRays: Container, normal: Container): Diffuse = {
    val diffuseInput = Array.ofDim[Container](4)
    diffuseInput(0) = color
    diffuseInput(1) = roughness
    diffuseInput(2) = numberOfRays
    diffuseInput(3) = normal
    val diffuseOut = Array.ofDim[Container](1)
    val diffuse = Diffuse(diffuseInput, diffuseOut)
    diffuseOut(0) = Container(diffuse, lightweight.nodes.Color(0, 0, 0))
    return diffuse
  }

  /* The Glossy shader's inputs are:
    1. Color
    2. Roughness
    3. Number of rays scattered
    4. Normal vector
   */
  def newGlossy(color: Container, roughness: Container, numberOfRays: Container, normal: Container): Glossy = {
    val glossyInput = Array.ofDim[Container](4)
    glossyInput(0) = color
    glossyInput(1) = roughness
    glossyInput(2) = numberOfRays
    glossyInput(3) = normal
    val glossyOut = Array.ofDim[Container](1)
    val glossy = Glossy(glossyInput, glossyOut)
    glossyOut(0) = Container(glossy, lightweight.nodes.Color(0, 0, 0))
    return glossy
  }

  def newGetUV(uvMap: UVMap): GetUV = {
    val getUVInput = Array.ofDim[Container](0)
    val getUVOut = Array.ofDim[Container](1)
    val getUV = GetUV(getUVInput, getUVOut, uvMap)
    getUVOut(0) = Container(getUV, null)
    return getUV
  }

  def newImageTexture(coordinates: Container, image: BufferedImage): ImageTexture = {
    val imageTextureInput = Array.ofDim[Container](1)
    imageTextureInput(0) = coordinates
    val imageTextureOut = Array.ofDim[Container](1)
    val imageTexture = ImageTexture(imageTextureInput, imageTextureOut, image)
    imageTextureOut(0) = Container(imageTexture, null)
    return imageTexture
  }
}
