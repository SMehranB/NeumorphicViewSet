package com.smb.neumorphicviewset.interfaces

import com.smb.neumorphicviewset.NeuSeekBar

interface OnNeuSeekBarChangeListener {
    fun onProgressChanged(neuSeekBar: NeuSeekBar?, progress: Int, fromUser: Boolean)
    fun onStartTrackingTouch(neuSeekBar: NeuSeekBar?)
    fun onStopTrackingTouch(neuSeekBar: NeuSeekBar?)
}