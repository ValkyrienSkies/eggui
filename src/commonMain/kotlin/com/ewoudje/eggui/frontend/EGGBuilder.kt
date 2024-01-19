package com.ewoudje.eggui.frontend

import com.ewoudje.eggui.*
import com.ewoudje.eggui.components.*
import com.ewoudje.eggui.components.containers.*
import javax.swing.text.html.HTML.Tag.P

@DslMarker
annotation class EGGBuilderMarker

@EGGBuilderMarker
class ChildBuilder< C: EGGContainer, T: EGGComponent>(
    val parent: C,
    val constructor: EGGChildConstructor<T>
) {
    fun wrap(wrap: (T) -> Unit): ChildBuilder<C,T> =
        ChildBuilder(parent) { p, i -> constructor(p, i).apply(wrap) }
}

@EGGBuilderMarker
operator fun <C, T, T2> ChildBuilder<C, T>.rem(content: ChildBuilder<C, T2>): ChildBuilder<T, T2>
        where C: EGGMultipleContainer,
              T: EGGSingleContainer,
              T2: EGGChildComponent
        = ChildBuilder(parent.addChild(constructor), content.constructor)

@EGGBuilderMarker
@JvmName("multipleContainer")
operator fun <C, T> ChildBuilder<C, T>.invoke(block: T.() -> Unit): Unit
    where C: EGGMultipleContainer,
          T: EGGChildComponent
        = parent.addChild(constructor).block()

@EGGBuilderMarker
@JvmName("singleContainer")
operator fun <C, T> ChildBuilder<C, T>.invoke(block: T.() -> Unit): Unit
        where C: EGGSingleContainer,
              T: EGGChildComponent
        = parent.setChild(constructor).block()

@EGGBuilderMarker
infix fun <C, T> ChildBuilder<C, T>.size(size: Size): ChildBuilder<C, T>
        where C: EGGContainer,
              T: EGGChildComponent
        = wrap { it.size = size }