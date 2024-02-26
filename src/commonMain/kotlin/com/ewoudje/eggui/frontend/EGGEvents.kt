package com.ewoudje.eggui.frontend

import com.ewoudje.eggui.EGGPass
import com.ewoudje.eggui.components.EGGContainer
import com.ewoudje.eggui.components.EGGElement

fun EGGContainer.catchEnter(block: (EGGPass) -> Boolean) {
    val visit = attachment.onEnter
    attachment.onEnter = { block(it) || visit(it) }
}

fun EGGContainer.catchExit(block: (EGGPass) -> Boolean) {
    val visit = attachment.onExit
    attachment.onExit = { block(it) || visit(it) }
}

fun EGGElement.catchVisit(block: (EGGPass) -> Boolean): Unit {
    val visit = attachment.onVisit
    attachment.onVisit = { block(it) || visit(it) }
}