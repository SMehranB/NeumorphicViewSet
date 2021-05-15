package com.smb.neumorphicview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smb.neumorphicview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var disabled = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.disableEnable.setOnClickListener {
            disabled = if (disabled) {
                binding.apply {
                    rb1.enable()
                    rb2.enable()
                    btnMute.enable()
                    btnCheckOut.enable()
                    neuCheckBox.enable()
                    neuSeekBar.enable()
                    neuSwitch.enable()
                    btnPlay.enable()
                    btnToStart.enable()
                    btnToEnd.enable()
                    btnNeumorphic.enable()
                }
                !disabled
            } else {
                binding.apply {
                    rb1.disable()
                    rb2.disable()
                    btnMute.disable()
                    btnCheckOut.disable()
                    neuCheckBox.disable()
                    neuSeekBar.disable()
                    neuSwitch.disable()
                    btnNeumorphic.disable()
                    btnPlay.disable()
                    btnToStart.disable()
                    btnToEnd.disable()
                }
                !disabled
            }
        }

        binding.btnNeumorphic.setOnClickListener {
            Toast.makeText(this, "Add More Items to Cart Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.btnPlay.setOnClickListener {
            Toast.makeText(this, "btnPlay Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}