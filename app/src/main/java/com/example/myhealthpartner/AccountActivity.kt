package com.example.myhealthpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_page)
        initEvent()
    }

    fun initEvent(){
        val accountPageSigninFragment = AccountPage_SignInFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, accountPageSigninFragment).commit()


    }
}