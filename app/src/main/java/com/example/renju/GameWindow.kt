package com.example.renju

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*



class GameWindow : AppCompatActivity() {


    private var Score = 0
    private val FILE_NAME = "content.txt"
    var boardSize = 15
    private var btnPlayInGame: Button? = null
    private var turn: TextView? = null

    private var ivCell = Array(boardSize) {
        arrayOfNulls<ImageView>(
            boardSize
        )
    }

    private lateinit var valueCell: Array<IntArray>
//    private var valueCell = Array(boardSize) {
//        IntArray(
//            boardSize
//        )
//    }

    private var winnerPlay = 0
    private var lastWinnerPlay = 0
    private var firstMove = false
    private var xMove = 0
    private var yMove = 0
    private var xMove0 = -1
    private var turnPlay = 0
    private var isClicked = false
    private val drawCell = arrayOfNulls<Drawable>(14)

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        barColorChange()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_window)
        setListen()
        loadResources()
        boardSize = getDefaults()
//        Log.d("boardSize", "$boardSize")
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
        viewText()
        designBoardGame()
        initGame()
        playGame()
    }

    private fun barColorChange() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.GameBarColor)
    }


    private fun getDefaults(): Int {
        val size: Int
        val shared = getSharedPreferences("size_key", MODE_PRIVATE)
        val a = shared.contains("value")
        if (!a) {
            size = 15
        } else {
            size = shared.getString("value", "15")!!.toInt()
        }
        return size
    }

    private fun setListen() {
        btnPlayInGame = findViewById(R.id.reloadButton)
        turn = findViewById(R.id.turn)
        turn!!.text = "~~~"

        btnPlayInGame!!.setOnClickListener {
            xMove = 0
            yMove = 0
            xMove0 = -1
            initGame()
            playGame()
        }
    }

    private fun initGame() {
        firstMove = true
        winnerPlay = 0

        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                ivCell[i][j]!!.setImageDrawable(drawCell[0]) //default
                valueCell[i][j] = 0
            }
        }

    }

    private fun playGame() {
        val r = Random()
        turnPlay = r.nextInt(2) + 1
        if (turnPlay == 1) {
            playerTurn()
        } else {
            computerTurn()
        }
    }

    private fun playerTurn() {
        turn!!.text = getString(R.string.YourTern)
        firstMove = false
        isClicked = false
    }

    private fun computerTurn() {
        turn!!.text = getString(R.string.NotYourTern)
        if (firstMove) { //in center
            xMove = boardSize / 2
            yMove = boardSize / 2
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
//        Log.d("ListLx", listX[0].toString())
        var lx = listX[0]
        var ly = listY[0]
        var res = Int.MAX_VALUE - 10
        for (i in listX.indices) {
            val x = listX[i]
            val y = listY[i]
            valueCell[x][y] = 2
            val rr = valuePosition()
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

    private fun valuePosition(): Int {
        var rr = 0
        val pl = turnPlay
        for (i in 0 until boardSize) {
//            Log.d("WhileLog", "inPut")
            rr += checkValue(boardSize - 1, i, -1, 0, pl)
        }
        for (i in 0 until boardSize) {
            rr += checkValue(i, boardSize - 1, 0, -1, pl)
        }
        //cross right to left
        for (i in boardSize - 1 downTo 0) {
            rr += checkValue(i, boardSize - 1, -1, -1, pl)
        }
        for (i in boardSize - 2 downTo 0) {
            rr += checkValue(boardSize - 1, i, -1, -1, pl)
        }
        //cross left to right
        for (i in boardSize - 1 downTo 0) {
            rr += checkValue(i, 0, -1, 1, pl)
        }
        for (i in boardSize - 1 downTo 1) {
            rr += checkValue(boardSize - 1, i, -1, 1, pl)
        }
        return rr
    }

    private fun checkValue(xd: Int, yd: Int, vx: Int, vy: Int, pl: Int): Int {
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
                    rr += eval(st, pl)
                    st = st.substring(1, 6)
                }
            } else {
                break
            }
        }
        return rr
    }

    private fun makeMove() {
        val currentScoreView = findViewById<TextView>(R.id.ScoreDraw)

        if (turnPlay == 1) {
            ivCell[xMove][yMove]!!.setImageDrawable(drawCell[1])
        } else if (turnPlay == 2 && xMove0 != -1) {
            ivCell[xMove][yMove]!!.setImageDrawable(drawCell[11 + turnPlay])
        } else if (xMove0 == -1) {
            ivCell[xMove][yMove]!!.setImageDrawable(drawCell[2])
        }


        valueCell[xMove][yMove] = turnPlay


        if (notEmptyCell()) {
            return
        } else if (checkWinner()) {
            if (winnerPlay == 1) {
                val bestScore = viewText().toInt()
                turn!!.text = getString(R.string.YouWin)
                Score += 1
                currentScoreView.text = Score.toString()
                if (Score > bestScore) {
                    saveText(Score)
                }
            } else {
                Score = 0
                currentScoreView.text = Score.toString()
                turn!!.text = getString(R.string.YouLose)

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
        drawCell[0] = null //empty cell
        drawCell[1] = ContextCompat.getDrawable(this, R.drawable.black_player) //for player
        drawCell[2] = ContextCompat.getDrawable(this, R.drawable.white_player) //for bot
        drawCell[3] = ContextCompat.getDrawable(this, R.drawable.cell) //background
        drawCell[4] = ContextCompat.getDrawable(this, R.drawable.border_cell_1) //background
        drawCell[5] = ContextCompat.getDrawable(this, R.drawable.border_cell_2) //background
        drawCell[6] = ContextCompat.getDrawable(this, R.drawable.border_cell_3) //background
        drawCell[7] = ContextCompat.getDrawable(this, R.drawable.border_cell_4) //background
        drawCell[8] = ContextCompat.getDrawable(this, R.drawable.corner_cell_1) //background
        drawCell[9] = ContextCompat.getDrawable(this, R.drawable.corner_cell_2) //background
        drawCell[10] = ContextCompat.getDrawable(this, R.drawable.corner_cell_3) //background
        drawCell[11] = ContextCompat.getDrawable(this, R.drawable.corner_cell_4) //background
        drawCell[13] = ContextCompat.getDrawable(this, R.drawable.white_player_touch) //for bot last move
    }

    private fun designBoardGame() {

        val sizeofCell = (getScreenWidth() / boardSize)
        val lpRow: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(sizeofCell * boardSize, sizeofCell)
        val lpCell = LinearLayout.LayoutParams(sizeofCell, sizeofCell)
        val linBoardGame = findViewById<View>(R.id.linBoardGame) as LinearLayout

        for (i in 0 until boardSize) {
            val linRow = LinearLayout(this)
            //make a row
            for (j in 0 until boardSize) {
                ivCell[i][j] = ImageView(this)
                if (i == boardSize - 1 && j in (1..boardSize - 2)) ivCell[i][j]!!.background =
                    drawCell[4]
                else if (j == boardSize - 1 && i in (1..boardSize - 2)) ivCell[i][j]!!.background =
                    drawCell[5]
                else if (i == 0 && j in (1..boardSize - 2)) ivCell[i][j]!!.background = drawCell[6]
                else if (j == 0 && i in (1..boardSize - 2)) ivCell[i][j]!!.background = drawCell[7]
                else if ((j == boardSize - 1 && i == boardSize - 1)) ivCell[i][j]!!.background =
                    drawCell[8]
                else if ((j == 0 && i == boardSize - 1)) ivCell[i][j]!!.background = drawCell[9]
                else if ((j == 0 && i == 0)) ivCell[i][j]!!.background = drawCell[10]
                else if ((j == boardSize - 1 && i == 0)) ivCell[i][j]!!.background = drawCell[11]
                else ivCell[i][j]!!.background = drawCell[3]
                ivCell[i][j]!!.setOnClickListener {
                    if (valueCell[i][j] == 0) { //empty cell
                        if ((turnPlay == 1 || !isClicked) && !checkWinner()) { //turn of player
                            isClicked = true
                            if (xMove0 != -1) {
                                ivCell[xMove][yMove]!!.setImageDrawable(drawCell[2])
                            }
                            xMove = i
                            yMove = j
                            xMove0 = xMove
//                            Log.d("xy", "$xMove")
                            makeMove()

                        }

                    }
                }
                linRow.addView(ivCell[i][j], lpCell)
            }
            linBoardGame.addView(linRow, lpRow)
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
                evalEnd(st)
                st = st.substring(1, 5)
            }
            i += vx
            j += vy
            if (!inBoard(i, j) || !inside(i, xbelow, xabove) ||
                !inside(j, ybelow, yabove) || winnerPlay != 0
            ) {
                break
            }
        }
    }

    private fun inBoard(i: Int, j: Int): Boolean {
        return !(i < 0 || i > boardSize - 1 || j < 0 || j > boardSize - 1)
    }

    private fun evalEnd(st: String) {
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

    fun inside(i: Int, xbelow: Int, xabove: Int): Boolean {
        return (i - xbelow) * (i - xabove) <= 0
    }

    private fun getScreenWidth(): Int {
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

    fun viewText(): String {
        val line: String
        if (File("/data/user/0/com.example.renju/files", FILE_NAME).exists()) {
            val fileStream = openFileInput(FILE_NAME)
            val read = BufferedReader(InputStreamReader(fileStream))
            line = read.readLine()
        } else line = "0"
        return line
    }

//    private fun negaMax()


    private fun eval(st: String, pl: Int): Int {

//        Log.d("Player", pl.toString())
        val b1: Int
        val b2: Int
        if (pl == 1) {
            b1 = 2
            b2 = 1
        } else {  //bot
            b1 = 1
            b2 = 2
        }
//        Log.d("StLog", st)
        when (st) {


            "111110" -> return b1 * 100000000
            "011111" -> return b1 * 100000000
            "211111" -> return b1 * 100000000
            "111112" -> return b1 * 100000000
            "011112" -> return b1 * 10000000                //1000 shd less than 111112
            "211110" -> return b1 * 10000000                //100
            "011110" -> return b1 * 10000000
            "110111" -> return b1 * 1000000                //fix 1003  more here
            "111011" -> return b1 * 1000000                //fix 1003
            "101110" -> return b1 * 1000000                //1002
            "011101" -> return b1 * 1000000                //1002
            "011100" -> return b1 * 10000                //102
            "001110" -> return b1 * 10000               //102
            "210111" -> return b1 * 100000                //100
            "011011" -> return b1 * 100000                //fix
            "110110" -> return b1 * 100000                //fix
            "110112" -> return b1 * 100000                //fix
            "211011" -> return b1 * 100000                //100
            "211101" -> return b1 * 100000                //100
            "010100" -> return b1 * 100                //10
            "011000" -> return b1 * 100                //10
            "001100" -> return b1 * 100                //10
            "000110" -> return b1 * 100                //10
            "211000" -> return b1 * 10                //1
            "201100" -> return b1 * 10                //1
            "200110" -> return b1 * 10                //1
            "200011" -> return b1 * 10                //1  more here


            "222220" -> return b2 * -100000000
            "022222" -> return b2 * -100000000
            "122222" -> return b2 * -100000000
            "222221" -> return b2 * -100000000
            "022221" -> return b2 * -1000000                //-1000
            "122220" -> return b2 * -1000000                //-100
            "022220" -> return b2 * -1000000
            "220222" -> return b2 * -100000                //fix
            "222022" -> return b2 * -100000                //fix
            "202220" -> return b2 * -100000                //-1002
            "022202" -> return b2 * -100000                //-1002
            "022200" -> return b2 * -10000                //-102
            "002220" -> return b2 * -10000                //-102
            "120222" -> return b2 * -100000                //-100
            "022022" -> return b2 * -100000                //fix  -100
            "220220" -> return b2 * -100000                //fix  -100
            "220221" -> return b2 * -100000                //fix  -100
            "122022" -> return b2 * -100000                //-100
            "122202" -> return b2 * -100000                //-100
            "020200" -> return b2 * -100                //-10
            "022000" -> return b2 * -100                //-10
            "002200" -> return b2 * -100                //-10
            "000220" -> return b2 * -100                //-10
            "122000" -> return b2 * -10                //-1
            "102200" -> return b2 * -10                //-1
            "100220" -> return b2 * -10                //-1
            "100022" -> return b2 * -10                //-1

            else -> {}
        }
        return 0
    }
}
