package com.ewoudje.eggui

class EGGContext<P: EGGPlatform<P>> (
    private val platform: P,
    private val positions: List<Pos>,
    private val sizes: List<CalculatedSize>,
    val pass: EGGPass<P>
) : EGGPlatform<P> by platform {

    fun getPosition(childId: Int): Pos {
        return positions[childId]
    }

    fun getSize(childId: Int): CalculatedSize {
        return sizes[childId]
    }

    fun new(positions: List<Pos>, sizes: List<CalculatedSize>): EGGContext<P> {
        return EGGContext(platform, positions, sizes, pass)
    }
}