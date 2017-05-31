package com.douban.malfurion.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.douban.malfurion.Monitor


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById(R.id.text) as TextView
        val btn = findViewById(R.id.button) as Button
        btn.setOnClickListener {
            view -> Monitor.onViewClick(view)
        }
    }
}
