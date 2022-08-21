package com.example.myhealthpartner

import android.view.MotionEvent
import android.view.View

class CommonUsedFunctionClass {
    fun changeBtnColor(myView: View, view: View, firstColor: Int, changedColor :Int){
        view.setOnTouchListener(
            View.OnTouchListener { myView, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                view.setBackgroundResource(changedColor)
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                view.setBackgroundResource(firstColor)
            }
            false
        })
    }


}