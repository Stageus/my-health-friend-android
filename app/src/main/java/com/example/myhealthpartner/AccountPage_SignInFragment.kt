package com.example.myhealthpartner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.w3c.dom.Text

class AccountPage_SignInFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_page_sign_in_fragment, container, false)
        initEvent(view)
        return view
    }

    fun initEvent(myView: View){
        val changeFragment = context as ChangeFragment
        val singinBtn = myView.findViewById<Button>(R.id.signInBtn)
        val signupBtn = myView.findViewById<Button>(R.id.signUpBtn)
        val findPwText = myView.findViewById<TextView>(R.id.findPwText)

        singinBtn.setOnClickListener {
            changeFragment.change(2)
        }
        signupBtn.setOnClickListener {
            changeFragment.change(1)
        }
        findPwText.setOnClickListener{
            changeFragment.change(4)
        }
    }
}