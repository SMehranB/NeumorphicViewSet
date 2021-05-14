package com.smb.neumorphicview

import android.os.Bundle
import android.util.Log.d
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smb.neumorphicview.databinding.ActivityMainBinding
import com.smb.neumorphicviewset.*
import com.smb.neumorphicviewset.interfaces.OnNeuCheckBoxCheckedChangeListener
import com.smb.neumorphicviewset.interfaces.OnNeuRadioButtonCheckedChangeListener
import com.smb.neumorphicviewset.interfaces.OnNeuSeekBarChangeListener
import com.smb.neumorphicviewset.interfaces.OnNeuSwitchCheckedChangeListener

class MainActivity : AppCompatActivity() {

    private var disabled = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tempRG.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {

            }
        })

        binding.neuRadioGroup.onCheckedChangeListener = object : NeuRadioGroup.OnCheckedChangedListener{
            override fun onCheckChanged(neuRadioGroup: NeuRadioGroup, checkedId: Int) {
                d("MMM", checkedId.toString())
                d("MMM", neuRadioGroup.checkedNeuRadioButtonId.toString())
            }
        }

        binding.tempRD.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                d("MMM", "afmjsdnfkas")
            }

        })
        binding.rb1.setOnNeuCheckedChangeListener(object: OnNeuRadioButtonCheckedChangeListener{
            override fun onCheckedChanged(neuRadioButton: NeuRadioButton, checked: Boolean) {
//                d("MMM", "Radio $checked")
            }
        })
        binding.neuCheckBox.setOnNeuCheckedChangeListener(object: OnNeuCheckBoxCheckedChangeListener {
            override fun onCheckedChanged(neuCheckBox: NeuCheckBox, checked: Boolean) {
//                d("MMM", "Check box is $checked")
            }
        })

        binding.neuSwitch.setOnNeuCheckedChangeListener(object : OnNeuSwitchCheckedChangeListener {
            override fun onCheckedChanged(neuSwitch: NeuSwitch, checked: Boolean) {
//                d("MMM", "the switch is $checked")
            }
        })

        binding.neuSeekBar.setOnSeekBarProgressChanged(object : OnNeuSeekBarChangeListener {
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
                    neuCheckBox.enable()
                    neuSeekBar.enable()
                    neuSwitch.enable()
//                neuButton.enable()
                    btnPlay.enable()
                    btnNeumorphic.enable()


                }
                !disabled
            } else {

                binding.apply {
                    rb1.disable()
                    neuCheckBox.disable()
                    neuSeekBar.disable()
                    neuSwitch.disable()
//                neuButton.disable()
                    btnNeumorphic.disable()
                    btnPlay.disable()
                }
                !disabled
            }
        }

        binding.change.setOnClickListener {

            binding.neuRadioGroup.apply {
//                check(2131296559)
                clearCheck()
            }
        }

//        neuButton.setOnClickListener {
//            Toast.makeText(this, "neuButton Clicked", Toast.LENGTH_SHORT).show()
//        }
        binding.btnNeumorphic.setOnClickListener {
            Toast.makeText(this, "btnNeumorphic Clicked", Toast.LENGTH_SHORT).show()
        }
        binding.btnPlay.setOnClickListener {
            Toast.makeText(this, "btnPlay Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}