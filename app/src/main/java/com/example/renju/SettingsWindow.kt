package com.example.renju

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.red
import androidx.core.graphics.toColor

//https://www.youtube.com/watch?v=tvl1s7PxS6Q&t=2s
//http://androidseeker.blogspot.com/2012/09/how-to-apply-new-theme-to-whole.html

class SettingsWindow : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchSystem: Switch
    private lateinit var switchLight: Switch
    private lateinit var switchDark: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_window)

        switchLight = findViewById(R.id.switch2)
        switchSystem = findViewById(R.id.switch1)
        switchDark = findViewById(R.id.switch3)

        switchLight.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchSystem.isChecked = false
                switchDark.isChecked = false
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchDark.isChecked = true
           }
        })

        switchDark.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchSystem.isChecked = false
                switchLight.isChecked = false
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchLight.isChecked = true
            }
        })
        switchSystem.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                switchLight.isChecked = false
                switchDark.isChecked = false
                if (isDarkThemeOn()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchDark.isChecked = true
            }
        })
    }

    private fun onClick() {
        switchSystem = findViewById(R.id.switch1)
        switchLight = findViewById(R.id.switch2)
        switchDark = findViewById(R.id.switch3)
    }


    private fun setSystemTheme(switch: Switch) {
        if (switch.isChecked) {
            switchLight = findViewById(R.id.switch2)
            switchLight!!.isChecked = false
            switchDark = findViewById(R.id.switch3)
            switchDark!!.isChecked = false
            if (isDarkThemeOn()) {
                theme.applyStyle(R.style.RenjuLight, true)
                var a = R.color.GameBarColor
            } else {
                theme.applyStyle(R.style.RenjuDark, true)
            }
        }
    }

    private fun setLight(switch: Switch) {
        if (switch.isChecked) {
            switchSystem = findViewById(R.id.switch1)
            switchSystem!!.isChecked = false
            switchDark = findViewById(R.id.switch3)
            switchDark!!.isChecked = false
            if (isDarkThemeOn()) {
                theme.applyStyle(R.style.RenjuLight, true)
            }
        }
    }

    private fun setDark(switch: Switch) {
        if (switch.isChecked) {
            switchLight = findViewById(R.id.switch2)
            switchLight!!.isChecked = false
            switchSystem = findViewById(R.id.switch1)
            switchSystem!!.isChecked = false
            if (!isDarkThemeOn()) {
                val a = isDarkThemeOn()
                Log.d("dark?", "$a")
                theme.applyStyle(R.style.RenjuDark, true)
            }
        }
    }

    fun Context.isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    }
}