package com.ewoudje.eggui.frontend

import com.ewoudje.eggui.*
import com.ewoudje.eggui.components.*
import com.ewoudje.eggui.components.containers.*

@DslMarker
annotation class EGGBuilderMarker

@EGGBuilderMarker
class ChildBuilder<P: EGGPlatform<P>, C: EGGContainer<P>, T: EGGComponent<P>>(
    val parent: C,
    val constructor: EGGChildConstructor<P, T>
) {
    fun wrap(wrap: (T) -> Unit): ChildBuilder<P,C,T> =
        ChildBuilder(parent) { p, i -> constructor(p, i).apply(wrap) }
}

@EGGBuilderMarker
operator fun <P, C, T, T2> ChildBuilder<P, C, T>.rem(content: ChildBuilder<P, C, T2>): ChildBuilder<P, T, T2>
        where P: EGGPlatform<P>,
              C: EGGMultipleContainer<P>,
              T: EGGSingleContainer<P>,
              T2: EGGChildComponent<P>
        = ChildBuilder(parent.addChild(constructor), content.constructor)

@EGGBuilderMarker
@JvmName("multipleContainer")
operator fun <P, C, T> ChildBuilder<P, C, T>.invoke(block: T.() -> Unit): Unit
    where P: EGGPlatform<P>,
          C: EGGMultipleContainer<P>,
          T: EGGChildComponent<P>
        = parent.addChild(constructor).block()

@EGGBuilderMarker
@JvmName("singleContainer")
operator fun <P, C, T> ChildBuilder<P, C, T>.invoke(block: T.() -> Unit): Unit
        where P: EGGPlatform<P>,
              C: EGGSingleContainer<P>,
              T: EGGChildComponent<P>
        = parent.setChild(constructor).block()

@EGGBuilderMarker
infix fun <P, C, T> ChildBuilder<P, C, T>.size(size: Size): ChildBuilder<P, C, T>
        where P: EGGPlatform<P>,
              C: EGGContainer<P>,
              T: EGGChildComponent<P>
        = wrap { it.size = size }