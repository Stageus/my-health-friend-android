package com.example.myhealthpartner

import android.app.AlertDialog
import android.media.Image
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class MainPage_MatchingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_page_matching_fragment, container, false)
        val checkboxLinear = view.findViewById<LinearLayout>(R.id.checkboxLinear)
        checkboxLinear.visibility = View.GONE
        initEvent(view)
        return view
    }


    fun initEvent(myView: View) {
        val checkboxLinear = myView.findViewById<LinearLayout>(R.id.checkboxLinear)
        val fitnessListBtn = myView.findViewById<ImageButton>(R.id.fitnessListBtn)
        fitnessListBtn.setOnClickListener {
            if(checkboxLinear.visibility == View.GONE){
                checkboxLinear.visibility = View.VISIBLE
            }
            else{
                checkboxLinear.visibility = View.GONE
            }



        }
    }


}


