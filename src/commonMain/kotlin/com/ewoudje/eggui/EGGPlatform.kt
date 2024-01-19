package com.ewoudje.eggui

import com.ewoudje.eggui.components.*


interface EGGPlatform<P: EGGPlatform<P>> {
    fun <T: EGGAssetDescriptor> getAsset(assetDescriptor: T): EGGAsset<P, T>

    fun traverse(comp: EGGComponent<P>, ctx: EGGContext<P>) {
        when (comp) {
            is EGGMultipleContainer<P> -> {
                val newCtx = comp.enter(ctx)
                comp.children.forEach { traverse(it, newCtx) }
                comp.exit(newCtx)
            }
            is EGGSingleContainer<P> -> {
                val newCtx = comp.enter(ctx)
                comp.child?.let { traverse(it, newCtx) }
                comp.exit(newCtx)
            }
            is EGGElement<P> -> {
                comp.visit(ctx)
            }
            is RootContainer<P> -> {
                comp.child?.let { traverse(it, ctx) }
            }
        }
    }
}