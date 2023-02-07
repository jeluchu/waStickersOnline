package com.jeluchu.wastickersonline.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.annotation.AnimRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeluchu.wastickersonline.R

class AnimatedRecyclerView : RecyclerView {
    private var orientation = LinearLayoutManager.VERTICAL
    private var reverse = false
    private var animationDuration = 600
    private var layoutManagerType = LayoutManagerType.LINEAR
    private var columns = 1

    @AnimRes
    private var animation = R.anim.layout_animation_from_bottom
    private var animationController: LayoutAnimationController? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, attrs)
    }

    @SuppressLint("Recycle", "WrongConstant")
    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.AnimatedRecyclerView, 0, 0)
        orientation = typedArray.getInt(
            R.styleable.AnimatedRecyclerView_layoutManagerOrientation,
            orientation
        )
        reverse =
            typedArray.getBoolean(R.styleable.AnimatedRecyclerView_layoutManagerReverse, reverse)
        animationDuration =
            typedArray.getInt(R.styleable.AnimatedRecyclerView_animationDuration, animationDuration)
        layoutManagerType =
            typedArray.getInt(R.styleable.AnimatedRecyclerView_layoutManagerType, layoutManagerType)
        columns =
            typedArray.getInt(R.styleable.AnimatedRecyclerView_gridLayoutManagerColumns, columns)
        animation = typedArray.getResourceId(R.styleable.AnimatedRecyclerView_layoutAnimation, -1)
        if (animationController == null) animationController =
            if (animation != -1) AnimationUtils.loadLayoutAnimation(
                getContext(),
                animation
            ) else AnimationUtils.loadLayoutAnimation(
                getContext(),
                R.anim.layout_animation_from_bottom
            )
        animationController!!.animation.duration = animationDuration.toLong()
        layoutAnimation = animationController
        if (layoutManagerType == LayoutManagerType.LINEAR) layoutManager = LinearLayoutManager(
            context,
            orientation,
            reverse
        ) else if (layoutManagerType == LayoutManagerType.GRID) layoutManager =
            GridLayoutManager(context, columns, orientation, reverse)
    }

    annotation class LayoutManagerType {
        companion object {
            var LINEAR = 0
            var GRID = 1
        }
    }
}