package com.xxluciferinxx.tictactoe

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    // components
    private var startScreen: RelativeLayout? = null
    private var backButton: ImageButton? = null
    private var logoText: TextView? = null
    private var playButton: Button? = null
    private var settings: Button? = null
    private var quit: Button? = null
    private var playArea: ConstraintLayout? = null
    private var playerX: TextView? = null
    private var playerO: TextView? = null
    private var button1: Button? = null
    private var button2: Button? = null
    private var button3: Button? = null
    private var button4: Button? = null
    private var button5: Button? = null
    private var button6: Button? = null
    private var button7: Button? = null
    private var button8: Button? = null
    private var button9: Button? = null
    private var reset: Button? = null
    private var settingsMenu: RelativeLayout? = null
    private var volumeSeekbar: SeekBar? = null

    // to close activity
    private var doubleBackPressed = false

    // for audio
    private var mediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null

    // for algorithm
    private var playerSwitch: Boolean? = null
    private var arr = Array(3) { CharArray(3) }

    // Click Listener for Board Button clicks
    @RequiresApi(Build.VERSION_CODES.N)
    private var playerSwitchMoves: View.OnClickListener = View.OnClickListener {
        val contDes = it.contentDescription
        val index1 = contDes[0].toInt() - 48
        val index2 = contDes[1].toInt() - 48
        playerSwitch = if (playerSwitch!!) {
            playerO!!.text = Html.fromHtml("<u>Player - O</u>", Html.FROM_HTML_MODE_LEGACY)
            playerX!!.text = Html.fromHtml("Player - X", Html.FROM_HTML_MODE_LEGACY)
            it!!.setBackgroundResource(R.drawable.x)
            arr[index1][index2] = 'X'
            false
        } else {
            playerX!!.text = Html.fromHtml("<u>Player - X</u>", Html.FROM_HTML_MODE_LEGACY)
            playerO!!.text = Html.fromHtml("Player - O", Html.FROM_HTML_MODE_LEGACY)
            it!!.setBackgroundResource(R.drawable.o)
            arr[index1][index2] = 'O'
            true
        }
        it.isClickable = false
        displayLog()
        if (won()) deactivateButtons()
    }


    // on create
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // to start music
        startMusic()

        // initializing components
        startScreen = findViewById(R.id.start_screen)
        playButton = findViewById(R.id.play)
        settings = findViewById(R.id.settings)
        quit = findViewById(R.id.quit)
        settingsMenu = findViewById(R.id.settings_menu)
        backButton = findViewById(R.id.back_button)
        logoText = findViewById(R.id.logo_text)
        playArea = findViewById(R.id.play_area)
        playerX = findViewById(R.id.player_x)
        playerO = findViewById(R.id.player_o)
        button1 = findViewById(R.id.but1)
        button2 = findViewById(R.id.but2)
        button3 = findViewById(R.id.but3)
        button4 = findViewById(R.id.but4)
        button5 = findViewById(R.id.but5)
        button6 = findViewById(R.id.but6)
        button7 = findViewById(R.id.but7)
        button8 = findViewById(R.id.but8)
        button9 = findViewById(R.id.but9)
        reset = findViewById(R.id.reset_button)

        //initializing values
        playerSwitch = true
        playerX!!.text = Html.fromHtml("<u>Player - X</u>", Html.FROM_HTML_MODE_LEGACY)
        arrayInitialize()

        //start screen listeners
        playButton!!.setOnClickListener {
            backButton!!.visibility = View.VISIBLE
            playArea!!.visibility = View.VISIBLE
            playArea!!.alpha = 0.0f
            playArea!!.animate()
                .alpha(1.0f)
                .setDuration(800)
                .setListener(null)
            startScreen!!.visibility = View.GONE
            logoText!!.translationX = 60F
        }
        settings!!.setOnClickListener {
            initControls()
            backButton!!.visibility = View.VISIBLE
            settingsMenu!!.visibility = View.VISIBLE
            settingsMenu!!.alpha = 0.0f
            settingsMenu!!.animate()
                .alpha(1.0f)
                .setDuration(800)
                .setListener(null)
            startScreen!!.visibility = View.GONE
            logoText!!.translationX = 60F
        }
        backButton!!.setOnClickListener {
            if (playArea!!.visibility == View.VISIBLE) {
                startScreen!!.visibility = View.VISIBLE
                startScreen!!.alpha = 0.0f
                startScreen!!.animate()
                    .alpha(1.0f)
                    .setDuration(800)
                    .setListener(null)
                playArea!!.visibility = View.GONE
            }
            if (settingsMenu!!.visibility == View.VISIBLE) {
                startScreen!!.visibility = View.VISIBLE
                startScreen!!.alpha = 0.0f
                startScreen!!.animate()
                    .alpha(1.0f)
                    .setDuration(800)
                    .setListener(null)
                settingsMenu!!.visibility = View.GONE
            }
            backButton!!.visibility = View.GONE
            logoText!!.translationX = 0F
        }
        quit!!.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Confirm Exit.")
            builder.setMessage("Do you want to Quit Tic-Tac-Toe?")
            builder.setPositiveButton("YES") { _, _ ->
                // Do something when user press the positive button
                moveTaskToBack(true)
                exitProcess(-1)
            }
            builder.setNegativeButton("No") { _, _ -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        // player board listeners
        reset!!.setOnClickListener {
            resetButtons()
        }
        button1!!.setOnClickListener(playerSwitchMoves)
        button2!!.setOnClickListener(playerSwitchMoves)
        button3!!.setOnClickListener(playerSwitchMoves)
        button4!!.setOnClickListener(playerSwitchMoves)
        button5!!.setOnClickListener(playerSwitchMoves)
        button6!!.setOnClickListener(playerSwitchMoves)
        button7!!.setOnClickListener(playerSwitchMoves)
        button8!!.setOnClickListener(playerSwitchMoves)
        button9!!.setOnClickListener(playerSwitchMoves)
    }

    // initialize array
    private fun arrayInitialize() {
        for (i in 0 until arr.size) {
            val tempArray = CharArray(3)
            for (j in 0 until tempArray.size) tempArray[j] = '-'
            arr[i] = tempArray
        }
        /*
        * - - -
        * - - -
        * - - -
        */
    }

    // Check for Winner
    @SuppressLint("SetTextI18n")
    private fun won(): Boolean {
        // Check Horizontal
        for (i in 0 until arr.size) {
            if (arr[i][0] == 'X' && arr[i][1] == 'X' && arr[i][2] == 'X') {
                logoText!!.text = "X WON!"
                return true
            } else if (arr[i][0] == 'O' && arr[i][1] == 'O' && arr[i][2] == 'O') {
                logoText!!.text = "O WON!!"
                return true
            }
        }
        // Check Vertical
        for (i in 0 until arr.size) {
            if (arr[0][i] == 'X' && arr[1][i] == 'X' && arr[2][i] == 'X') {
                logoText!!.text = "X WON!"
                return true
            } else if (arr[0][i] == 'O' && arr[1][i] == 'O' && arr[2][i] == 'O') {
                logoText!!.text = "O WON!!"
                return true
            }
        }
        // Check Diagonal - 1
        if (arr[0][0] == 'X' && arr[1][1] == 'X' && arr[2][2] == 'X') {
            logoText!!.text = "X WON!"
            return true
        } else if (arr[0][0] == 'O' && arr[1][1] == 'O' && arr[2][2] == 'O') {
            logoText!!.text = "O WON!!"
            return true
        }
        // Check Diagonal - 2
        if (arr[0][2] == 'X' && arr[1][1] == 'X' && arr[2][0] == 'X') {
            logoText!!.text = "X WON!"
            return true
        } else if (arr[0][2] == 'O' && arr[1][1] == 'O' && arr[2][0] == 'O') {
            logoText!!.text = "O WON!!"
            return true
        }
        // Check Draw
        if (checkDraw()) {
            logoText!!.text = "DRAW!!"
            return true
        }
        return false
    }

    // Check for Draw
    private fun checkDraw(): Boolean {
        var flag = true
        for (i in 0 until arr.size) {
            for (j in 0 until arr.size) {
                if (arr[i][j] == '-') {
                    flag = false
                    break
                }
            }
        }
        return flag
    }

    //display Array in Log
    private fun displayLog() {
        var str = ""
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                str += arr[i][j] + " "
            }
            str += " "
        }
        Log.i("arrayOut", str)
    }

    // reset button function
    @SuppressLint("SetTextI18n")
    private fun resetButtons() {
        button1!!.setBackgroundResource(R.color.trans0)
        button2!!.setBackgroundResource(R.color.trans0)
        button3!!.setBackgroundResource(R.color.trans0)
        button4!!.setBackgroundResource(R.color.trans0)
        button5!!.setBackgroundResource(R.color.trans0)
        button6!!.setBackgroundResource(R.color.trans0)
        button7!!.setBackgroundResource(R.color.trans0)
        button8!!.setBackgroundResource(R.color.trans0)
        button9!!.setBackgroundResource(R.color.trans0)
        button1!!.isClickable = true
        button2!!.isClickable = true
        button3!!.isClickable = true
        button4!!.isClickable = true
        button5!!.isClickable = true
        button6!!.isClickable = true
        button7!!.isClickable = true
        button8!!.isClickable = true
        button9!!.isClickable = true
        logoText!!.text = "Tic-Tac-Toe"
        arrayInitialize()
        displayLog()
    }

    // Deactivate Board Buttons
    private fun deactivateButtons() {
        button1!!.isClickable = false
        button2!!.isClickable = false
        button3!!.isClickable = false
        button4!!.isClickable = false
        button5!!.isClickable = false
        button6!!.isClickable = false
        button7!!.isClickable = false
        button8!!.isClickable = false
        button9!!.isClickable = false
    }

    // to initialize volume control
    private fun initControls() {
        try {
            volumeSeekbar = findViewById(R.id.volume_control)
            audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
            volumeSeekbar!!.max = audioManager!!
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            volumeSeekbar!!.progress = audioManager!!
                .getStreamVolume(AudioManager.STREAM_MUSIC)

            volumeSeekbar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(arg0: SeekBar) {}

                override fun onStartTrackingTouch(arg0: SeekBar) {}

                override fun onProgressChanged(arg0: SeekBar, progress: Int, arg2: Boolean) {
                    audioManager!!.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        progress, 0
                    )
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //start music
    private fun startMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.bg_raw)
        mediaPlayer!!.setOnPreparedListener {
            playbackNow()
        }
    }

    // pause music
    private fun pausePlayback() {
        mediaPlayer!!.pause()
    }

    // start or resume music
    private fun playbackNow() {
        mediaPlayer!!.setVolume(0.6f, 0.6f)
        mediaPlayer!!.start()
        mediaPlayer!!.isLooping = true
    }

    //on app focus loss
    override fun onPause() {
        super.onPause()
        pausePlayback()
    }

    //on app focus back
    override fun onResume() {
        super.onResume()
        playbackNow()
    }

    // close app when back double pressed
    override fun onBackPressed() {
        if (doubleBackPressed) {
            if (mediaPlayer != null) {
                mediaPlayer!!.stop()
            }
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
            return
        }
        this.doubleBackPressed = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_LONG).show()
        Handler().postDelayed({ doubleBackPressed = false }, 2000)
    }
}
