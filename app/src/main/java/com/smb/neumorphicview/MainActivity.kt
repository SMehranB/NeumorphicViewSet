package com.smb.neumorphicview

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smb.neumorphicview.databinding.ActivityMainBinding
import com.smb.neumorphicviewset.NeumorphicButton

class MainActivity : AppCompatActivity() {

    var disabled = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val neuButton = NeumorphicButton(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        neuButton.apply {
            layoutParams = params
            setText("Mehran", 24, Color.CYAN)
            setBackgroundParams(Color.RED, 50)
            setDrawableParams(R.drawable.baseline_face_24, R.drawable.outline_shopping_cart_24, null, 16)
            setJutParams(NeumorphicButton.Jut.LARGE)
            setDrawableDimension(30)
            setTextPaddings(16, 16)
            setTypeface(Typeface.BOLD_ITALIC)
            disabledTextColor = Color.BLUE
        }

        binding.viewHolder.addView(neuButton)

        binding.disableEnable.setOnClickListener {
            disabled = if (disabled) {
                neuButton.enable()
                binding.apply {
                    btnPlay.enable()
                    btnNeumorphic.enable()
                }
                !disabled
            } else {
                neuButton.disable()
                binding.apply {
                    btnNeumorphic.disable()
                    btnPlay.disable()
                }
                !disabled
            }
        }

        neuButton.setOnClickListener {
            Toast.makeText(this, "neuButton Clicked", Toast.LENGTH_SHORT).show()
        }
        binding.btnNeumorphic.setOnClickListener {
            Toast.makeText(this, "btnNeumorphic Clicked", Toast.LENGTH_SHORT).show()
        }
        binding.btnPlay.setOnClickListener {
            Toast.makeText(this, "btnPlay Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}