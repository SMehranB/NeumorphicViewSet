package com.smb.neumorphicview

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log.d
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smb.neumorphicview.databinding.ActivityMainBinding
import com.smb.neumorphicviewset.Jut
import com.smb.neumorphicviewset.NeumorphicButton
import com.smb.neumorphicviewset.NeumorphicSeekBar
import com.smb.neumorphicviewset.OnNeumorphicSeekBarChangeListener

class MainActivity : AppCompatActivity() {

    var disabled = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.neuSeekBar.min = -100
        binding.neuSeekBar.max = 100


        binding.neuSeekBar.setOnSeekBarProgressChanged(object : OnNeumorphicSeekBarChangeListener{
            override fun onProgressChanged(neuSeekBar: NeumorphicSeekBar?, progress: Int, fromUser: Boolean) {
                d("MMM", "On progress changed $progress%")
            }

            override fun onStartTrackingTouch(neuSeekBar: NeumorphicSeekBar?) {
//                d("MMM", "onStartTrackingTouch " + neuSeekBar?.progress)
            }

            override fun onStopTrackingTouch(neuSeekBar: NeumorphicSeekBar?) {
//                d("MMM", "onStopTrackingTouch " + neuSeekBar?.progress)
            }
        })

        val neuButton = NeumorphicButton(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        neuButton.apply {
            layoutParams = params
            setText("Mehran", 24, Color.CYAN)
            setBackgroundParams(Color.RED, 50)
            setDrawableParams(R.drawable.baseline_face_24, R.drawable.outline_shopping_cart_24, null, 16)
            setJutParams(Jut.LARGE)
            setDrawableDimension(30)
            setTextPaddings(16, 16)
            setTypeface(Typeface.BOLD_ITALIC, R.font.alsscrp)
            disabledTextColor = Color.BLUE
        }

        binding.viewHolder.addView(neuButton)

        binding.disableEnable.setOnClickListener {
            disabled = if (disabled) {
                binding.neuSeekBar.enable()

                neuButton.enable()
                binding.apply {
                    btnPlay.enable()
                    btnNeumorphic.enable()
                }
                !disabled
            } else {

                binding.neuSeekBar.disable()

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