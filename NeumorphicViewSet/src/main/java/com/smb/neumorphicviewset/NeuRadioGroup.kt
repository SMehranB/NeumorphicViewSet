package com.smb.neumorphicviewset

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class NeuRadioGroup : LinearLayout {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)


    private val radioButtons: MutableMap<Int, NeuRadioButton> = mutableMapOf()
    var checkedNeuRadioButtonId: Int = -1
    private var checkedRadioButton: NeuRadioButton? = null

    var onCheckedChangeListener: OnCheckedChangedListener? = null

    interface OnCheckedChangedListener {
        fun onCheckChanged(neuRadioGroup: NeuRadioGroup, checkedId: Int)
    }

    override fun onViewAdded(child: View?) {

        if (child is NeuRadioButton) {
            if (child.isChecked) {
                checkedRadioButton = child
                checkedNeuRadioButtonId = child.id
            }

            child.setOnClickListener {
                changeCheckedTo(it as NeuRadioButton)
            }

            radioButtons[child.id] = child
        }

        super.onViewAdded(child)
    }

    override fun onViewRemoved(child: View?) {

        if (child is NeuRadioButton) {
            changeCheckedTo(null)
            radioButtons.remove(child.id)
        }

        super.onViewRemoved(child)
    }

    private fun changeCheckedTo(neuRadioButton: NeuRadioButton?) {

        neuRadioButton?.let {
            it.isChecked = true
            it.isCheckable = false
            checkedNeuRadioButtonId = it.id

            if (checkedRadioButton != it) {
                checkedRadioButton?.apply {
                    isChecked = false
                    isCheckable = true
                }
                onCheckedChangeListener?.onCheckChanged(this@NeuRadioGroup, neuRadioButton.id)
            }
        }?: run {
            checkedNeuRadioButtonId = -1
            checkedRadioButton?.apply {
                isChecked = false
                isCheckable = true
            }
        }

        checkedRadioButton = neuRadioButton
    }

    fun check(id: Int) {
        changeCheckedTo(radioButtons[id])
    }

    fun clearCheck() {
        changeCheckedTo(null)
    }
}