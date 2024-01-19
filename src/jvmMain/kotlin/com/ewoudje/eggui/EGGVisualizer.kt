package com.ewoudje.eggui

import com.ewoudje.eggui.components.RootContainer
import com.ewoudje.eggui.components.containers.*
import com.ewoudje.eggui.components.elements.RectangleDescriptor
import com.ewoudje.eggui.components.elements.rectangle
import com.ewoudje.eggui.frontend.invoke
import com.ewoudje.eggui.frontend.rem
import com.ewoudje.eggui.frontend.size
import java.awt.Canvas
import java.awt.Color
import java.awt.Graphics
import javax.swing.JFrame
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import kotlin.math.ceil

data object EGGVisualizer : JFrame("EGG Visualizer"), EGGPlatform<EGGVisualizer> {
    private fun readResolve(): Any = EGGVisualizer //idk
    var root = RootContainer<EGGVisualizer>()
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
                val ctx = EGGContext(EGGVisualizer, listOf(Pos(0f, 0f)), listOf(size), pass)
                traverse(root, ctx)
            }
        }) as Canvas
    }

    override fun <T: EGGAssetDescriptor> getAsset(assetDescriptor: T): EGGAsset<EGGVisualizer, T> =
        when (assetDescriptor) {
            is RectangleDescriptor -> RectangleAsset(assetDescriptor.size, Color(assetDescriptor.color)) as EGGAsset<EGGVisualizer, T>
            else -> throw IllegalArgumentException("Unknown asset descriptor: $assetDescriptor")
        }

    class VisualizerRenderer(val g: Graphics): EGGRenderer<EGGVisualizer> {
        override fun renderAsset(asset: EGGAsset<EGGVisualizer, out EGGRenderAssetDescriptor>, pos: Pos) {
            fun Float.u() = ceil(this * g.clipBounds.width).toInt()
            fun Float.v() = ceil(this * yVsX * g.clipBounds.height).toInt()

            when (asset) {
                is RectangleAsset -> {
                    g.color = asset.color
                    println("Rectangle: ${pos.x.u()}, ${pos.y.v()} : ${asset.size.width.u()}, ${asset.size.height.v()}")
                    g.fillRect(pos.x.u(), pos.y.v(), asset.size.width.u(), asset.size.height.v())
                }
            }
        }
    }
}

class RectangleAsset(val size: CalculatedSize, val color: Color) : EGGAsset<EGGVisualizer, RectangleDescriptor>


fun main(args: Array<String>) {
    EGGVisualizer.isVisible = true
    EGGVisualizer.defaultCloseOperation = EXIT_ON_CLOSE
    EGGVisualizer.root.setChild(::HorizontalContainer).apply {
        (vertical) {
            (rectangle size Size.FILL) {
                color = Color.RED.rgb
            }

            (square % rectangle size Size.FILL) {
                color = Color.GREEN.rgb
            }
        }

        (vertical) {
            (square % rectangle size Size.FILL) {
                color = Color.BLUE.rgb
            }
        }
    }

    EGGVisualizer.repaint()
}