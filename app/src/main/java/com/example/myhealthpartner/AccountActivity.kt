package com.example.myhealthpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction

interface ChangeFragment{
    fun change(requestData : Int)
}

class AccountActivity : AppCompatActivity(), ChangeFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_page)
        initEvent()
    }

    override fun change(requestData: Int) {
        when(requestData){
            0 -> {
                val signinFragment = AccountPage_SignInFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, signinFragment).commit()
            }
            1 -> {
                val signupFragment1 = AccountPage_SignUp1Fragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, signupFragment1)
                transaction.addToBackStack(null).commitAllowingStateLoss()
            }
            4 -> {
                val findpwFragment = AccountPage_FindPwFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, findpwFragment).addToBackStack(null).commitAllowingStateLoss()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    fun initEvent(){
        val accountPageSigninFragment = AccountPage_SignInFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, accountPageSigninFragment).commit()
    }
}