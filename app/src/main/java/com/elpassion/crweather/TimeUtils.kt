package com.elpassion.crweather

val currentTimeMs get() = System.currentTimeMillis()

val currentTimeString get() = "%tT".format(currentTimeMs)

val Long.asTimeString get() = "%tT".format(this)

val Long.asDateString get() = "%1\$tm-%1\$td".format(this)

val Float.asTimeMs get() = toLong() * 1000
