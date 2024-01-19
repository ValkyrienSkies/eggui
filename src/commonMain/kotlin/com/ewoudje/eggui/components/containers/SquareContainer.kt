package com.ewoudje.eggui.components.containers

import com.ewoudje.eggui.*
import com.ewoudje.eggui.components.*
import com.ewoudje.eggui.frontend.ChildBuilder
import com.ewoudje.eggui.frontend.EGGBuilderMarker

class SquareContainer<P: EGGPlatform<P>>(
    override val parent: EGGContainerParent<P>,
    override val childId: Int
) : EGGSingleContainer<P> {

    override var child: EGGChildComponent<P>? = null
        private set
    private var childSize = Size.EMPTY

    override fun updateChildSize(size: Size) {
        this.childSize = size
        val biggest = this.childSize.width.biggest(this.childSize.height)
        this.size = Size(biggest, biggest)
    }

    override fun getChildSize(): Size = childSize
    override fun <T : EGGChildComponent<P>> setChild(child: EGGChildConstructor<P, T>): T {
        return child(this, 0).also { this.child = it }
    }

    override fun enter(context: EGGContext<P>): EGGContext<P> {
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
val <P: EGGPlatform<P>, T: EGGContainer<P>> T.square get() =
    ChildBuilder(this, ::SquareContainer)
