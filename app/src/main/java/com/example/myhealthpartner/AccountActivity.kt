package com.example.myhealthpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            }
            1 -> {
                val signupFragment1 = AccountPage_SignUp1Fragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, signupFragment1)
                transaction.addToBackStack(null).commitAllowingStateLoss()
            }
            2-> {
                val signup2Fragment = AccountPage_SingUp2Fragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, signup2Fragment)
                transaction.addToBackStack(null).commitAllowingStateLoss()
            }
            3 -> {
                val signup3Fragment = AccountPage_SignUp3Fragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, signup3Fragment)
                transaction.addToBackStack(null).commitAllowingStateLoss()

            }
            4 -> {
                val intent = Intent(applicationContext, ProfileCreateActivity::class.java)
                startActivity(intent)
            }
            5 -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
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

    fun signinSequence(){
        //shared preference 세팅
        //만약 통과를 했고, 첫번째 접속하는 계정이라면,
        Log.d("error code:","error detect")
        val intent = Intent(this,ProfileCreateActivity::class.java)
        //intent.putExtra("userid", )
        startActivity(intent)
        // 만약 통과를 했고, 두번째 접속하는 계정이라면,
        //val intent = Intent(this,ProfileCreateActivity::class.java)
        //intent.putExtra("userid", )
        //startActivity(intent)
    }

}