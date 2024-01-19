package com.ewoudje.eggui.components

import com.ewoudje.eggui.*
import com.ewoudje.eggui.frontend.EGGBuilderMarker

@EGGBuilderMarker
sealed interface EGGComponent<P: EGGPlatform<P>>

sealed interface EGGChildComponent<P: EGGPlatform<P>> : EGGComponent<P> {
    val parent: EGGContainerParent<P>
    val childId: Int

    var size get() = parent.getSize(childId)
        set(value) = parent.updateSize(childId, value)

    var width get() = size.width
        set(value) { size = Size(value, size.height) }

    var height get() = size.height
        set(value) { size = Size(size.width, value) }

    fun scaleVertical(priority: Float = 1.0f) {
        size = Size(size.width, SizeElement.Fill(priority))
    }

    fun scaleHorizontal(priority: Float = 1.0f) {
        size = Size(SizeElement.Fill(priority), size.height)
    }

    val EGGContext<P>.position get() = getPosition(childId)
    val EGGContext<P>.size get() = getSize(childId)
}

typealias EGGChildConstructor<P, T> = (EGGContainerParent<P>, Int) -> T

sealed interface EGGElement<P: EGGPlatform<P>> : EGGChildComponent<P> {
    fun visit(context: EGGContext<P>) {}
}

interface EGGFillingElement<P: EGGPlatform<P>> : EGGElement<P>
interface EGGFixedElement<P: EGGPlatform<P>> : EGGElement<P>

sealed interface EGGContainerParent<P: EGGPlatform<P>> : EGGComponent<P> {
    fun updateSize(child: Int, size: Size)
    fun getSize(child: Int): Size
}

sealed interface EGGContainer<P: EGGPlatform<P>> : EGGChildComponent<P>, EGGContainerParent<P> {
    fun enter(context: EGGContext<P>): EGGContext<P>
    fun exit(context: EGGContext<P>) {}
}

interface EGGMultipleContainer<P: EGGPlatform<P>> : EGGContainer<P> {
    val children: Iterable<EGGChildComponent<P>>

    fun <T: EGGChildComponent<P>> addChild(child: EGGChildConstructor<P, T>): T
}

interface EGGSingleContainer<P: EGGPlatform<P>> : EGGContainer<P> {
    val child: EGGChildComponent<P>?

    override fun updateSize(child: Int, size: Size) {
        if (child != 0) throw IndexOutOfBoundsException("SingleContainer can only have one child")
        updateChildSize(size)
    }

    override fun getSize(child: Int): Size {
        if (child != 0) throw IndexOutOfBoundsException("SingleContainer can only have one child")
        return getChildSize()
    }

    fun <T: EGGChildComponent<P>> setChild(child: EGGChildConstructor<P, T>): T
    fun updateChildSize(size: Size)
    fun getChildSize(): Size
}

class RootContainer<P: EGGPlatform<P>> : EGGContainerParent<P> {
    var child: EGGChildComponent<P>? = null
    var size = Size.EMPTY

    fun <T:  EGGChildComponent<P>> setChild(child: EGGChildConstructor<P, T>): T {
        if (this.child != null) throw IllegalStateException("RootContainer can only have one child")
        return child(this, 0).apply { this@RootContainer.child = this }
    }

    override fun updateSize(child: Int, size: Size) {
        if (size == this.size) return
        this.size = size
    }

    override fun getSize(child: Int): Size {
        return size
    }
}