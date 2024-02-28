package com.ewoudje.eggui

import com.ewoudje.eggui.components.EGGChildComponent

interface EGGPass {
    fun shouldStop(pos: Pos, size: CalculatedSize, component: EGGChildComponent): Boolean = false
}
interface EGGInitPass: EGGPass

data class RenderPass(private val renderer: EGGRenderer) : EGGPass, EGGRenderer by renderer
data class BuildAssetsPass(private val assetBuilder: EGGAssets): EGGInitPass, EGGAssets by assetBuilder