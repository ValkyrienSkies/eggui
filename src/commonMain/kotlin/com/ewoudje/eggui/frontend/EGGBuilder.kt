package com.ewoudje.eggui.frontend

import com.ewoudje.eggui.components.*

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
operator fun <C, T, T2> ChildBuilder<C, T>.get(content: ChildBuilder<C, T2>): ChildBuilder<T, T2>
        where C: EGGMultipleContainer,
              T: EGGSingleContainer,
              T2: EGGChildComponent
        = ChildBuilder(parent.addChild(constructor), content.constructor)

@EGGBuilderMarker
@JvmName("multipleContainerInvoke")
operator fun <C, T> ChildBuilder<C, T>.invoke(block: T.() -> Unit): Unit
    where C: EGGMultipleContainer,
          T: EGGChildComponent
        = parent.addChild(constructor).block()

@EGGBuilderMarker
@JvmName("singleContainerInvoke")
operator fun <C, T> ChildBuilder<C, T>.invoke(block: T.() -> Unit): Unit
        where C: EGGSingleContainer,
              T: EGGChildComponent
        = parent.setChild(constructor).block()