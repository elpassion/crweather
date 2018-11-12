package com.elpassion.crweather

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.annotation.CallSuper
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume


open class CrView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var drawer: Canvas.() -> Unit = {}
    private var continuation: Continuation<Unit>? = null

    fun doOnDraw(drawer: Canvas.() -> Unit) { this.drawer = drawer }

    suspend fun redraw() {
        require(continuation === null) { "Redrawing is already in progress." }
        invalidate()
        suspendCoroutine<Unit> { continuation = it }
    }

    suspend fun draw(drawer: Canvas.() -> Unit) {
        doOnDraw(drawer)
        redraw()
    }

    @CallSuper
    override fun onDraw(canvas: Canvas) {
        drawer.invoke(canvas)
        continuation?.run { post { resume(Unit) } }
        continuation = null
    }
}
