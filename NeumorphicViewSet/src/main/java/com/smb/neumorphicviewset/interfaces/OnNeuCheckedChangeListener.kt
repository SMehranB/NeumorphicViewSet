package com.smb.neumorphicviewset.interfaces

import com.smb.neumorphicviewset.NeuSwitch

interface OnNeuCheckedChangeListener {
    fun onCheckedChanged(neuSwitch: NeuSwitch, checked: Boolean)
}