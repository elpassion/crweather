package com.elpassion.crweather

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test fun useAppContext() {
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.elpassion.crweather", appContext.packageName)
    }
}
