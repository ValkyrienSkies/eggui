package com.ewoudje.eggui.components.containers

import com.ewoudje.eggui.EGGContext
import com.ewoudje.eggui.EGGPlatform
import com.ewoudje.eggui.Size
import com.ewoudje.eggui.components.*
import com.ewoudje.eggui.frontend.ChildBuilder
import com.ewoudje.eggui.frontend.EGGBuilderMarker

class FixedContainer<P: EGGPlatform<P>>(override val parent: EGGContainerParent<P>, override val childId: Int) : EGGSingleContainer<P> {
    override var child: EGGChildComponent<P>? = null
        private set
    private var childSize = Size.EMPTY

    override fun enter(context: EGGContext<P>): EGGContext<P> =
        context.new(listOf(context.position), listOf(context.size.sameOrSmaller(childSize)))

    override fun updateChildSize(size: Size) {
        this.childSize = size
    }

    override fun getChildSize(): Size = childSize

    override fun <T : EGGChildComponent<P>> setChild(child: EGGChildConstructor<P, T>): T =
        child(this, 0).apply { this@FixedContainer.child = this }
}

@EGGBuilderMarker
val <P: EGGPlatform<P>, T: EGGContainer<P>> T.fixed get() =
    ChildBuilder(this, ::FixedContainer)
