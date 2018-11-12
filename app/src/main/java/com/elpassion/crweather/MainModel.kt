package com.elpassion.crweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor

@ObsoleteCoroutinesApi
class MainModel : ViewModel() {

    private val cache = HashMap<String, List<Chart>>(10)

    private val mutableCity = MutableLiveData<String>().apply { value = "" }
    private val mutableCharts = MutableLiveData<List<Chart>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableMessage = MutableLiveData<String>()

    val city: LiveData<String> = mutableCity
    val charts: LiveData<List<Chart>> = mutableCharts
    val loading: LiveData<Boolean> = mutableLoading
    val message: LiveData<String> = mutableMessage

    private val actor = GlobalScope.actor<Action>(Dispatchers.Main, Channel.CONFLATED) {
        for (action in this) when (action) {
            is SelectCity -> {
                mutableCity.value = action.city
                mutableLoading.value = true
                try {
                    mutableCharts.value = cache.getFreshCharts(action.city) ?: getNewCharts(action.city)
                } catch (e: Exception) {
                    mutableMessage.value = e.toString()
                }
                mutableLoading.value = false
            }
        }
    }

    init { action(SelectCity("Warsaw")) }

    fun action(action: Action) = actor.offer(action)

    override fun onCleared() = actor.close().unit

    /**
     * @throws IllegalStateException
     */
    private suspend fun getNewCharts(city: String)
            = Repository.getCityCharts(city).also { cache[city] = it }
}

