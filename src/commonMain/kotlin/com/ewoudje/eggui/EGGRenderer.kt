package com.ewoudje.eggui


interface EGGRenderer<P: EGGPlatform<P>> {
    fun renderAsset(asset: EGGAsset<P, out EGGRenderAssetDescriptor>, pos: Pos)
}