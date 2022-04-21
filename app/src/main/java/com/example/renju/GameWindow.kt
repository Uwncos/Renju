package com.example.renju

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GameWindow : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_window)
    }
}