package com.elpassion.crweather

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.drawer
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.progress
import kotlinx.android.synthetic.main.content_main.recycler
import kotlinx.android.synthetic.main.navigation.navigation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val adapter = ChartsAdapter()

    private lateinit var model: MainModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        drawer?.run {
            val toggle = ActionBarDrawerToggle(
                    this@MainActivity,
                    drawer,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()
        }
        navigation.setNavigationItemSelectedListener {
            model.action(SelectCity(it.title.toString()))
        }
        recycler.adapter = adapter
        initModel()
    }

    private fun initModel() {
        model = ViewModelProviders.of(this).get(MainModel::class.java)
        model.loading.observe { displayLoading(it == true) }
        model.city.observe { displayCity(it ?: "") }
        model.charts.observe { displayCharts(it ?: emptyList()) }
        model.message.observe { displayMessage(it ?: "") }
    }

    private fun displayLoading(loading: Boolean) {
        progress.visibility = if (loading) VISIBLE else INVISIBLE
    }

    private fun displayCity(city: String) {
        title = city
        for (item in navigation.menu)
            item.isChecked = item.title == city
    }

    private fun displayCharts(charts: List<Chart>) { adapter.charts = charts }

    private fun displayMessage(message: String) {
        if (message.isNotBlank()) toast(message)
    }

    private fun <T> LiveData<T>.observe(observe: (T?) -> Unit)
            = observe(this@MainActivity, Observer { observe(it) })
}
