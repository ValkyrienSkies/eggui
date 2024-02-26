package com.ewoudje.eggui.frontend

import com.ewoudje.eggui.EGGContext
import com.ewoudje.eggui.components.*

internal fun traverseComponent(comp: EGGComponent, ctx: EGGContext) {
    traverse(comp, ctx, Trace())
}

class TraverseException(message: String, cause: Throwable? = null) : Exception(message, cause)

private class Trace {
    val stack = mutableListOf<EGGContainer>()

    fun enter(comp: EGGContainer) {
        stack.add(comp)
    }

    fun exit(comp: EGGContainer) {
        if (stack.removeLast() != comp)
            throw IllegalStateException("Stack is not in sync with container")
    }

    fun makeTree(): String {
        val sb = StringBuilder()
        sb.append("Root")
        for (i in stack.size-1 downTo 0) {
            sb.append(" --> ")
            sb.append(stack[i].javaClass.simpleName)
        }
        return sb.toString()
    }

    fun enterError(e: Exception, comp: EGGContainer): Exception {
        return TraverseException("Error entering container ${comp.javaClass.simpleName} in tree:\n${makeTree()}", e)
    }

    fun exitError(e: Exception, comp: EGGContainer): Exception {
        return TraverseException("Error exiting container ${comp.javaClass.simpleName} in tree: ${makeTree()}", e)
    }

    fun visitError(e: Exception, comp: EGGElement): Exception {
        return TraverseException("Error visiting element ${comp.javaClass.simpleName} in tree: ${makeTree()}", e)
    }
}

private fun traverse(comp: EGGComponent, ctx: EGGContext, trace: Trace) {
    when (comp) {
        is EGGMultipleContainer -> {

            if (comp.attachment.onEnter(ctx.pass)) return

            val newCtx = try {
                comp.enter(ctx)
            } catch (e: Exception) {
                throw trace.enterError(e, comp)
            }

            trace.enter(comp)
            comp.children.forEach { traverse(it, newCtx, trace) }

            try {
                comp.exit(newCtx)
            } catch (e: Exception) {
                throw trace.exitError(e, comp)
            }

            if (comp.attachment.onExit(ctx.pass)) return

            trace.exit(comp)
        }
        is EGGSingleContainer -> {
            if (comp.attachment.onEnter(ctx.pass)) return

            val newCtx = try {
                comp.enter(ctx)
            } catch (e: Exception) {
                throw trace.enterError(e, comp)
            }

            trace.enter(comp)
            comp.child?.let { traverse(it, newCtx, trace) }

            if (comp.attachment.onExit(ctx.pass)) return

            try {
                comp.exit(newCtx)
            } catch (e: Exception) {
                throw trace.exitError(e, comp)
            }
            trace.exit(comp)
        }
        is EGGElement -> {
            try {
                if (comp.attachment.onVisit(ctx.pass)) return
                comp.visit(ctx)
            } catch (e: Exception) {
                throw trace.visitError(e, comp)
            }
        }
        is RootContainer -> {
            comp.child?.let { traverse(it, ctx, trace) }
        }
    }
}