package org.hyperskill.app.core.utils

fun <K, V> Map<K, V>.mutate(block: MutableMap<K, V>.() -> Unit): Map<K, V> =
    this.toMutableMap().apply(block)