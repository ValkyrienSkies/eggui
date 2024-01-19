package com.ewoudje.eggui

interface EGGAsset
interface EGGRenderAsset: EGGAsset
interface AssetDescriptor<T: EGGAsset>

interface EGGAssets {
    operator fun <T: EGGAsset> get(location: AssetDescriptor<T>): T
}