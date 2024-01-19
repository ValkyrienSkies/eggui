package com.ewoudje.eggui.frontend

import com.ewoudje.eggui.EGGContext
import com.ewoudje.eggui.components.*

fun traverse(comp: EGGComponent, ctx: EGGContext) {
    when (comp) {
        is EGGMultipleContainer -> {
            val newCtx = comp.enter(ctx)
            comp.children.forEach { traverse(it, newCtx) }
            comp.exit(newCtx)
        }
        is EGGSingleContainer -> {
            val newCtx = comp.enter(ctx)
            comp.child?.let { traverse(it, newCtx) }
            comp.exit(newCtx)
        }
        is EGGElement -> {
            comp.visit(ctx)
        }
        is RootContainer -> {
            comp.child?.let { traverse(it, ctx) }
        }
    }
}