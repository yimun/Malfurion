package com.douban.malfurion.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

/**
 * Created by linwei on 2017/6/4.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val linearLayout = findViewById(R.id.linear) as LinearLayout
        for (i in 1..10) {
            val button = Button(this)
            button.text = "BUTTON $i"
            button.setOnClickListener {
                v ->
                if (v is TextView) {
                    Toast.makeText(this, v.text.toString() + " clicked",
                            Toast.LENGTH_SHORT).show()
                }
            }
            linearLayout.addView(button)
        }
    }

}
