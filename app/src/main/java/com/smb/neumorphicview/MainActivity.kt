package com.smb.neumorphicview

import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smb.neumorphicview.databinding.ActivityMainBinding
import com.smb.neumorphicviewset.*

class MainActivity : AppCompatActivity() {

    private var disabled = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.neuRadioGroup.onCheckedChangeListener = object : NeuRadioGroup.OnCheckedChangedListener{
            override fun onCheckChanged(neuRadioGroup: NeuRadioGroup, checkedId: Int) {
                d("MMM", checkedId.toString())
                d("MMM", neuRadioGroup.checkedNeuRadioButtonId.toString())
            }
        }

        binding.rb1.setOnNeuCheckedChangeListener(object: NeuRadioButton.OnCheckedChangeListener{
            override fun onCheckedChanged(neuRadioButton: NeuRadioButton, checked: Boolean) {
//                d("MMM", "Radio $checked")
            }
        })

        binding.neuCheckBox.setOnNeuCheckedChangeListener(object: NeuCheckBox.OnCheckedChangeListener {
            override fun onCheckedChanged(neuCheckBox: NeuCheckBox, checked: Boolean) {
//                d("MMM", "Check box is $checked")
            }
        })

        binding.neuSwitch.setOnNeuCheckedChangeListener(object : NeuSwitch.OnCheckedChangeListener {
            override fun onCheckedChanged(neuSwitch: NeuSwitch, checked: Boolean) {
//                d("MMM", "the switch is $checked")
            }
        })

        binding.neuSeekBar.setOnNeuCheckedChangeListener(object : NeuSeekBar.OnChangeListener {
            override fun onProgressChanged(neuSeekBar: NeuSeekBar?, progress: Int, fromUser: Boolean) {
//                d("MMM", "On progress changed $progress%")
            }

            override fun onStartTrackingTouch(neuSeekBar: NeuSeekBar?) {
//                d("MMM", "onStartTrackingTouch " + neuSeekBar?.progress)
            }

            override fun onStopTrackingTouch(neuSeekBar: NeuSeekBar?) {
//                d("MMM", "onStopTrackingTouch " + neuSeekBar?.progress)
            }
        })

//        val neuButton = NeuButton(this)
//        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//
//        neuButton.apply {
//            layoutParams = params
//            setText("Mehran", 24, Color.CYAN)
//            setBackgroundParams(Color.RED, 50)
//            setDrawableParams(R.drawable.baseline_face_24, R.drawable.outline_shopping_cart_24, null, 16)
//            setJutParams(Jut.LARGE)
//            setDrawableDimension(30)
//            setTextPaddings(16, 16)
//            setTypeface(Typeface.BOLD_ITALIC, R.font.alsscrp)
//            disabledTextColor = Color.BLUE
//        }
//
//        binding.viewHolder.addView(neuButton)

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