package com.example.myhealthpartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class AccountPage_SingUp2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_page_sign_up2_fragment, container, false)
        initEvent(view)
        return view
    }

    fun initEvent(myView : View){
        val changeFragment = context as ChangeFragment
        val nextBtn = myView.findViewById<Button>(R.id.nextBtn)
        nextBtn.setOnClickListener {
            changeFragment.change(3)
        }
    }

}