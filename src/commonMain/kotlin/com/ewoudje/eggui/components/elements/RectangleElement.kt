package com.ewoudje.eggui.components.elements

import com.ewoudje.eggui.*
import com.ewoudje.eggui.components.EGGContainer
import com.ewoudje.eggui.components.EGGContainerParent
import com.ewoudje.eggui.components.EGGFillingElement
import com.ewoudje.eggui.components.EGGFixedElement
import com.ewoudje.eggui.frontend.ChildBuilder
import com.ewoudje.eggui.frontend.EGGBuilderMarker
import java.util.UUID
import kotlin.random.Random

data class RectangleDescriptor(val size: CalculatedSize, val color: Int): EGGRenderAssetDescriptor

class RectangleElement<P: EGGPlatform<P>>(override val parent: EGGContainerParent<P>, override val childId: Int) : EGGFillingElement<P>, EGGFixedElement<P> {
    var color = 0

    override fun visit(context: EGGContext<P>) {
        when (context.pass) {
            is EGGPass.Render -> {
                val renderer = context.pass
                val asset = context.getAsset(RectangleDescriptor(context.size, color))
                renderer.renderAsset(asset, context.position)
            }
            else -> {}
        }
    }
}

@EGGBuilderMarker
val <P: EGGPlatform<P>, T: EGGContainer<P>> T.rectangle get() =
    ChildBuilder(this, ::RectangleElement)

@EGGBuilderMarker
fun <P: EGGPlatform<P>, T: EGGContainer<P>> T.rectangle(color: Int = Random.nextInt()) =
    ChildBuilder(this, ::RectangleElement).wrap { it.color = color }