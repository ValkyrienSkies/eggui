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

class RectangleElement(override val parent: EGGContainerParent, override val childId: Int) : EGGFillingElement, EGGFixedElement {
    var color = 0

    override fun visit(context: EGGContext) {
        when (context.pass) {
            is EGGPass.Render -> {
                val renderer = context.pass
                renderer.renderAsset(RectangleDescriptor(context.size, color), context.position)
            }
            else -> {}
        }
    }
}

@EGGBuilderMarker
val <T: EGGContainer> T.rectangle get() =
    ChildBuilder(this, ::RectangleElement)

@EGGBuilderMarker
fun <T: EGGContainer> T.rectangle(color: Int = Random.nextInt()) =
    ChildBuilder(this, ::RectangleElement).wrap { it.color = color }