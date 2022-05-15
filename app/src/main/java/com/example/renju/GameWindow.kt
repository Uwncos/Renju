package com.example.renju

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*


/*
СПИСОК ДЕЛ:
-найти уязвимость в ходе бота
-дизайн
-переворачивание экрана
-помечять выйгравшую пятерку цветом
 */



class GameWindow : AppCompatActivity() {

    private var Score = 0

    private val FILE_NAME = "content.txt"

    var boardSize = 15

    private var context = this

    private var btnPlayInGame: Button? = null
    private var turn: TextView? = null

    private var ivCell = Array(boardSize) {
        arrayOfNulls<ImageView>(
            boardSize
        )
    }

    private var valueCell = Array(boardSize) {
        IntArray(
            boardSize
        )
    }

    private var winnerPlay = 0
    private var lastWinnerPlay = 0
    private var firstMove = false
    private var xMove = 0
    private var yMove = 0
    private var turnPlay = 0

    private var isClicked = false

    private val drawCell = arrayOfNulls<Drawable>(12)



    @Override
    override fun onCreate(savedInstanceState: Bundle?) {

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.GameBarColor)
        //leaderCount add in bundle (https://www.youtube.com/watch?v=puj9OXs2iPM&list=PLRmiL0mct8WnodKkGLpBN0mfXIbAAX-Ux&index=9)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_window)
        context = this
        setListen()
        loadResources()
        boardSize = getDefaults()
        Log.d("boardSize", "$boardSize")
        valueCell = Array(boardSize) {
            IntArray(
                boardSize
            )
        }
        ivCell = Array(boardSize) {
            arrayOfNulls<ImageView>(
                boardSize
            )
        }
        Log.d("boardsize", "$boardSize")
        designBoardGame()
        init_game()
        play_game()
    }




    fun getDefaults(): Int {
        val size: Int
        val shared = getSharedPreferences("size_key", MODE_PRIVATE)
        if (shared == null) {
            size = 15
        } else {
            size = shared.getString("value", "")!!.toInt()
        }
        return size
    }

    private fun setListen() {
        btnPlayInGame = findViewById(R.id.reloadButton)
        turn = findViewById(R.id.turn)
        turn!!.text = "Turn:"

        btnPlayInGame!!.setOnClickListener {
            init_game()
            play_game()
        }
    }

    private fun init_game() {
        firstMove = true
        winnerPlay = 0

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
            xMove = boardSize / 2 + boardSize % 2
            yMove = boardSize / 2 + boardSize % 2
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
        val currentScoreView = findViewById<TextView>(R.id.ScoreDraw)
//        if (!checkWinner()) {
            ivCell[xMove][yMove]!!.setImageDrawable(drawCell[turnPlay])
            valueCell[xMove][yMove] = turnPlay

//        }

        if (notEmptyCell()) {
            return
        } else if (checkWinner()) {
            if (winnerPlay == 1) {
                val bestScore = viewText().toInt()
                turn!!.text = "You Win"
                Score += 1
                currentScoreView.text = Score.toString()
                if (Score > bestScore) {
                    saveText(Score)
                }
            } else {
                turn!!.text = "You Lose"

            }
            lastWinnerPlay = winnerPlay
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
        if (winnerPlay != 0) return true

        VectorEnd(xMove, 0, 0, 1, xMove, yMove)
        VectorEnd(0, yMove, 1, 0, xMove, yMove)
        if (xMove + yMove >= boardSize - 1) {
            VectorEnd(boardSize - 1, xMove + yMove - boardSize + 1, -1, 1, xMove, yMove)
        } else {
            VectorEnd(xMove + yMove, 0, -1, 1, xMove, yMove)
        }
        if (xMove <= yMove) {
            VectorEnd(xMove - yMove + boardSize - 1, boardSize - 1, -1, -1, xMove, yMove)
        } else {
            VectorEnd(boardSize - 1, boardSize - 1 - (xMove - yMove), -1, -1, xMove, yMove)
        }
        return winnerPlay != 0
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
        drawCell[0] = null //empty cell
        drawCell[1] = context.resources.getDrawable(R.drawable.black_player) //for player
        drawCell[2] = context.resources.getDrawable(R.drawable.white_player) //for bot
        drawCell[4] = context.resources.getDrawable(R.drawable.border_cell_1) //background
        drawCell[5] = context.resources.getDrawable(R.drawable.border_cell_2) //background
        drawCell[6] = context.resources.getDrawable(R.drawable.border_cell_3) //background
        drawCell[7] = context.resources.getDrawable(R.drawable.border_cell_4) //background
        drawCell[8] = context.resources.getDrawable(R.drawable.corner_cell_1) //background
        drawCell[9] = context.resources.getDrawable(R.drawable.corner_cell_2) //background
        drawCell[10] = context.resources.getDrawable(R.drawable.corner_cell_3) //background
        drawCell[11] = context.resources.getDrawable(R.drawable.corner_cell_4) //background
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
                if (i == boardSize - 1 && j in (1..boardSize - 2)) ivCell[i][j]!!.background = drawCell[4]
                else if (j == boardSize - 1 && i in (1..boardSize - 2)) ivCell[i][j]!!.background = drawCell[5]
                else if (i == 0 && j in (1..boardSize - 2)) ivCell[i][j]!!.background = drawCell[6]
                else if (j == 0 && i in (1..boardSize - 2)) ivCell[i][j]!!.background = drawCell[7]
                else if ((j == boardSize - 1 && i == boardSize - 1)) ivCell[i][j]!!.background = drawCell[8]
                else if ((j == 0 && i == boardSize - 1)) ivCell[i][j]!!.background = drawCell[9]
                else if ((j == 0 && i == 0)) ivCell[i][j]!!.background = drawCell[10]
                else if ((j == boardSize - 1 && i == 0)) ivCell[i][j]!!.background = drawCell[11]
                else ivCell[i][j]!!.background = drawCell[3]
                ivCell[i][j]!!.setOnClickListener {
                    if (valueCell[i][j] == 0) { //empty cell
                        if ((turnPlay == 1 || !isClicked) && !checkWinner()) { //turn of player
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
        if (winnerPlay != 0) return
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
                st = st.substring(1, 5)
            }
            i += vx
            j += vy
            if (!inBoard(i, j) || !inside(i, xbelow, xabove) ||
                !inside(j, ybelow, yabove) || winnerPlay != 0) {
                break
            }
        }
    }

    private fun inBoard(i: Int, j: Int): Boolean {
        return !(i < 0 || i > boardSize - 1 || j < 0 || j > boardSize - 1)
    }

    private fun EvalEnd(st: String) {
        when (st) {
            "11111" -> {
                winnerPlay = 1
            }
            "22222" -> {
                winnerPlay = 2
            }
            else -> {}
        }
    }


    private fun inside(i: Int, xbelow: Int, xabove: Int): Boolean {
        return (i - xbelow) * (i - xabove) <= 0
    }


    fun getScreenWidth(): Int {
        return resources.displayMetrics.widthPixels
    }


    private fun saveText(best_score: Int) {
        val text = best_score.toString()
        try {
            val fos = openFileOutput(FILE_NAME, MODE_PRIVATE)
            fos.write(text.toByteArray())
            fos.close()
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
