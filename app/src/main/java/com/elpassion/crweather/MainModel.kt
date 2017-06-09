package com.elpassion.crweather

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Color
import android.support.annotation.ColorInt


class MainModel : ViewModel() {

    private val city_ = MutableLiveData<String>().apply { value = "" }
    private val charts_ = MutableLiveData<List<Chart>>().apply { value = emptyList() }
    private val loading_ = MutableLiveData<Boolean>().apply { value = false }

    val city: LiveData<String> = city_
    val charts: LiveData<List<Chart>> = charts_
    val loading: LiveData<Boolean> = loading_

    init {
        selectCity("Warsaw")
    }

    fun selectCity(city: String) {
        // TODO: real implementation
        this.city_.value = city
        this.charts_.value = listOf(
                Chart(
                        inputRange = 1f..4f,
                        outputRange = -10f..50f,
                        lines = listOf(
                                Line(
                                        name = "Temperature in Celsius",
                                        color = Color.RED,
                                        points = listOf(
                                                Point(1.0f, 10f),
                                                Point(1.5f, 20f),
                                                Point(2.0f, 15f),
                                                Point(2.5f, 11f),
                                                Point(3.0f, 10f),
                                                Point(3.5f, -7f),
                                                Point(4.0f, -5f)
                                        )
                                )
                        )
                )
        )
    }

}

data class Chart(
        val inputRange: ClosedFloatingPointRange<Float>,
        val outputRange: ClosedFloatingPointRange<Float>,
        val lines: List<Line>,
        val time: Long = System.currentTimeMillis()
)

data class Line(
        val name: String,
        @ColorInt val color: Int,
        val points: List<Point>
)

data class Point(val x: Float, val y: Float)