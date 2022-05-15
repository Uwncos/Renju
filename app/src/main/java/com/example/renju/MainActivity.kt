package com.example.renju

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private val FILE_NAME = "content.txt"

    override fun onCreate(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openText()
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

    private fun viewText(): String {
        var line: String
        if (File("/data/user/0/com.example.renju/files", FILE_NAME).exists()) {
            val fileStream = openFileInput(FILE_NAME)
            val read = BufferedReader(InputStreamReader(fileStream))
            line = read.readLine()
        }
        else line = "0"
        Log.d("FILE:", "$line")
        return line
    }
}