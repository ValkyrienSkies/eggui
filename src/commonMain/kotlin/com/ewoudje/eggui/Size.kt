package com.ewoudje.eggui

sealed interface SizeElement {

    data class Fill(val priority: Float = 1f) : SizeElement
    data class Fixed(val value: Float) : SizeElement
    data class Additional(val size: SizeElement, val extra: Float) : SizeElement

    fun expand(extra: Float): SizeElement = Additional(this, extra)
    fun asFloat(): Float? = when (this) {
        is Fixed -> value
        is Additional -> size.asFloat()?.plus(extra)
        is Fill -> null
    }

    fun smallest(value: Float): Float {
        val f = asFloat() ?: return value
        return if (f < value) f else value
    }

    fun biggest(value: Float): Float? {
        val f = asFloat() ?: return null
        return if (f > value) f else value
    }

    fun biggest(value: SizeElement): SizeElement {
        val f1 = asFloat() ?: return this
        val f2 = value.asFloat() ?: return value
        return if (f1 > f2) this else value
    }

    operator fun div(value: Float) = this.asFloat()?.div(value)?.let(::Fixed) ?: this
    operator fun times(value: Float) = this.asFloat()?.times(value)?.let(::Fixed) ?: this

    companion object {
        val ZERO = Fixed(0f)

        operator fun invoke(value: Float) = Fixed(value)

        fun combine(vararg elements: SizeElement, max: Float): List<Float> = combine(elements.toList(), max)
        fun combine(elements: List<SizeElement>, max: Float): List<Float> {
            val fillList = mutableListOf<Pair<Int, Float>>()
            var lastX = 0f

            fun traverse(index: Int, element: SizeElement): Float = when (element) {
                is Additional -> traverse(index, element.size) + element.extra
                is Fill -> {
                    fillList.add(index to element.priority); 0f
                }
                is Fixed -> element.value
            }

            val result = FloatArray(elements.size)
            for (i in elements.indices) {
                result[i] = lastX
                lastX += traverse(i, elements[i])
            }

            val sizeLeft = max - lastX
            if (sizeLeft < -0.00001f) throw IllegalArgumentException("$elements combined is bigger than $max")
            if (fillList.isEmpty() || sizeLeft < 0.00001f) return result.toList()

            val fillSum = fillList.sumOf { it.second.toDouble() }.toFloat()
            var shift = 0f

            for (i in elements.indices) {
                result[i] += shift
                fillList.find { it.first == i }?.let {
                    val size = (it.second / fillSum) * sizeLeft
                    shift += size
                }
            }

            return result.toList()
        }

        fun List<SizeElement>.maxSize(): SizeElement =
            SizeElement(fold(0f) { acc, sizeElement ->
                val f = sizeElement.asFloat() ?: return@maxSize Fill()
                if (f > acc) f else acc
            })



        fun List<SizeElement>.sumSize(): SizeElement =
            SizeElement(fold(0f) { acc, sizeElement ->
                acc + (sizeElement.asFloat() ?: return@sumSize Fill())
            })

    }
}

data class Size(val width: SizeElement, val height: SizeElement) {
    inline fun map(map: (SizeElement) -> SizeElement) = Size(map(width), map(height))

    companion object {
        val EMPTY = Size(SizeElement.ZERO, SizeElement.ZERO)
        val FILL = Size(SizeElement.Fill(), SizeElement.Fill())

        operator fun invoke(width: Float, height: Float) = Size(SizeElement(width), SizeElement(height))
        operator fun invoke(width: Float, height: SizeElement) = Size(SizeElement(width), height)
        operator fun invoke(width: SizeElement, height: Float) = Size(width, SizeElement(height))
    }
}

