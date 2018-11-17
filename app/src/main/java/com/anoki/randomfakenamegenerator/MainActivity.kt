package com.anoki.randomfakenamegenerator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.android.anoki.randomfakenamegenerator.di.appModule
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.koin.android.ext.android.startKoin

class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            try {
                startKoin(this, listOf(appModule))
            } catch (e: Exception) {
                error { e }
            }
        }
        NavigationUI.setupActionBarWithNavController(
                this,
                this.findNavController(R.id.nav_host_fragment)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return this.findNavController(R.id.nav_host_fragment).navigateUp()
    }
}
