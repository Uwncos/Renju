package com.example.renju

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

//import javax.swing.text.View
//import javax.swing.text.html.ImageView


class GameWindow : AppCompatActivity() {


//    private var lDisplayMetrics = resources.displayMetrics
//    var widthPixels = lDisplayMetrics.widthPixels
//    var heightPixels = lDisplayMetrics.heightPixels


    private val boardSize = 15


    private var context: Context? = null

    private val ivCell = Array(boardSize) {
        arrayOfNulls<ImageView>(
            boardSize
        )
    }
    private val drawCell = arrayOfNulls<Drawable>(4)
//    private val btnPlay = Button(this)
//    private val tvTurn: TextView? = null


    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        //leaderCount add in bundle (https://www.youtube.com/watch?v=puj9OXs2iPM&list=PLRmiL0mct8WnodKkGLpBN0mfXIbAAX-Ux&index=9)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_window)
        designBoardGame()
        loadResources()
        context = this
    }

    private fun loadResources() {
        drawCell[3] = context!!.resources.getDrawable(R.drawable.cell) //background
        //copy 2 image for 2 drawable player and bot
        drawCell[0] = null //empty cell
        drawCell[1] = context!!.resources.getDrawable(R.drawable.black_player) //drawable for player
        drawCell[2] = context!!.resources.getDrawable(R.drawable.white_player) //for bot
    }

    @SuppressLint("NewApi")
    private fun designBoardGame() {

        val sizeofCell = (getScreenWidth() / boardSize)
        val IpRow: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(sizeofCell * boardSize, sizeofCell)
        val lpCell = LinearLayout.LayoutParams(sizeofCell, sizeofCell)
        val linBoardGame = findViewById<View>(R.id.linBoardGame) as LinearLayout

        for (i in 0 until boardSize) {
            val linRow = LinearLayout(context)
            //make a row
            for (j in 0 until boardSize) {
                ivCell[i][j] = ImageView(context)
                //make a cell
                //need to set background default for cell
                //cell has 3 status, empty(defautl),player,bot
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

    fun Activity.displayMetrics(): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().getDisplayMetrics().widthPixels
    }

}












































