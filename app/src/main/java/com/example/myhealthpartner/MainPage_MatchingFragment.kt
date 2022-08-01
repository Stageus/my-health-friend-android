package com.example.myhealthpartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class MainPage_MatchingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_page_matching_fragment, container, false)
        initEvent(view)
        return view
    }
    fun initEvent(myView: View){
        val matchingstartBtn = myView.findViewById<Button>(R.id.matchingstartBtn)
        val changeMainpageFragment = context as ChangeMainpageFragment

        matchingstartBtn.setOnClickListener{
            changeMainpageFragment.change(2)
        }
    }
}