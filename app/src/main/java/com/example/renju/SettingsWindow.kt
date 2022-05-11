package com.example.renju

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//https://www.youtube.com/watch?v=tvl1s7PxS6Q&t=2s
//http://androidseeker.blogspot.com/2012/09/how-to-apply-new-theme-to-whole.html

class SettingsWindow : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var switchSystem: Switch? = null
    private var switchLight: Switch? = null
    private var switchDark: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_window)
        setTheme(R.style.Theme_RenjuDark)
        switchSystem = findViewById(R.id.switch1)
        switchLight = findViewById(R.id.switch2)
        switchDark = findViewById(R.id.switch3)
        switchSystem!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                setSystemTheme()
            }
        }
        switchLight!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                setLight()
            }
        }
        switchDark!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                setDark()
            }
        }
    }

    fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        Toast.makeText(
            this, "Отслеживание переключения: " + if (isChecked) "on" else "off",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setListen() {
        switchSystem = findViewById(R.id.switch1)
        switchLight = findViewById(R.id.switch2)
        switchDark = findViewById(R.id.switch3)

    }


    private fun setSystemTheme() {
        switchLight = findViewById(R.id.switch2)
        switchLight!!.isChecked = false
        switchDark = findViewById(R.id.switch3)
        switchDark!!.isChecked = false
        if (isDarkThemeOn()) {
            application.setTheme(R.style.Theme_Renju)
        }
        if (!isDarkThemeOn()) {
            application.setTheme(R.style.Theme_Renju)
        }
    }

    private fun setLight() {
        switchSystem = findViewById(R.id.switch1)
        switchSystem!!.isChecked = false
        switchDark = findViewById(R.id.switch3)
        switchDark!!.isChecked = false
        if (isDarkThemeOn()) {
            application.setTheme(R.style.Theme_RenjuLight)
        }
    }

    private fun setDark() {
        switchLight = findViewById(R.id.switch2)
        switchLight!!.isChecked = false
        switchSystem = findViewById(R.id.switch1)
        switchSystem!!.isChecked = false
        if (!isDarkThemeOn()) {
            val a = isDarkThemeOn()
            Log.d("dark?", "$a")
            application.setTheme(R.style.Theme_RenjuDark)
        }
    }

    fun Context.isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    }
}