package com.elpassion.crweather

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async


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
        // TODO: real implementation (use coroutines actor)
        async(UI) {
            city_.value = city
            loading_.value = true
            charts_.value = Repository.getCityCharts(city)
            loading_.value = false
        }
    }
}
