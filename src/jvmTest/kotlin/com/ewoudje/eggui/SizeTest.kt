package com.ewoudje.eggui

import com.ewoudje.eggui.SizeElement.Companion.maxSize
import com.ewoudje.eggui.SizeElement.Companion.sumSize
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CombineSizeElementsTest : StringSpec({
    "fixed combination" {
        val fixed1 = SizeElement.Fixed(0.1f)
        val fixed2 = SizeElement.Fixed(0.9f)
        val combined = SizeElement.combine(fixed1, fixed2, max = 1f)

        combined[0] shouldBe 0f
        combined[1] shouldBe 0.1f
    }

    "fixed plus fill" {
        val fill = SizeElement.Fill()
        val fixed1 = SizeElement.Fixed(0.1f)
        val combined = SizeElement.combine(fill, fixed1, max = 1f)

        combined[0] shouldBe 0f
        combined[1] shouldBe 0.9f
    }

    "fill plus fill" {
        val fill1 = SizeElement.Fill()
        val fill2 = SizeElement.Fill()
        val combined = SizeElement.combine(fill1, fill2, max = 1f)

        combined[0] shouldBe 0f
        combined[1] shouldBe 0.5f
    }

    "fill plus fill plus fill" {
        val fill1 = SizeElement.Fill()
        val fill2 = SizeElement.Fill(3.0f)
        val fill3 = SizeElement.Fill()
        val combined = SizeElement.combine(fill1, fill2, fill3, max = 1f)

        combined[0] shouldBe 0f
        combined[1] shouldBe 0.2f
        combined[2] shouldBe 0.8f
    }
})

class SizesTest : StringSpec({
    "simple equals test" {
        val size1 = Size(SizeElement.Fill(), SizeElement.Fill())
        val size2 = Size(SizeElement.Fill(), SizeElement.Fill())
        val size3 = Size(SizeElement.Fixed(0.1f), SizeElement.Fill())

        (size1 == size2) shouldBe true
        (size2 == size3) shouldBe false
    }

    "equals test" {
        val fill1: SizeElement = SizeElement.Fill()
        val fill2: SizeElement = SizeElement.Fill(3.0f)
        val fixed1: SizeElement = SizeElement.Fixed(0.1f)
        val fixed2: SizeElement = SizeElement.Fixed(0.1f)
        val fixed3: SizeElement = SizeElement.Fixed(0.2f)
        val additional: SizeElement = SizeElement.Additional(fixed1, 0.1f)

        (fill1 == fill2) shouldBe false
        (fill2 == fixed1) shouldBe false
        (fixed1 == fixed2) shouldBe true
        (fixed2 == fixed3) shouldBe false
        (additional == fill1) shouldBe false
    }

    "sum test" {
        val fill1: SizeElement = SizeElement.Fill()
        val fixed1: SizeElement = SizeElement.Fixed(0.1f)
        val fixed2: SizeElement = SizeElement.Fixed(0.1f)
        val fixed3: SizeElement = SizeElement.Fixed(0.2f)
        val additional: SizeElement = fixed1.expand(0.1f)

        listOf(fixed1, fixed2).sumSize() shouldBe SizeElement.Fixed(0.2f)
        listOf(fixed1, fixed2, fixed3).sumSize() shouldBe SizeElement.Fixed(0.4f)
        listOf(fixed1, fill1).sumSize() shouldBe SizeElement.Fill()
        listOf(fixed1, additional).sumSize() shouldBe SizeElement.Fixed(0.3f)
    }

    "max test" {
        val fill1: SizeElement = SizeElement.Fill()
        val fixed1: SizeElement = SizeElement.Fixed(0.1f)
        val fixed2: SizeElement = SizeElement.Fixed(0.1f)
        val fixed3: SizeElement = SizeElement.Fixed(0.2f)
        val additional: SizeElement = fixed1.expand(0.1f)

        listOf(fixed1, fixed2).maxSize() shouldBe SizeElement.Fixed(0.1f)
        listOf(fixed1, fixed2, fixed3).maxSize() shouldBe SizeElement.Fixed(0.2f)
        listOf(fixed1, fill1).maxSize() shouldBe SizeElement.Fill()
        listOf(fixed1, additional).maxSize() shouldBe SizeElement.Fixed(0.2f)
    }
})