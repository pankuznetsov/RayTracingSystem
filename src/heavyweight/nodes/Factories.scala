package heavyweight.nodes

import java.awt.image.BufferedImage

import lightweight.geometry.{UVMap, Vector3D}
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

  def newLampOutput(color: Container): LampOutput = {
    val inputs = Array.ofDim[Container](1)
    inputs(0) = color
    val outputs = Array.ofDim[Container](1)
    val lamp = LampOutput(inputs, outputs)
    outputs(0) = Container(lamp, null)
    return lamp
  }

  def newSkySurface(color: Container): SurfaceOutput = {
    val inputs = Array.ofDim[Container](1)
    inputs(0) = color
    val outputs = Array.ofDim[Container](1)
    val skySurface = SurfaceOutput(inputs, outputs)
    outputs(0) = Container(skySurface, null)
    return skySurface
  }

  def newEmission(color: Container, number: Container): Emission = {
    val emissionInput = Array.ofDim[Container](2)
    emissionInput(0) = color
    emissionInput(1) = number
    val emissionOut = Array.ofDim[Container](1)
    val emission = Emission(emissionInput, emissionOut)
    emissionOut(0) = Container(emission, null)
    return emission
  }

  def newDiffuse(color: Container, roughness: Container, numberOfRays: Container, normal: Container, lampIlluminationOutput: Container): Diffuse = {
    val diffuseInput = Array.ofDim[Container](5)
    diffuseInput(0) = color
    diffuseInput(1) = roughness
    diffuseInput(2) = numberOfRays
    diffuseInput(3) = normal
    diffuseInput(4) = lampIlluminationOutput
    val diffuseOut = Array.ofDim[Container](1)
    val diffuse = Diffuse(diffuseInput, diffuseOut)
    diffuseOut(0) = Container(diffuse, lightweight.nodes.Color(0, 0, 0))
    return diffuse
  }

  def newGlossy(color: Container, roughness: Container, numberOfRays: Container, normal: Container, lampIlluminationOutput: Container): Glossy = {
    val glossyInput = Array.ofDim[Container](5)
    glossyInput(0) = color
    glossyInput(1) = roughness
    glossyInput(2) = numberOfRays
    glossyInput(3) = normal
    glossyInput(4) = lampIlluminationOutput
    val glossyOut = Array.ofDim[Container](1)
    val glossy = Glossy(glossyInput, glossyOut)
    glossyOut(0) = Container(glossy, lightweight.nodes.Color(0, 0, 0))
    return glossy
  }

  def newTransparent(color: Container, roughness: Container, numberOfRays: Container, normal: Container, lampIlluminationOutput: Container): Transparent = {
    val transparentInput = Array.ofDim[Container](5)
    transparentInput(0) = color
    transparentInput(1) = roughness
    transparentInput(2) = numberOfRays
    transparentInput(3) = normal
    transparentInput(4) = lampIlluminationOutput
    val transparentOut = Array.ofDim[Container](1)
    val transparent = Transparent(transparentInput, transparentOut)
    transparentOut(0) = Container(transparent, lightweight.nodes.Color(0, 0, 0))
    return transparent
  }

  def newTranslucent(color: Container, roughness: Container, numberOfRays: Container, normal: Container, lampIlluminationOutput: Container): Translucent = {
    val translucentInput = Array.ofDim[Container](5)
    translucentInput(0) = color
    translucentInput(1) = roughness
    translucentInput(2) = numberOfRays
    translucentInput(3) = normal
    translucentInput(4) = lampIlluminationOutput
    val translucentOut = Array.ofDim[Container](1)
    val translucent = Translucent(translucentInput, translucentOut)
    translucentOut(0) = Container(translucent, lightweight.nodes.Color(0, 0, 0))
    return translucent
  }

  def newGeometry(): Geometry = {
    val geometryInput = Array.ofDim[Container](0)
    val geometryOut = Array.ofDim[Container](5)
    val geometry = Geometry(geometryInput, geometryOut)
    geometryOut(0) = Container(geometry, null)
    geometryOut(1) = Container(geometry, null)
    geometryOut(2) = Container(geometry, null)
    geometryOut(3) = Container(geometry, null)
    geometryOut(4) = Container(geometry, null)
    return geometry
  }

  def newLampIllumination(): LampIllumination = {
    val lampIlluminationInput = Array.ofDim[Container](0)
    val lampIlluminationOut = Array.ofDim[Container](5)
    val lampIllumination = LampIllumination(lampIlluminationInput, lampIlluminationOut)
    lampIlluminationOut(0) = Container(lampIllumination, null)
    lampIlluminationOut(1) = Container(lampIllumination, null)
    lampIlluminationOut(2) = Container(lampIllumination, null)
    lampIlluminationOut(3) = Container(lampIllumination, null)
    lampIlluminationOut(4) = Container(lampIllumination, null)
    return lampIllumination
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

  def newSpectrate(input: Container): Spectrate = {
    val spectrateInput = Array.ofDim[Container](1)
    val spectrateOut = Array.ofDim[Container](3)
    val spectrate = Spectrate(spectrateInput, spectrateOut)
    spectrateInput(0) = input
    spectrate
  }

  def newCombineRGB(inputZero: Container, inputOne: Container, inputTwo: Container): CombineRGB = {
    val combineRGBInput = Array.ofDim[Container](3)
    val combineRGBOut = Array.ofDim[Container](1)
    val combineRGB = CombineRGB(combineRGBInput, combineRGBOut)
    combineRGBInput(0) = inputZero
    combineRGBInput(0) = inputOne
    combineRGBInput(0) = inputTwo
    combineRGB
  }

  def newInvertColor(inputZero: Container): InvertColor = {
    val invertColorInput = Array.ofDim[Container](1)
    val invertColorOut = Array.ofDim[Container](1)
    val invertColor = InvertColor(invertColorInput, invertColorOut)
    invertColorInput(0) = inputZero
    invertColor
  }

  val MATH_ADDITION: Int = 0
  val MATH_SUBSTRACTION: Int = 1
  val MATH_MULTIPLICATION: Int = 2
  val MATH_DIVISION: Int = 3
  val MATH_MAX: Int = 4
  val MATH_MIN: Int = 5

  def newBinaryMath(inputZero: Container, inputOne: Container, optionIndex: Int): BinaryMath = {
    val binaryMathInput = Array.ofDim[Container](2)
    val binaryMathOut = Array.ofDim[Container](1)
    val binaryMath = BinaryMath(binaryMathInput, binaryMathOut, optionIndex)
    binaryMathInput(0) = inputZero
    binaryMathInput(1) = inputOne
    binaryMath
  }

  val VECTOR_MATH_ADDITION: Int = 0
  val VECTOR_MATH_SUBSTRACTION: Int = 1
  val VECTOR_MATH_MULTIPLICATION: Int = 2
  val VECTOR_MATH_DIVISION: Int = 3
  val VECTOR_MATH_MAX: Int = 4
  val VECTOR_MATH_MIN: Int = 5

  def newVectorMath(inputZero: Container, inputOne: Container, optionIndex: Int): BinaryMath = {
    val vectorMathInput = Array.ofDim[Container](2)
    val vectorMathOut = Array.ofDim[Container](1)
    val vectorMath = VectorMath(vectorMathInput, vectorMathOut, optionIndex)
    vectorMathInput(0) = inputZero
    vectorMathInput(1) = inputOne
    vectorMath
  }
}
