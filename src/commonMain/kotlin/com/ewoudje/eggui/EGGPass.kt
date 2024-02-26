package com.ewoudje.eggui

interface EGGPass
interface EGGInitPass: EGGPass

data class RenderPass(private val renderer: EGGRenderer) : EGGPass, EGGRenderer by renderer
data class BuildAssetsPass(private val assetBuilder: EGGAssets): EGGInitPass, EGGAssets by assetBuilder