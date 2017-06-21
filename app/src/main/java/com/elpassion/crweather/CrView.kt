package com.elpassion.crweather

import android.content.Context
import android.graphics.Canvas
import android.support.annotation.CallSuper
import android.util.AttributeSet
import android.view.View
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine


open class CrView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var drawer: (Canvas.() -> Unit)? = null
    private var continuation: Continuation<Unit>? = null

    suspend fun draw(drawer: Canvas.() -> Unit) {
        this.drawer = drawer
        invalidate()
        suspendCoroutine<Unit> { continuation = it  }
    }

    @CallSuper
    override fun onDraw(canvas: Canvas) {
        drawer?.invoke(canvas)
        drawer = null
        continuation?.run { post { resume(Unit) } }
        continuation = null
    }
}