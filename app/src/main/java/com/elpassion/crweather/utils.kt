package com.elpassion.crweather

import android.support.annotation.LayoutRes
import android.view.*


operator fun Menu.iterator() = object : Iterator<MenuItem> {
    private var current = 0
    override fun hasNext() = current < size()
    override fun next() = getItem(current++)
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View {
    val inflater = LayoutInflater.from(context)
    return inflater.inflate(layoutId, this, attachToRoot)
}

operator fun StringBuilder.plusAssign(string: String) { append(string) }
