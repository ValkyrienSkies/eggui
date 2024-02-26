package com.ewoudje.eggui.components.elements

import com.ewoudje.eggui.*
import com.ewoudje.eggui.components.*
import com.ewoudje.eggui.frontend.ChildBuilder
import com.ewoudje.eggui.frontend.EGGBuilderMarker
import java.util.UUID
import kotlin.random.Random

data class RectangleAsset(val size: CalculatedSize, val color: Int): EGGRenderAsset

class RectangleElement(override val parent: EGGContainerParent, override val childId: Int) : EGGFillingElement, EGGFixedElement {
    override val attachment: EGGElementAttachment = EGGElementAttachment()

    var color = 0

    override fun visit(context: EGGContext) {
        when (context.pass) {
            is RenderPass -> {
                val renderer = context.pass
                renderer.renderAsset(RectangleAsset(context.size, color), context.position)
            }
            else -> {}
        }
    }
}

@EGGBuilderMarker
val <T: EGGContainer> T.rectangle get() =
    ChildBuilder(this, ::RectangleElement)