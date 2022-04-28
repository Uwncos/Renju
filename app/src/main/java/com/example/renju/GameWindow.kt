package com.example.renju

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class GameWindow : AppCompatActivity() {

    private val boardSize = 15
    private var context = this

    private var btnPlay1: Button? = null
    private var btnPlayInGame: Button? = null
    private var turn: TextView? = null

    private val ivCell = Array(boardSize) {
        arrayOfNulls<ImageView>(
            boardSize
        )
    }

    private val valueCell = Array(boardSize) {
        IntArray(
            boardSize
        )
    }

    private var winner_play = 0
    private var firstMove = false
    private var xMove = 0
    private var yMove = 0
    private var turnPlay = 0

        private val drawCell = arrayOfNulls<Drawable>(4)
//    private val btnPlay = Button(this)
//    private val tvTurn: TextView? = null


    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        //leaderCount add in bundle (https://www.youtube.com/watch?v=puj9OXs2iPM&list=PLRmiL0mct8WnodKkGLpBN0mfXIbAAX-Ux&index=9)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_window)
        context = this
        setListen()
        loadResources()
        designBoardGame()


    }

    private fun setListen() {
        btnPlay1 = findViewById(R.id.mainButton1)
        btnPlayInGame = findViewById(R.id.reloadButton)
        turn = findViewById(R.id.turn)
        turn!!.text = "Turn:"

        btnPlay1!!.setOnClickListener {
            init_game()
            play_game()
        }
        btnPlayInGame!!.setOnClickListener {
            init_game()
            play_game()
        }
    }

    private fun init_game() {
        firstMove = true
        winner_play = 0

        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                ivCell[i][j]!!.setImageDrawable(drawCell[0]) //default or Empty cell
                valueCell[i][j] = 0
            }
        }

    }

    private fun play_game() {
        val r = Random()
        turnPlay = r.nextInt(2) + 1
        if (turnPlay == 1) {
            Toast.makeText(this, "Make first move", Toast.LENGTH_SHORT).show()
            playerTurn()
        }
        else {
            Toast.makeText(this, "Computer turn", Toast.LENGTH_SHORT).show()
            computerTurn()
        }
    }

    private fun playerTurn() {

    }

    private fun computerTurn() {

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadResources() {
        drawCell[3] = context.resources.getDrawable(R.drawable.cell) //background
        //copy 2 image for 2 drawable player and bot
        drawCell[0] = null //empty cell
        drawCell[1] = context.resources.getDrawable(R.drawable.black_player) //drawable for player
        drawCell[2] = context.resources.getDrawable(R.drawable.white_player) //for bot
    }

    private fun designBoardGame() {

        val sizeofCell = (getScreenWidth() / boardSize)
        val IpRow: LinearLayout.LayoutParams = LinearLayout.LayoutParams(sizeofCell * boardSize, sizeofCell)
        val lpCell = LinearLayout.LayoutParams(sizeofCell, sizeofCell)
        val linBoardGame = findViewById<View>(R.id.linBoardGame) as LinearLayout

        for (i in 0 until boardSize) {
            val linRow = LinearLayout(context)
            //make a row
            for (j in 0 until boardSize) {
                ivCell[i][j] = ImageView(context)
                //make a cell
                //need to set background default for cell
                //cell has 3 status, empty(default),player,bot
                ivCell[i][j]!!.background = drawCell[3]
                //make that for safe and clear
//                ivCell[i][j].setOnClickListener(object : OnClickListener() {
//                    fun onClick(v: View) {
//                        if (valueCell[i][j] == 0) { //empty cell
//                            if (turnPlay == 1 || !isClicked) { //turn of player
//                                Log.d("tuanh", "click to cell ")
//                                isClicked = true
//                                xMove = i
//                                yMove = j //i,j must be final variable
//                                make_a_move()
//                            }
//                        }
//                    }
//                })
                linRow.addView(ivCell[i][j], lpCell)
            }
            linBoardGame.addView(linRow, IpRow)
        }
    }

//    private fun ScreenWidth(): Float {
//        val resources = context!!.resources
//        val dm = resources.displayMetrics
//        return dm.widthPixels.toFloat()
//    }

//    fun Activity.displayMetrics(): DisplayMetrics {
//        val displayMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(displayMetrics)
//        return displayMetrics
//    }

    fun getScreenWidth(): Int {
        val a = this.resources.displayMetrics.widthPixels
        return a - 20
    }

}












































