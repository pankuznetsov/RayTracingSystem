package heavyweight.nodes

import java.awt.image.BufferedImage

import jdk.internal.util.xml.impl.Input
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

  def newRefraction(color: Container, roughness: Container, numberOfRays: Container, normal: Container, lampIlluminationOutput: Container, materialIOR: Container): Refraction = {
    val refractionInput = Array.ofDim[Container](6)
    refractionInput(0) = color
    refractionInput(1) = roughness
    refractionInput(2) = numberOfRays
    refractionInput(3) = normal
    refractionInput(4) = lampIlluminationOutput
    refractionInput(5) = materialIOR
    val refractionOut = Array.ofDim[Container](1)
    val refraction = Refraction(refractionInput, refractionOut)
    refractionOut(0) = Container(refraction, Color(0, 0, 0))
    refraction
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
    val geometryOut = Array.ofDim[Container](6)
    val geometry = Geometry(geometryInput, geometryOut)
    geometryOut(0) = Container(geometry, null)
    geometryOut(1) = Container(geometry, null)
    geometryOut(2) = Container(geometry, null)
    geometryOut(3) = Container(geometry, null)
    geometryOut(4) = Container(geometry, null)
    geometryOut(5) = Container(geometry, null)
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

  def newSpectrate(input: Container): Spectrate = {
    val spectrateInput = Array.ofDim[Container](1)
    val spectrateOut = Array.ofDim[Container](3)
    val spectrate = Spectrate(spectrateInput, spectrateOut)
    spectrateInput(0) = input
    spectrateOut(0) = Container(spectrate, null)
    spectrate
  }

  def newCombineRGB(red: Container, green: Container, blue: Container): CombineRGB = {
    val combineRGBInput = Array.ofDim[Container](3)
    val combineRGBOut = Array.ofDim[Container](1)
    val combineRGB = CombineRGB(combineRGBInput, combineRGBOut)
    combineRGBInput(0) = red
    combineRGBInput(1) = green
    combineRGBInput(2) = blue
    combineRGBOut(0) = Container(combineRGB, null)
    combineRGB
  }

  def newInvertColor(color: Container): InvertColor = {
    val invertColorInput = Array.ofDim[Container](1)
    val invertColorOut = Array.ofDim[Container](1)
    val invertColor = InvertColor(invertColorInput, invertColorOut)
    invertColorInput(0) = color
    invertColorOut(0) = Container(invertColor, null)
    invertColor
  }

  val MATH_ADDITION: Int = 0
  val MATH_SUBSTRACTION: Int = 1
  val MATH_MULTIPLICATION: Int = 2
  val MATH_DIVISION: Int = 3
  val MATH_MAX: Int = 4
  val MATH_MIN: Int = 5

  def newBinaryMath(firstComponent: Container, secondComponent: Container, optionIndex: Int): BinaryMath = {
    val binaryMathInput = Array.ofDim[Container](2)
    val binaryMathOut = Array.ofDim[Container](1)
    val binaryMath = BinaryMath(binaryMathInput, binaryMathOut, optionIndex)
    binaryMathInput(0) = firstComponent
    binaryMathInput(1) = secondComponent
    binaryMathOut(0) = Container(binaryMath, null)
    binaryMath
  }

  val VECTOR_MATH_ADDITION: Int = 0
  val VECTOR_MATH_SUBSTRACTION: Int = 1
  val VECTOR_MATH_MULTIPLICATION: Int = 2
  val VECTOR_MATH_DIVISION: Int = 3
  val VECTOR_MATH_MAX: Int = 4
  val VECTOR_MATH_MIN: Int = 5

  def newVectorMath(firstComponent: Container, secondComponent: Container, optionIndex: Int): VectorMath = {
    val vectorMathInput = Array.ofDim[Container](2)
    val vectorMathOut = Array.ofDim[Container](1)
    val vectorMath = VectorMath(vectorMathInput, vectorMathOut, optionIndex)
    vectorMathInput(0) = firstComponent
    vectorMathInput(1) = secondComponent
    vectorMathOut(0) = Container(vectorMath, null)
    vectorMath
  }

  def newColorRamp(factore: Container, interpolateTipe: Int, colorPoints: Array[(Double, lightweight.nodes.Color)]): ColorRamp = {
    val colorRampInputs = Array.ofDim[Container](1)
    val colorRampOutputs = Array.ofDim[Container](1)
    val colorRamp = ColorRamp(colorRampInputs, colorRampOutputs, colorPoints, interpolateTipe)
    colorRampInputs(0) = factore
    colorRampOutputs(0) = Container(colorRamp, null)
    colorRamp
  }

  def newImageTexture(coordinates: Container, image: BufferedImage): ImageTexture = {
    val imageTextureInput = Array.ofDim[Container](1)
    imageTextureInput(0) = coordinates
    val imageTextureOut = Array.ofDim[Container](1)
    val imageTexture = ImageTexture(imageTextureInput, imageTextureOut, image)
    imageTextureOut(0) = Container(imageTexture, null)
    return imageTexture
  }

  def newRGBToBW(color: Container): RGBToBlackWhite = {
    val rgbToBlackWhiteInput = Array.ofDim[Container](1)
    val rgbToBlackWhiteOutput = Array.ofDim[Container](2)
    rgbToBlackWhiteInput(0) = color
    val rgbToBlackWhite = RGBToBlackWhite(rgbToBlackWhiteInput, rgbToBlackWhiteOutput)
    rgbToBlackWhiteOutput(0) = Container(rgbToBlackWhite, null)
    rgbToBlackWhiteOutput(1) = Container(rgbToBlackWhite, null)
    rgbToBlackWhite
  }

  def newGetAngle(): GetAngle = {
    val getAngleInputs = Array.ofDim[Container](0)
    val getAngleOutputs = Array.ofDim[Container](2)
    val getAngle = GetAngle(getAngleInputs, getAngleOutputs)
    getAngleOutputs(0) = Container(getAngle, null)
    getAngleOutputs(1) = Container(getAngle, null)
    getAngle
  }

  def newVolumeEmission(color: Container, intensity: Container): VolumeEmission = {
    val volumeEmissionInput = Array.ofDim[Container](2)
    volumeEmissionInput(0) = color
    volumeEmissionInput(1) = intensity
    val volumeEmissionOut = Array.ofDim[Container](1)
    val volumeEmission = VolumeEmission(volumeEmissionInput, volumeEmissionOut)
    volumeEmissionOut(0) = Container(volumeEmission, null)
    return volumeEmission
  }

  def newVolumeAbsorption(color: Container, density: Container): VolumeAbsorption = {
    val volumeAbsorptionInput = Array.ofDim[Container](2)
    volumeAbsorptionInput(0) = color
    volumeAbsorptionInput(1) = density
    val volumeAbsorptionOut = Array.ofDim[Container](1)
    val volumeAbsorption = VolumeAbsorption(volumeAbsorptionInput, volumeAbsorptionOut)
    volumeAbsorptionOut(0) = Container(volumeAbsorption, null)
    return volumeAbsorption
  }

  def newRayTeleport(): RayTeleport = {
    val rayTeleportInput = Array.ofDim[Container](0)
    val rayTeleportOutput = Array.ofDim[Container](1)
    val rayTeleport = RayTeleport(rayTeleportInput, rayTeleportOutput)
    rayTeleportOutput(0) = Container(rayTeleport, null)
    rayTeleport
  }

  def newChekcerTexture(vector: Container, inputOne: Container, firstColor: Container, secondColor: Container): CheckerTexture = {
    val inputs = Array.ofDim[Container](4)
    val outputs = Array.ofDim[Container](2)
    val checkerTexture = CheckerTexture(inputs, outputs)
    inputs(0) = vector
    inputs(1) = inputOne
    inputs(2) = firstColor
    inputs(3) = secondColor
    outputs(0) = Container(checkerTexture, null)
    outputs(1) = Container(checkerTexture, null)
    checkerTexture
  }

  def newVolumeScatter(color: Container, density: Container, levelOfAnisotropic: Container, normal: Container, lampIllumination: Container, rayQuantity: Int): VolumeScatter = {
    val inputs = Array.ofDim[Container](5)
    val outputs = Array.ofDim[Container](1)
    val volumeScatter = VolumeScatter(inputs, outputs, rayQuantity)
    inputs(0) = color
    inputs(1) = density
    inputs(2) = levelOfAnisotropic
    inputs(3) = normal
    inputs(4) = lampIllumination
    outputs(0) = Container(volumeScatter, null)
    volumeScatter
  }

  def newMix(factor: Container, firstComponent: Container, secondComponent: Container): Mix = {
    val inputs = Array.ofDim[Container](3)
    val outputs = Array.ofDim[Container](1)
    inputs(0) = factor
    inputs(1) = firstComponent
    inputs(2) = secondComponent
    val mix = Mix(inputs, outputs)
    outputs(0) = Container(mix, null)
    mix
  }
}
