package com.example.myhealthpartner

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class MyProfilePageIntroduceFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.myprofile_page_introduce_fragment, container, false)
        initEvent(view)
        return view
    }

    fun initEvent(myView : View){
        val loginData = this.activity?.getSharedPreferences("loginData", 0)
        val introduce = loginData?.getString("introduce", "")!!
        val introduceTextView = myView.findViewById<TextView>(R.id.introduceTextView)
        introduceTextView.text = introduce
        Log.d("msg", loginData?.getString("introduce", "")!! )
    }
}