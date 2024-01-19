package com.ewoudje.eggui

import com.ewoudje.eggui.components.containers.HorizontalContainer
import com.ewoudje.eggui.components.containers.VerticalContainer
import io.kotest.core.spec.style.StringSpec

class ContainerTest : StringSpec({
    "vertical verification" {
        ContainerVerification.verify(::VerticalContainer)
    }

    "horizontal verification" {
        ContainerVerification.verify(::HorizontalContainer)
    }

    /*
    "vertical test" {
        val container = RootContainer()
        val vertical = container.addChild(::VerticalContainer)
        val rectangle = vertical.addChild(RectangleElement.factory(Size(SizeElement.Fill(), SizeElement.Fill())))

        container.size.width shouldBe SizeElement.Fill()
        container.size.height shouldBe SizeElement.Fill()
    }

    "vertical test2" {
        val container = RootContainer()
        val vertical = container.addChild(::VerticalContainer)
        val rectangle = vertical.addChild(RectangleElement.factory(Size(SizeElement.Fixed(0.2f), SizeElement.Fixed(0.1f))))
        val rectangle2 = vertical.addChild(RectangleElement.factory(Size(SizeElement.Fixed(0.1f), SizeElement.Fixed(0.2f))))

        container.size.width shouldBe SizeElement.Fixed(0.2f)
        container.size.height shouldBe SizeElement.Fixed(0.3f)
    }*/
})