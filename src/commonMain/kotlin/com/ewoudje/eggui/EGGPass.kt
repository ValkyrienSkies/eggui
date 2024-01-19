package com.ewoudje.eggui

interface EGGPass {
    data class Render(private val renderer: EGGRenderer) : EGGPass, EGGRenderer by renderer
    data class BuildAssets(private val assetBuilder: EGGAssets): EGGPass, EGGAssets by assetBuilder
}