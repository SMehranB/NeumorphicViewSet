package com.smb.neumorphicviewset

interface OnNeumorphicSeekBarChangeListener {
    fun onProgressChanged(neuSeekBar: NeumorphicSeekBar?, progress: Int, fromUser: Boolean)
    fun onStartTrackingTouch(neuSeekBar: NeumorphicSeekBar?)
    fun onStopTrackingTouch(neuSeekBar: NeumorphicSeekBar?)
}