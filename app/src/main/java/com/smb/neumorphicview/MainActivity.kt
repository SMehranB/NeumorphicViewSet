package com.smb.neumorphicview

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smb.neumorphicviewset.NeumorphicButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var disabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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

        viewHolder.addView(neuButton)

        disableEnable.setOnClickListener {
            disabled = if (disabled) {
                neuButton.enable()
                btnNeumorphic.enable()

                !disabled
            } else {
                neuButton.disable()
                btnNeumorphic.disable()
                !disabled
            }
        }

        neuButton.setOnClickListener {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}