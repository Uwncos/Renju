package com.example.renju

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


//https://www.youtube.com/watch?v=tvl1s7PxS6Q&t=2s
//http://androidseeker.blogspot.com/2012/09/how-to-apply-new-theme-to-whole.html

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
        val SIZE_KEY = "size_key"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_window)
        Log.d("new", "OnCreare starts")
        setBoardSize()
        buttonToSystem = findViewById(R.id.buttonToSystem)
        buttonToLight = findViewById(R.id.buttonToLight)
        buttonToDark = findViewById(R.id.buttonToDark)
        if (savedInstanceState == null) {
            buttonToSystem.background = this.resources.getDrawable(R.drawable.button_no_border)
            buttonToLight.background = this.resources.getDrawable(R.drawable.button_no_border)
            buttonToDark.background = this.resources.getDrawable(R.drawable.button_no_border)
        } else {
            p1 = savedInstanceState.getInt(BORDER_1)
            buttonToSystem.background = this.resources.getDrawable(p1)
            p2 = savedInstanceState.getInt(BORDER_2)
            buttonToLight.background = this.resources.getDrawable(p2)
            p3 = savedInstanceState.getInt(BORDER_3)
            buttonToDark.background = this.resources.getDrawable(p3)
        }
        setBoardSize()
//        init()

//        val myIntent = Intent()
//        myIntent.putExtra("key", "value")
//        startActivity(myIntent)
    }

    private fun saveSize(size: String) {

//        val shared = getSharedPreferences("size_key", MODE_PRIVATE)
        val shared = PreferenceManager
            .getDefaultSharedPreferences(this)
        val edit = shared.edit()
        edit.putString("value", size)
        edit.apply()
    }



//    fun init() {
//        val shared = PreferenceManager.getDefaultSharedPreferences(this)
//    }

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
        buttonToSystem.background = this.resources.getDrawable(R.drawable.button_border)
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
        buttonToLight.background = this.resources.getDrawable(R.drawable.button_border)
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
        buttonToDark.background = this.resources.getDrawable(R.drawable.button_border)
        p3 = R.drawable.button_border
        p2 = R.drawable.button_no_border
        p1 = R.drawable.button_no_border
        recreate()
    }

    fun setBoardSize() {
        checkBox = findViewById(R.id.checkBox)
        checkBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked){
                size = 19
                saveSize(size.toString())
            } else{
                size = 15
                saveSize(size.toString())
            }
        }
    }

    fun Context.isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    }
}