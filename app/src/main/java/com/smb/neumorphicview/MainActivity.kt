package com.smb.neumorphicview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var disabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disableEnable.setOnClickListener {
            disabled = if (disabled) {
                neuButton.enable()
                !disabled
            } else {
                neuButton.disable()
                !disabled
            }
        }

    }
}