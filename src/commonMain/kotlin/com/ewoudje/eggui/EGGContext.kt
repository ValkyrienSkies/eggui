package com.ewoudje.eggui

import com.ewoudje.eggui.components.EGGComponent
import com.ewoudje.eggui.components.EGGContainer
import com.ewoudje.eggui.components.EGGElement
import com.ewoudje.eggui.frontend.TraverseException
import com.ewoudje.eggui.frontend.traverseComponent

class EGGContext (
    private val positions: List<Pos>,
    private val sizes: List<CalculatedSize>,
    val pass: EGGPass
) {

    fun getPosition(childId: Int): Pos {
        return positions[childId]
    }

    fun getSize(childId: Int): CalculatedSize {
        return sizes[childId]
    }

    fun new(positions: List<Pos>, sizes: List<CalculatedSize>): EGGContext {
        return EGGContext(positions, sizes, pass)
    }

    /**
     * @throws TraverseException
     */
    companion object {
        fun traverse(component: EGGComponent, context: EGGContext) = traverseComponent(component, context)
    }
}