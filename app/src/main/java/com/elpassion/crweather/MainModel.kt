package com.elpassion.crweather

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor


class MainModel : ViewModel() {

    private val cache = HashMap<String, List<Chart>>(10)

    private val city_ = MutableLiveData<String>().apply { value = "" }
    private val charts_ = MutableLiveData<List<Chart>>().apply { value = emptyList() }
    private val loading_ = MutableLiveData<Boolean>().apply { value = false }

    val city: LiveData<String> = city_
    val charts: LiveData<List<Chart>> = charts_
    val loading: LiveData<Boolean> = loading_

    val actor = actor<Action>(UI, Channel.CONFLATED) {
        for (action in this) when (action) {
            is SelectCity -> {
                city_.value = action.city
                loading_.value = true
                charts_.value = cache.getFreshCharts(action.city) ?: getNewCharts(action.city)
                loading_.value = false
            }
        }
    }

    init { action(SelectCity("Warsaw")) }

    fun action(action: Action) = actor.offer(action)

    override fun onCleared() = actor.cancel().unit

    private suspend fun getNewCharts(city: String) = Repository.getCityCharts(city).also { cache[city] = it }
}

