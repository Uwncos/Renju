package com.example.renju

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

class SettingsWindow : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")

    private lateinit var buttonToSystem: ToggleButton
    private lateinit var buttonToLight: ToggleButton
    private lateinit var buttonToDark: ToggleButton
    private lateinit var checkBox: CheckBox

    private var p1 = R.drawable.button_no_border
    private var p2 = R.drawable.button_no_border
    private var p3 = R.drawable.button_no_border

    private var size = 15


    companion object {
        val BORDER_1 = "border1"
        val BORDER_2 = "border2"
        val BORDER_3 = "border3"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        barColorChange()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_window)
        setBoardSize()
        buttonToSystem = findViewById(R.id.buttonToSystem)
        buttonToLight = findViewById(R.id.buttonToLight)
        buttonToDark = findViewById(R.id.buttonToDark)
        checkBox = findViewById(R.id.checkBox)
        if (getCheck() == "true") checkBox.isChecked = true
        else if (getCheck() == "false") checkBox.isChecked = false
        else checkBox.isChecked = false

        if (savedInstanceState == null) {
            buttonToSystem.background = ContextCompat.getDrawable(this, R.drawable.button_no_border)
            buttonToLight.background = ContextCompat.getDrawable(this, R.drawable.button_no_border)
            buttonToDark.background = ContextCompat.getDrawable(this, R.drawable.button_no_border)
        } else {
            p1 = savedInstanceState.getInt(BORDER_1)
            buttonToSystem.background = ContextCompat.getDrawable(this, p1)
            p2 = savedInstanceState.getInt(BORDER_2)
            buttonToLight.background = ContextCompat.getDrawable(this, p2)
            p3 = savedInstanceState.getInt(BORDER_3)
            buttonToDark.background = ContextCompat.getDrawable(this, p3)
        }
        setBoardSize()
    }

    fun barColorChange() {
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

    fun getCheck(): String {
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BORDER_1, p1)
        outState.putInt(BORDER_2, p2)
        outState.putInt(BORDER_3, p3)
    }

    fun setSystemTheme(view: View) {
        buttonToSystem = findViewById(R.id.buttonToSystem)
        buttonToLight = findViewById(R.id.buttonToLight)
        buttonToDark = findViewById(R.id.buttonToDark)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        buttonToSystem.background = ContextCompat.getDrawable(this, R.drawable.button_border)
        p1 = R.drawable.button_border
        p2 = R.drawable.button_no_border
        p3 = R.drawable.button_no_border
        recreate()
    }

    fun setLightTheme(view: View) {
        buttonToSystem = findViewById(R.id.buttonToSystem)
        buttonToLight = findViewById(R.id.buttonToLight)
        buttonToDark = findViewById(R.id.buttonToDark)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        buttonToLight.background = ContextCompat.getDrawable(this, R.drawable.button_border)
        p2 = R.drawable.button_border
        p1 = R.drawable.button_no_border
        p3 = R.drawable.button_no_border
        recreate()
    }

    fun setDarkTheme(view: View) {
        buttonToSystem = findViewById(R.id.buttonToSystem)
        buttonToLight = findViewById(R.id.buttonToLight)
        buttonToDark = findViewById(R.id.buttonToDark)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        buttonToDark.background = ContextCompat.getDrawable(this, R.drawable.button_border)
        p3 = R.drawable.button_border
        p2 = R.drawable.button_no_border
        p1 = R.drawable.button_no_border
        recreate()
    }

    private fun setBoardSize() {
        val sharedCheck = getSharedPreferences("check", MODE_PRIVATE)
        val edit = sharedCheck.edit()
        checkBox = findViewById(R.id.checkBox)
        checkBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked){
                edit.putString("isChecked", "true")
                size = 19
                saveSize(size.toString())
            } else{
                edit.putString("isChecked", "false")
                size = 15
                saveSize(size.toString())
            }
            edit.apply()
        }
    }
}