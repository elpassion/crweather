package com.elpassion.crweather

import android.view.Menu
import android.view.MenuItem


operator fun Menu.iterator() = object : Iterator<MenuItem> {
    private var current = 0
    override fun hasNext() = current < size()
    override fun next() = getItem(current++)
}