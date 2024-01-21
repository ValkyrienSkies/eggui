package com.ewoudje.eggui.components.containers

import com.ewoudje.eggui.*
import com.ewoudje.eggui.components.*
import com.ewoudje.eggui.frontend.ChildBuilder
import com.ewoudje.eggui.frontend.EGGBuilderMarker

class SquareContainer(
    override val parent: EGGContainerParent,
    override val childId: Int
) : EGGSingleContainer {

    override var child: EGGChildComponent? = null
        private set
    private var childSize = Size.FILL

    override fun updateChildSize(size: Size) {
        this.childSize = size
        val biggest = this.childSize.width.biggest(this.childSize.height)
        this.size = Size(biggest, biggest)
    }

    override fun getChildSize(): Size = childSize
    override fun <T : EGGChildComponent> setChild(child: EGGChildConstructor<T>): T {
        return child(this, 0).also { this.child = it }
    }

    override fun enter(context: EGGContext): EGGContext {
        var size = context.size
        if (size.width > size.height) {
            size = CalculatedSize(size.height, size.height)
        } else if (size.height > size.width) {
            size = CalculatedSize(size.width, size.width)
        }

        val position = context.position + ((context.size - size) / 2f)
        return context.new(listOf(position), listOf(size))
    }
}

@EGGBuilderMarker
val <T: EGGContainer> T.square get() =
    ChildBuilder(this, ::SquareContainer)
