package com.ewoudje.eggui.components.containers

import com.ewoudje.eggui.EGGContext
import com.ewoudje.eggui.Size
import com.ewoudje.eggui.components.*
import com.ewoudje.eggui.frontend.ChildBuilder
import com.ewoudje.eggui.frontend.EGGBuilderMarker

class FixedContainer(override val parent: EGGContainerParent, override val childId: Int) : EGGSingleContainer {
    override var child: EGGChildComponent? = null
        private set
    private var childSize = Size.FILL

    override fun enter(context: EGGContext): EGGContext =
        context.new(listOf(context.position), listOf(context.size.sameOrSmaller(childSize)))

    override fun updateChildSize(size: Size) {
        this.childSize = size
    }

    override fun getChildSize(): Size = childSize

    override fun <T : EGGChildComponent> setChild(child: EGGChildConstructor<T>): T =
        child(this, 0).apply { this@FixedContainer.child = this }
}

@EGGBuilderMarker
val <T: EGGContainer> T.fixed get() =
    ChildBuilder(this, ::FixedContainer)
