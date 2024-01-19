package com.ewoudje.eggui

fun <T> T?.singletonOrEmpty(): Iterable<T> = if (this == null) emptyList() else listOf(this)