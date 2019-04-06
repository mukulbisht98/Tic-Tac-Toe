package com.xxluciferinxx.tictactoe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.TextView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logo)
        val logoText = findViewById<TextView>(R.id.logo_text)
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val fadeInLtR = AnimationUtils.loadAnimation(this, R.anim.fade_in_ltr)
        bounce.interpolator = BounceInterpolator()
        logo.startAnimation(bounce)
        Handler().postDelayed({
            logoText.startAnimation(fadeInLtR)
            logoText.visibility = View.VISIBLE
        }, 1000)
        val staticTimeOut = 1800
        Handler().postDelayed({
            val homeIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(homeIntent)
            finish()
        }, staticTimeOut.toLong())
    }
}
