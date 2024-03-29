package com.ewoudje.eggui.components.containers

import com.ewoudje.eggui.*
import com.ewoudje.eggui.SizeElement.Companion.maxSize
import com.ewoudje.eggui.SizeElement.Companion.sumSize
import com.ewoudje.eggui.components.*
import com.ewoudje.eggui.frontend.ChildBuilder
import com.ewoudje.eggui.frontend.EGGBuilderMarker

class HorizontalContainer(
    override val parent: EGGContainerParent,
    override val childId: Int
) : EGGMultipleContainer {
    override val attachment: EGGContainerAttachment = EGGContainerAttachment()
    override val children = mutableListOf<EGGChildComponent>()
    private val sizes = mutableListOf<Size>()

    override fun <T : EGGChildComponent> addChild(child: EGGChildConstructor<T>): T {
        sizes.add(Size.FILL)
        return child(this, children.size).apply(children::add)
    }

    override fun enter(context: EGGContext): EGGContext {
        val pos = context.position
        val size = context.size

        val positionsX = SizeElement.combine(sizes.map { it.width }, size.width).map { it + pos.x }
        val positions = positionsX.map { Pos(it, pos.y) }

        val sizes = mutableListOf<CalculatedSize>()
        for (i in positionsX.indices) {
            val next = positionsX.getOrNull(i + 1) ?: (size.width + pos.x)
            sizes += CalculatedSize(
                next - positionsX[i],
                this.sizes[i].height.smallest(size.height)
            )
        }

        return context.new(positions, sizes)
    }

    override fun updateSize(child: Int, size: Size) {
        if (size == sizes[child]) return
        sizes[child] = size

        calculateSize()
    }

    override fun getSize(child: Int): Size {
        return sizes[child]
    }

    private fun calculateSize() {
        val maxHeight = sizes.map { it.height }.maxSize()
        val width = sizes.map { it.width }.sumSize()

        size = Size(width, maxHeight)
    }
}

@EGGBuilderMarker
val <T: EGGContainer> T.horizontal get() =
    ChildBuilder(this, ::HorizontalContainer)