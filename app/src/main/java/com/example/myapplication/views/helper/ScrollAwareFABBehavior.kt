package com.example.myapplication.views.helper

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Created by nishant.patel on 24/08/16.
 */
class ScrollAwareFABBehavior(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior() {

  override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
    return true
  }

  override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
      child: FloatingActionButton,
      target: View, dxConsumed: Int, dyConsumed: Int,
      dxUnconsumed: Int, dyUnconsumed: Int) {
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)

    if (dyConsumed > 0) {
      val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
      val viewBottomMargin = layoutParams.bottomMargin
      child.animate().translationY((child.height + viewBottomMargin).toFloat()).setInterpolator(LinearInterpolator()).start()
    } else if (dyConsumed < 0) {
      child.animate().translationY(0f).setInterpolator(LinearInterpolator()).start()
    }
  }
}