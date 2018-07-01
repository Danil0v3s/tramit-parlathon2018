@file:JvmName("ExtensionsUtils")

package br.com.firstsoft.parlathon.util

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.view.components.GlideApp
import java.text.SimpleDateFormat

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun ImageView.loadImg(imageUrl: String?) {
    GlideApp.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.parlamentar_placeholder)
            .error(R.drawable.parlamentar_placeholder)
            .into(this)
}

fun <T> androidLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

@SuppressLint("SimpleDateFormat")
fun String.displayAsDate(pattern: String = "yyyy-MM-dd'T'HH:mm:ss", showHours: Boolean = false): String {
    return this.let {
        val dateFormatter = SimpleDateFormat(pattern)
        val date = dateFormatter.parse(this)
        if (showHours) {
            dateFormatter.applyPattern("dd/MM/yyyy HH:mm")
        } else {
            dateFormatter.applyPattern("dd/MM/yyyy")
        }
        dateFormatter.format(date)
    }
}

fun ImageView.setMarked(liked: Boolean) {
    val colorFrom: Int
    val colorTo: Int
    if (liked) {
        colorFrom = ContextCompat.getColor(context, R.color.colorPrimary)
        colorTo = Color.RED
    } else {
        colorFrom = Color.RED
        colorTo = ContextCompat.getColor(context, R.color.colorPrimary)
    }

    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.setDuration(250)
            .addUpdateListener { animator -> drawable.setColorFilter(animator.animatedValue as Int, PorterDuff.Mode.SRC_ATOP) }
    colorAnimation.start()
}

