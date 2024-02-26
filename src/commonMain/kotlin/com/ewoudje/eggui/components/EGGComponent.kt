package com.ewoudje.eggui.components

import com.ewoudje.eggui.*
import com.ewoudje.eggui.frontend.EGGBuilderMarker

@EGGBuilderMarker
sealed interface EGGComponent{
    val attachment: EGGComponentAttachment
}

sealed interface EGGComponentAttachment
open class EGGContainerAttachment(
    var onEnter: (EGGPass) -> Boolean = {false},
    var onExit: (EGGPass) -> Boolean = {false}
): EGGComponentAttachment

open class EGGElementAttachment(
    var onVisit: (EGGPass) -> Boolean = {false}
): EGGComponentAttachment

sealed interface EGGChildComponent : EGGComponent {
    val parent: EGGContainerParent
    val childId: Int

    var size get() = parent.getSize(childId)
        set(value) = parent.updateSize(childId, value)

    var width get() = size.width
        set(value) { size = Size(value, size.height) }

    var height get() = size.height
        set(value) { size = Size(size.width, value) }

    val EGGContext.position get() = getPosition(childId)
    val EGGContext.size get() = getSize(childId)
}

typealias EGGChildConstructor<T> = (EGGContainerParent, Int) -> T

sealed interface EGGElement : EGGChildComponent {
    override val attachment: EGGElementAttachment

    fun visit(context: EGGContext) {}
}

interface EGGFillingElement : EGGElement
interface EGGFixedElement : EGGElement

sealed interface EGGContainerParent : EGGComponent {
    fun updateSize(child: Int, size: Size)
    fun getSize(child: Int): Size
}

sealed interface EGGContainer : EGGChildComponent, EGGContainerParent {
    override val attachment: EGGContainerAttachment

    fun enter(context: EGGContext): EGGContext
    fun exit(context: EGGContext) {}
}

interface EGGMultipleContainer : EGGContainer {
    val children: Iterable<EGGChildComponent>

    fun <T: EGGChildComponent> addChild(child: EGGChildConstructor<T>): T
}

interface EGGSingleContainer : EGGContainer {
    val child: EGGChildComponent?

    override fun updateSize(child: Int, size: Size) {
        if (child != 0) throw IndexOutOfBoundsException("SingleContainer can only have one child")
        updateChildSize(size)
    }

    override fun getSize(child: Int): Size {
        if (child != 0) throw IndexOutOfBoundsException("SingleContainer can only have one child")
        return getChildSize()
    }

    fun <T: EGGChildComponent> setChild(child: EGGChildConstructor<T>): T
    fun updateChildSize(size: Size)
    fun getChildSize(): Size
}

class RootContainer : EGGContainerParent {
    override val attachment = EGGContainerAttachment()
    var child: EGGChildComponent? = null
    var size = Size.EMPTY

    fun <T:  EGGChildComponent> setChild(child: EGGChildConstructor<T>): T {
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