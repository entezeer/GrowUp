package com.growup.core.extensions

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.growup.growup.R

fun AppCompatActivity.setFragment(fragment: Fragment, frameId: Int, title: String) {
    supportFragmentManager.beginTransaction()
        .replace(frameId, fragment)
        .commitAllowingStateLoss()
    supportActionBar?.title = title
}

fun Activity.slideLeftIn() {
    this.overridePendingTransition(R.anim.right_in, R.anim.right_out)
}

fun Activity.slideRightOut() {
    this.overridePendingTransition(R.anim.left_in, R.anim.left_out)
}