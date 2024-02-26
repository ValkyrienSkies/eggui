package com.ewoudje.eggui.components.containers

import com.ewoudje.eggui.*
import com.ewoudje.eggui.components.*
import com.ewoudje.eggui.frontend.ChildBuilder
import com.ewoudje.eggui.frontend.EGGBuilderMarker
import com.ewoudje.eggui.frontend.invoke
import kotlin.reflect.KProperty

class StatefulContainer(
    val block: StatefulContainer.() -> Unit,
    override val parent: EGGContainerParent,
    override val childId: Int,
): EGGSingleContainer {
    override val attachment: EGGContainerAttachment = EGGContainerAttachment()
    override var child: EGGChildComponent? = null
        private set
    private var childSize = Size.FILL
    private val map = mutableMapOf<KProperty<*>, Any?>()
    private val inits = mutableListOf<EGGInitPass>()
    private var dirty = false

    override fun <T : EGGChildComponent> setChild(child: EGGChildConstructor<T>): T =
        child(this, 0).also { this.child = it }

    override fun updateChildSize(size: Size) {
        this.childSize = size
    }

    override fun getChildSize(): Size = childSize

    override fun enter(context: EGGContext): EGGContext {
        if (dirty) updateChild(context.position, context.size)
        return context.new(listOf(context.position), listOf(context.size))
    }

    fun <T> newState(v: T) = StatePreparer(v, this)

    private fun updateChild(pos: Pos, size: CalculatedSize) {
        child = null
        block()

        child?.let {
            for (pass in inits) {
                EGGContext.traverse(it, EGGContext(listOf(pos), listOf(size), pass))
            }
        }
        dirty = false
    }

    class StatePreparer<T>(private val defaultVal: T, private val container: StatefulContainer) {
        operator fun provideDelegate(
            thisRef: Any?,
            prop: KProperty<*>
        ): State<T> {
            val value = (container.map[prop] ?: defaultVal) as T
            return State(value, container)
        }
    }

    class State<T>(
        private var value: T,
        private val container: StatefulContainer
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            this.value = value
            container.map[property] = value
            container.dirty = true
        }
    }
}



@EGGBuilderMarker
fun <T: EGGContainer> T.stateful(block: StatefulContainer.() -> Unit): Unit =
    ChildBuilder(this) { parent, id -> StatefulContainer(block, parent, id) }.invoke { block() }