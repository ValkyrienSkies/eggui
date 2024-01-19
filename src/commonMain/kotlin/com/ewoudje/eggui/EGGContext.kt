package com.ewoudje.eggui

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
}