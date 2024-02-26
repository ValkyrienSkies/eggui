package com.ewoudje.eggui.components.containers

import com.ewoudje.eggui.*
import com.ewoudje.eggui.SizeElement.Companion.maxSize
import com.ewoudje.eggui.SizeElement.Companion.sumSize
import com.ewoudje.eggui.components.*
import com.ewoudje.eggui.frontend.ChildBuilder
import com.ewoudje.eggui.frontend.EGGBuilderMarker

class VerticalContainer(
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

        val positionsY = SizeElement.combine(sizes.map { it.height }, size.height).map { it + pos.y }
        val positions = positionsY.map { Pos(pos.x, it) }

        val sizes = mutableListOf<CalculatedSize>()
        for (i in positionsY.indices) {
            val next = positionsY.getOrNull(i + 1) ?: (size.height + pos.y)
            sizes += CalculatedSize(
                this.sizes[i].width.smallest(size.width),
                next - positionsY[i]
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
        val maxWidth = sizes.map { it.width }.maxSize()
        val height = sizes.map { it.height }.sumSize()

        size = Size(maxWidth, height)
    }
}

@EGGBuilderMarker
val <T: EGGContainer> T.vertical get() =
    ChildBuilder(this, ::VerticalContainer)
