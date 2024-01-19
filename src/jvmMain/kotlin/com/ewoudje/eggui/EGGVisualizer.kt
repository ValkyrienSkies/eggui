package com.ewoudje.eggui

import com.ewoudje.eggui.components.RootContainer
import com.ewoudje.eggui.components.containers.*
import com.ewoudje.eggui.components.elements.RectangleAsset
import com.ewoudje.eggui.components.elements.rectangle
import com.ewoudje.eggui.frontend.*
import java.awt.Canvas
import java.awt.Color
import java.awt.Graphics
import javax.swing.JFrame
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import kotlin.math.ceil

data object EGGVisualizer : JFrame("EGG Visualizer") {
    private fun readResolve(): Any = EGGVisualizer //idk
    var root = RootContainer()
    val canvas: Canvas
    val xVsY = (720f / 1280f)
    val yVsX = (1280f / 720f)

    init {
        setSize(1280, 720)
        canvas = add(object : Canvas() {
            override fun paint(g: Graphics) {
                g.clearRect(0, 0, width, height)

                val size = CalculatedSize(
                    1f,
                    xVsY
                )

                val pass = EGGPass.Render(VisualizerRenderer(g))
                val ctx = EGGContext(listOf(Pos(0f, 0f)), listOf(size), pass)
                traverse(root, ctx)
            }
        }) as Canvas
    }

    class VisualizerRenderer(val g: Graphics): EGGRenderer {
        override fun renderAsset(asset: EGGRenderAsset, pos: Pos) {
            fun Float.u() = ceil(this * g.clipBounds.width).toInt()
            fun Float.v() = ceil(this * yVsX * g.clipBounds.height).toInt()

            when (asset) {
                is RectangleAsset -> {
                    g.color = Color(asset.color)
                    println("Rectangle: ${pos.x.u()}, ${pos.y.v()} : ${asset.size.width.u()}, ${asset.size.height.v()}")
                    g.fillRect(pos.x.u(), pos.y.v(), asset.size.width.u(), asset.size.height.v())
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    EGGVisualizer.isVisible = true
    EGGVisualizer.defaultCloseOperation = EXIT_ON_CLOSE
    EGGVisualizer.root.setChild(::HorizontalContainer).apply {
        vertical {
            rectangle {
                size = Size(0.1f, 0.6f)
                color = Color.RED.rgb
            }

            square[rectangle] {
                color = Color.GREEN.rgb
                size = Size(0.1f, 0.6f)
            }
        }

        vertical {
            square[rectangle(Color.YELLOW.rgb)] {
                size = Size.FILL
            }
        }
    }

    EGGVisualizer.repaint()
}