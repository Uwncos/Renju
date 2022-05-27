package com.example.renju

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private val FILE_NAME = "content.txt"

    override fun onCreate(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else {
            barColorChange()
        }
        super.onCreate(savedInstanceState)
        getMode()
        setContentView(R.layout.activity_main)
        openText()
    }

    fun barColorChange() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.backgroundMainColorLight)
    }


    fun toGameWindow(view: View) {
        val intent = Intent(this, GameWindow::class.java)
        startActivity(intent)
    }

    fun toSettingsWindow(view: View) {
        val intent = Intent(this, SettingsWindow::class.java)
        startActivity(intent)
    }

    private fun openText() {
        val bestScoreView = findViewById<TextView>(R.id.bestScoreDraw0)
        try {
            val fin = openFileInput(FILE_NAME)
            val bytes = ByteArray(fin.available())
            fin.read(bytes)
            val text = String(bytes)
            bestScoreView.text = text
            fin.close()

        } catch (ex: FileNotFoundException) {
            print(ex.message)
        }

    }

    private fun getMode() {
        val mode: Int
        val sharedMode = getSharedPreferences("theme_mode", MODE_PRIVATE)
        mode = sharedMode.getInt("mode", -1)
        if (mode == -1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else if (mode == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

}