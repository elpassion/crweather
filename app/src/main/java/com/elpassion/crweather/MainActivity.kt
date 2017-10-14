package com.elpassion.crweather

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.navigation.*

class MainActivity : AppCompatActivity() {

    val adapter = ChartsAdapter()

    lateinit var model: MainModel

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
        model.loading.observe { displayLoading(it ?: false) }
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
