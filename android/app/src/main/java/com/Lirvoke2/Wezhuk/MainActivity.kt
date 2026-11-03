package com.Lirvoke2.Wezhuk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.Lirvoke2.Wezhuk.core.di.ServiceLocator
import com.Lirvoke2.Wezhuk.core.theme.AppTheme
import com.Lirvoke2.Wezhuk.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
