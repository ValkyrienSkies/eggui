package com.ewoudje.eggui

import com.ewoudje.eggui.components.*

object ContainerVerification {
    private val OMEGA = 0.0001f

    private class TestPass(val pos: Pos, val size: CalculatedSize) : EGGPass {
        var totalSize = CalculatedSize.ZERO
    }

    fun verify(container: EGGChildConstructor<out EGGMultipleContainer>) {
        val root = RootContainer()
        val container = root.setChild(container)

        container.addChild(::TestElement)
        container.addChild(::TestElement)
        container.addChild(::TestElement)
        container.addChild(::TestElement)


        val iter = container.children.iterator()
        iter.next()
        iter.next()
        iter.next()
        iter.next()
        if (iter.hasNext()) throw IllegalStateException("Container should only have four children")


        fun setSizes(s1: Size, s2: Size, s3: Size, s4: Size) {
            val iter = container.children.iterator()
            iter.next().size = s1
            iter.next().size = s2
            iter.next().size = s3
            iter.next().size = s4
        }

        fun testContainer(pos: Pos, size: CalculatedSize) {
            val ctx = EGGContext(
                listOf(pos), listOf(size),
                TestPass(pos, size)
            )
            EGGContext.traverse(container, ctx)
        }

        setSizes(
            Size(0.1f, 0.1f),
            Size(0.1f, 0.1f),
            Size(0.1f, 0.1f),
            Size(0.1f, 0.1f)
        )
        testContainer(Pos(0f, 0f), CalculatedSize(1f, 1f))
        testContainer(Pos(0.1f, 0.1f), CalculatedSize(0.7f, 0.8f))
        testContainer(Pos(0.3f, 0.3f), CalculatedSize(0.8f, 0.7f))
        testContainer(Pos(0.1f, 0.4f), CalculatedSize(0.6f, 0.8f))

        setSizes(
            Size(0.1f, SizeElement.Fill()),
            Size(SizeElement.Fill(), 0.1f),
            Size(SizeElement.Fill(), SizeElement.Fill()),
            Size(0.4f, 0.3f)
        )
        testContainer(Pos(0f, 0f), CalculatedSize(1f, 1f))
        testContainer(Pos(0.1f, 0.1f), CalculatedSize(0.8f, 0.8f))
        testContainer(Pos(0.4f, 0.3f), CalculatedSize(0.9f, 0.8f))
    }

    private class TestElement(
        override val parent: EGGContainerParent,
        override val childId: Int
    ) : EGGFixedElement, EGGFillingElement {
        override fun visit(ctx: EGGContext) {
            when (ctx.pass) {
                is TestPass -> {
                    val testSize = ctx.pass.size
                    val diff = ctx.position - ctx.pass.pos
                    if (diff.x < -OMEGA || diff.y < -OMEGA)
                        throw IllegalStateException("${ctx.position} is outside the containers lower bounds of ${testSize + ctx.pass.pos}")
                    if (diff.x > testSize.width + OMEGA || diff.y > testSize.height + OMEGA)
                        throw IllegalStateException("${ctx.position} is outside the containers upper bounds of ${testSize + ctx.pass.pos}")
                    if (diff.x + ctx.size.width > testSize.width + OMEGA || diff.y + ctx.size.height > testSize.height + OMEGA)
                        throw IllegalStateException("element at ${ctx.position} with size ${ctx.size} overflows container upper bounds ${testSize + ctx.pass.pos}")

                    val size = ctx.size
                    if (size.width < -OMEGA || size.height < -OMEGA) throw IllegalStateException("Size is smaller than 0")
                    if (size.width > testSize.width + OMEGA) throw IllegalStateException("Width is bigger than the container width")
                    if (size.height > testSize.height + OMEGA) throw IllegalStateException("Height is bigger than the container height")

                    ctx.pass.totalSize += size
                    //TODO check for element collision
                }
                else -> {}
            }
        }
    }
}