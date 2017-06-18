package com.elpassion.crweather

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, LifecycleRegistryOwner {

    val registry = LifecycleRegistry(this)

    val adapter = ChartsAdapter()

    lateinit var model: MainModel

    override fun getLifecycle() = registry // can not use LifecycleActivity (it does not have setSupportActionBar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigation.setNavigationItemSelectedListener(this)
        recycler.adapter = adapter
        initModel()
    }

    override fun onBackPressed() = if (drawer.isDrawerOpen(GravityCompat.START)) {
        drawer.closeDrawer(GravityCompat.START)
    } else {
        super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer.closeDrawer(GravityCompat.START)
        model.selectCity(item.title.toString())
        return true
    }

    private fun initModel() {
        model = ViewModelProviders.of(this).get(MainModel::class.java)
        model.loading.observe(this, Observer { displayLoading(it ?: false) })
        model.city.observe(this, Observer { displayCity(it ?: "") })
        model.charts.observe(this, Observer { displayCharts(it ?: emptyList()) })
    }

    private fun displayLoading(loading: Boolean) {
        Log.w("CRW", "displaying loading: $loading")
        progress.visibility = if (loading == true) View.VISIBLE else View.INVISIBLE
    }

    private fun displayCity(city: String) {
        Log.w("CRW", "displaying city: $city")
        title = city
        for (item in navigation.menu) {
            item.isChecked = item.title == city
        }
    }

    private fun displayCharts(charts: List<Chart>) {
        Log.w("CRW", "displaying charts:\n$charts")
        adapter.charts = charts
    }
}
