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

class EGGVisualizer(val renderer: (g: Graphics) -> EGGRenderer) : JFrame("EGG Visualizer") {
    var root = RootContainer()
    val canvas: Canvas

    init {
        setSize(1280, 720)
        canvas = add(object : Canvas() {
            override fun paint(g: Graphics) {
                g.clearRect(0, 0, width, height)

                val size = CalculatedSize(
                    1f,
                    size.height.toFloat() / size.width.toFloat()
                )

                val pass = EGGPass.Render(renderer(g))
                val ctx = EGGContext(listOf(Pos(0f, 0f)), listOf(size), pass)
                EGGContext.traverse(root, ctx)
            }
        }) as Canvas
    }
}

fun main(args: Array<String>) {

    val yVsX = (1280f / 720f)
    class SimpleRenderer(val g: Graphics): EGGRenderer {
        override fun renderAsset(asset: EGGRenderAsset, pos: Pos) {
            fun Float.u() = ceil(this * g.clip.bounds2D.width).toInt()
            fun Float.v() = ceil(this * yVsX * g.clip.bounds2D.height).toInt()

            when (asset) {
                is RectangleAsset -> {
                    g.color = Color(asset.color)
                    println("Rectangle: ${pos.x.u()}, ${pos.y.v()} : ${asset.size.width.u()}, ${asset.size.height.v()}")
                    g.fillRect(pos.x.u(), pos.y.v(), asset.size.width.u(), asset.size.height.v())
                }
            }
        }
    }

    val visualizer = EGGVisualizer(::SimpleRenderer)

    visualizer.isVisible = true
    visualizer.defaultCloseOperation = EXIT_ON_CLOSE
    visualizer.root.setChild(::HorizontalContainer).apply {
        vertical {
            rectangle {
                size = Size(0.1f, 0.1f)
                color = Color.RED.rgb
            }

            square[rectangle] {
                color = Color.GREEN.rgb
                size = Size(0.1f, 0.1f)
            }
        }

        vertical {
            square[rectangle(Color.YELLOW.rgb)] {
                size = Size.FILL
            }
        }
    }

    visualizer.repaint()
}