package com.ewoudje.eggui


data class Pos(val x: Float, val y: Float) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
    operator fun minus(other: Pos): Pos = Pos(x - other.x, y - other.y)
    operator fun plus(other: CalculatedSize): Pos = Pos(x + other.width, y + other.height)
    operator fun minus(other: CalculatedSize): Pos = Pos(x - other.width, y - other.height)

    companion object {
        val ZERO = Pos(0f, 0f)
        val ONE = Pos(1f, 1f)
    }
}

data class CalculatedSize(val width: Float, val height: Float) {
    operator fun plus(other: Pos): CalculatedSize = CalculatedSize(width + other.x, height + other.y)
    operator fun minus(other: Pos): CalculatedSize = CalculatedSize(width - other.x, height - other.y)
    operator fun plus(other: CalculatedSize): CalculatedSize = CalculatedSize(width + other.width, height + other.height)
    operator fun minus(other: CalculatedSize): CalculatedSize = CalculatedSize(width - other.width, height - other.height)
    operator fun div(other: Float): CalculatedSize = CalculatedSize(width / other, height / other)
    operator fun times(other: Float): CalculatedSize = CalculatedSize(width * other, height * other)

    fun sameOrSmaller(other: Size): CalculatedSize = CalculatedSize(
        other.width.smallest(width),
        other.height.smallest(height)
    )

    companion object {
        val ZERO = CalculatedSize(0f, 0f)
        val ONE = CalculatedSize(1f, 1f)

        operator fun invoke(from: Pos, to: Pos) = CalculatedSize(to.x - from.x, to.y - from.y)
    }
}

