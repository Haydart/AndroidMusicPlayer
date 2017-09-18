package pl.rmakowiecki.simplemusicplayer.util

import android.view.animation.Animation

open class AnimationListenerAdapter : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation) = Unit

    override fun onAnimationEnd(animation: Animation) = Unit

    override fun onAnimationRepeat(animation: Animation) = Unit
}