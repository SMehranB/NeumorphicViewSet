package com.smb.neumorphicviewset.interfaces

import android.animation.Animator

interface MyAnimatorListener: Animator.AnimatorListener {
    override fun onAnimationStart(p0: Animator?) {
    }

    override fun onAnimationEnd(p0: Animator?) {
    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationRepeat(p0: Animator?) {
    }
}