package com.example.renju

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import org.jetbrains.annotations.NotNull
import kotlin.properties.Delegates.notNull

class SettingsWindow : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")

    private lateinit var buttonToSystem: ToggleButton
    private lateinit var buttonToLight: ToggleButton
    private lateinit var buttonToDark: ToggleButton
    private lateinit var checkBox: CheckBox

    private var p1 = 0
    private var p2 = 0
    private var p3 = 0

    private var size = 15
    private var mode = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        barColorChange()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_window)

        setBoardSize()
        buttonToSystem = findViewById(R.id.buttonToSystem)
        buttonToLight = findViewById(R.id.buttonToLight)
        buttonToDark = findViewById(R.id.buttonToDark)
        checkBox = findViewById(R.id.checkBox)
        val sharedMode = getSharedPreferences("theme_mode", MODE_PRIVATE)
        if (sharedMode == null) {
            buttonToSystem.background = ContextCompat.getDrawable(this, R.drawable.button_border)
            buttonToLight.background = ContextCompat.getDrawable(this, R.drawable.button_no_border)
            buttonToDark.background = ContextCompat.getDrawable(this, R.drawable.button_no_border)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            getMode()
        }

        when {
            getCheck() == "true" -> checkBox.isChecked = true
            getCheck() == "false" -> checkBox.isChecked = false
            else -> checkBox.isChecked = false
        }
        setBoardSize()
//        saveMode()
    }

    private fun barColorChange() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.backgroundMainColorLight)
    }

    private fun saveSize(size: String) {

        val shared = getSharedPreferences("size_key", MODE_PRIVATE)
        val edit = shared.edit()
        edit.putString("value", size)
        edit.apply()
    }

    private fun getCheck(): String {
        val isChecked: String
        val sharedCheck = getSharedPreferences("check", MODE_PRIVATE)
        if (sharedCheck == null) {
            isChecked = "false"
        } else {
            isChecked = sharedCheck.getString("isChecked", "")!!
        }
        Log.d("isChecked", isChecked)
        return isChecked
    }

    private fun saveMode() {
        val sharedMode = getSharedPreferences("theme_mode", MODE_PRIVATE)
        val edit = sharedMode.edit()
        edit.putInt("button1", p1)
        edit.putInt("button2", p2)
        edit.putInt("button3", p3)
        edit.putInt("mode", mode)
        edit.apply()
    }


    private fun getMode() {
        val sharedMode = getSharedPreferences("theme_mode", MODE_PRIVATE)
        p1 = sharedMode.getInt("button1", R.drawable.button_border)
        p2 = sharedMode.getInt("button2", R.drawable.button_no_border)
        p3 = sharedMode.getInt("button3", R.drawable.button_no_border)
        mode = sharedMode.getInt("mode", -1)
        if (mode == -1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else if (mode == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        buttonToSystem.background = ContextCompat.getDrawable(this, p1)
        buttonToLight.background = ContextCompat.getDrawable(this, p2)
        buttonToDark.background = ContextCompat.getDrawable(this, p3)
    }

    fun setSystemTheme(view: View) {
        buttonToSystem = findViewById(R.id.buttonToSystem)
        buttonToLight = findViewById(R.id.buttonToLight)
        buttonToDark = findViewById(R.id.buttonToDark)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        mode = -1
        buttonToSystem.background = ContextCompat.getDrawable(this, R.drawable.button_border)
        p1 = R.drawable.button_border
        p2 = R.drawable.button_no_border
        p3 = R.drawable.button_no_border
        saveMode()
        recreate()
    }

    fun setLightTheme(view: View) {
        buttonToSystem = findViewById(R.id.buttonToSystem)
        buttonToLight = findViewById(R.id.buttonToLight)
        buttonToDark = findViewById(R.id.buttonToDark)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        mode = 1
        buttonToLight.background = ContextCompat.getDrawable(this, R.drawable.button_border)
        p2 = R.drawable.button_border
        p1 = R.drawable.button_no_border
        p3 = R.drawable.button_no_border
        saveMode()
        recreate()
    }

    fun setDarkTheme(view: View) {
        buttonToSystem = findViewById(R.id.buttonToSystem)
        buttonToLight = findViewById(R.id.buttonToLight)
        buttonToDark = findViewById(R.id.buttonToDark)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        mode = 2
        buttonToDark.background = ContextCompat.getDrawable(this, R.drawable.button_border)
        p3 = R.drawable.button_border
        p2 = R.drawable.button_no_border
        p1 = R.drawable.button_no_border
        saveMode()
        recreate()
    }

    private fun setBoardSize() {
        val sharedCheck = getSharedPreferences("check", MODE_PRIVATE)
        val edit = sharedCheck.edit()
        checkBox = findViewById(R.id.checkBox)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                edit.putString("isChecked", "true")
                size = 19
                saveSize(size.toString())
            } else {
                edit.putString("isChecked", "false")
                size = 15
                saveSize(size.toString())
            }
            edit.apply()
        }
    }
}