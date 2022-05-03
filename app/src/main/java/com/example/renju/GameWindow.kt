package com.example.renju

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class GameWindow : AppCompatActivity() {

    companion object {
        const val BEST_SCORE = "BEST_SCORE"
    }

    private var bestScore = 0


    private val boardSize = 15
    private var context = this

    //    private var btnPlay1: Button? = null
    private var btnPlayInGame: Button? = null
    private var turn: TextView? = null
    private var bestScoreView: TextView? = null

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

    private var isClicked = false

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BEST_SCORE, bestScore)
    }

    private fun setListen() {
//        btnPlay1 = findViewById(R.id.mainButton1)
        btnPlayInGame = findViewById(R.id.reloadButton)
        turn = findViewById(R.id.turn)
        turn!!.text = "Turn:"

//        btnPlay1!!.setOnClickListener {
//            init_game()
//            play_game()
//        }
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
        } else {
            Toast.makeText(this, "Computer turn", Toast.LENGTH_SHORT).show()
            computerTurn()
        }
    }

    private fun playerTurn() {
        Log.d("tuanh", "player turn")
        turn!!.text = "Tern: You"
        firstMove = false
        isClicked = false
    }

    private fun computerTurn() {
        turn!!.text = "Tern: Computer"
        if (firstMove) { //center
            xMove = 7
            yMove = 7
            firstMove = false
            makeMove()
        } else {
            findComputerMove()
            makeMove()
        }
    }

    private val iRow = intArrayOf(-1, -1, -1, 0, 1, 1, 1, 0)
    private val iCol = intArrayOf(-1, 0, 1, 1, 1, 0, -1, -1)

    private fun findComputerMove() {
            val listX: MutableList<Int> = ArrayList()
            val listY: MutableList<Int> = ArrayList()
            //find empty cell can move, and we we only move two cell in range 2
            val range = 2
            for (i in 0 until boardSize) {
                for (j in 0 until boardSize) if (valueCell[i][j] != 0) { //not empty
                    for (t in 1..range) {
                        for (k in 0..7) {
                            val x = i + iRow[k] * t
                            val y = j + iCol[k] * t
                            if (inBoard(x, y) && valueCell[x][y] == 0) {
                                listX.add(x)
                                listY.add(y)
                            }
                        }
                    }
                }
            }
        var lx = listX[0]
        var ly = listY[0]
        //bot always find min board_position_value
        var res = Int.MAX_VALUE - 10
        for (i in listX.indices) {
            val x = listX[i]
            val y = listY[i]
            valueCell[x][y] = 2
            val rr = value_Position()
            if (rr < res) {
                res = rr
                lx = x
                ly = y
            }
            valueCell[x][y] = 0
        }
        xMove = lx
        yMove = ly
    }

    private fun value_Position(): Int {
        var rr = 0
        var pl = turnPlay
        for (i in 0 until boardSize) {
            rr += CheckValue(boardSize - 1, i, -1, 0, pl)
        }
        for (i in 0 until boardSize) {
            rr += CheckValue(i, boardSize - 1, 0, -1, pl)
        }
        //cross right to left
        for (i in boardSize - 1 downTo 0) {
            rr += CheckValue(i, boardSize - 1, -1, -1, pl)
        }
        for (i in boardSize - 2 downTo 0) {
            rr += CheckValue(boardSize - 1, i, -1, -1, pl)
        }
        //cross left to right
        for (i in boardSize - 1 downTo 0) {
            rr += CheckValue(i, 0, -1, 1, pl)
        }
        for (i in boardSize - 1 downTo 1) {
            rr += CheckValue(boardSize - 1, i, -1, 1, pl)
        }
        return rr
    }

    private fun CheckValue(xd: Int, yd: Int, vx: Int, vy: Int, pl: Int): Int {
        var i: Int = xd
        var j: Int = yd
        var rr = 0
        var st = valueCell[i][j].toString()
        while (true) {
            i += vx
            j += vy
            if (inBoard(i, j)) {
                st += valueCell[i][j].toString()
                if (st.length == 6) {
                    rr += Eval(st, pl)
                    st = st.substring(1, 6)
                }
            } else break
        }
        return rr
    }


    private fun makeMove() {
        ivCell[xMove][yMove]!!.setImageDrawable(drawCell[turnPlay])
//        val dd = LayerDrawable(arrayOf(drawCell[3], drawCell[turnPlay]))
//        ivCell[xMove][yMove]!!.background = dd
        valueCell[xMove][yMove] = turnPlay

        if (notEmptyCell()) {
            return
        } else if (checkWinner()) {
            if (winner_play == 1) {
                turn!!.text = "You Win"
                bestScore += 1
                bestScoreView = findViewById(R.id.bestScoreDraw)
                bestScoreView!!.text = "$bestScore"
            } else {
                turn!!.text = "You Lose"
            }
            return
        }

        if (turnPlay == 1) {
            turnPlay = 3 - turnPlay
            computerTurn()
        } else {
            turnPlay = 3 - turnPlay
            playerTurn()
        }
    }

    private fun checkWinner(): Boolean {
        if (winner_play != 0) return true

        VectorEnd(xMove, 0, 0, 1, xMove, yMove)
        VectorEnd(0, yMove, 1, 0, xMove, yMove)
        if (xMove + yMove >= boardSize - 1) {
            VectorEnd(boardSize - 1, xMove + yMove - boardSize + 1, -1, 1, xMove, yMove)
        } else {
            VectorEnd(xMove + yMove, 0, -1, 1, xMove, yMove)
        }
        if (xMove <= yMove) {
            VectorEnd(xMove - yMove + boardSize - 1, boardSize - 1, -1, -1, xMove, yMove)
        } else{
            VectorEnd( boardSize - 1, boardSize - 1 - (xMove - yMove), -1, -1, xMove, yMove)
        }

        return winner_play != 0
    }

    private fun notEmptyCell(): Boolean {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (valueCell[i][j] == 0) {
                    return false
                }
            }
        }
        return true
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
                //cell has 3 status, empty(default),player,bot
                ivCell[i][j]!!.background = drawCell[3]
                //make that for safe and clear
                ivCell[i][j]!!.setOnClickListener {
                    if (valueCell[i][j] == 0) { //empty cell
                        if (turnPlay == 1 || !isClicked) { //turn of player
                            isClicked = true
                            xMove = i
                            yMove = j
                            makeMove()
                        }
                    }
                }
                linRow.addView(ivCell[i][j], lpCell)
            }
            linBoardGame.addView(linRow, IpRow)
        }
    }

    private fun VectorEnd(xx: Int, yy: Int, vx: Int, vy: Int, rx: Int, ry: Int) {
        //this void will check the row base on vector(vx,vy) in range (rx,ry)-4*(vx,vy) -> (rx,ry)+4*(vx,vy)
        //ok i will explain this :) hope you understand :D if not yet feel free to comment below i will help you
        if (winner_play != 0) return
        val range = 4
        var i: Int
        var j: Int
        val xbelow = rx - range * vx
        val ybelow = ry - range * vy
        val xabove = rx + range * vx
        val yabove = ry + range * vy
        var st = ""
        i = xx
        j = yy
        while (!inside(i, xbelow, xabove) || !inside(j, ybelow, yabove)) {
            i += vx
            j += vy
        }
        while (true) {
            st += valueCell[i][j].toString()
            if (st.length == 5) {
                EvalEnd(st)
                st = st.substring(1, 5) //substring of st from index 1->5;=> delete first character
            }
            i += vx
            j += vy
            if (!inBoard(i, j) || !inside(i, xbelow, xabove) || !inside(
                    j,
                    ybelow,
                    yabove
                ) || winner_play != 0
            ) {
                break
            }
        }
    }

    private fun inBoard(i: Int, j: Int): Boolean {
        return !(i < 0 || i > boardSize - 1 || j < 0 || j > boardSize - 1)
    }

    private fun EvalEnd(st: String) {
        when (st) {
            "11111" -> winner_play = 1
            "22222" -> winner_play = 2
            else -> {}
        }
    }


    private fun inside(i: Int, xbelow: Int, xabove: Int): Boolean {
        return (i - xbelow) * (i - xabove) <= 0
    }


    fun getScreenWidth(): Int {
        val a = this.resources.displayMetrics.widthPixels
        return a
    }

    private fun Eval(st: String, pl: Int): Int {
        //this function is put score for 6 cells in a row
        //pl is player turn => you will get a bonus point if it's your turn
        //I will show you and explain how i can make it and what it mean in part improve bot move
        var b1 = 1
        var b2 = 1
        if (pl == 1) {
            b1 = 2
            b2 = 1
        } else {
            b1 = 1
            b2 = 2
        }
        when (st) {
            "111110" -> return b1 * 100000000
            "011111" -> return b1 * 100000000
            "211111" -> return b1 * 100000000
            "111112" -> return b1 * 100000000
            "011110" -> return b1 * 10000000
            "101110" -> return b1 * 1002
            "011101" -> return b1 * 1002
            "011112" -> return b1 * 1000
            "011100" -> return b1 * 102
            "001110" -> return b1 * 102
            "210111" -> return b1 * 100
            "211110" -> return b1 * 100
            "211011" -> return b1 * 100
            "211101" -> return b1 * 100
            "010100" -> return b1 * 10
            "011000" -> return b1 * 10
            "001100" -> return b1 * 10
            "000110" -> return b1 * 10
            "211000" -> return b1 * 1
            "201100" -> return b1 * 1
            "200110" -> return b1 * 1
            "200011" -> return b1 * 1
            "222220" -> return b2 * -100000000
            "022222" -> return b2 * -100000000
            "122222" -> return b2 * -100000000
            "222221" -> return b2 * -100000000
            "022220" -> return b2 * -10000000
            "202220" -> return b2 * -1002
            "022202" -> return b2 * -1002
            "022221" -> return b2 * -1000
            "022200" -> return b2 * -102
            "002220" -> return b2 * -102
            "120222" -> return b2 * -100
            "122220" -> return b2 * -100
            "122022" -> return b2 * -100
            "122202" -> return b2 * -100
            "020200" -> return b2 * -10
            "022000" -> return b2 * -10
            "002200" -> return b2 * -10
            "000220" -> return b2 * -10
            "122000" -> return b2 * -1
            "102200" -> return b2 * -1
            "100220" -> return b2 * -1
            "100022" -> return b2 * -1
            else -> {}
        }
        return 0
    }


}
