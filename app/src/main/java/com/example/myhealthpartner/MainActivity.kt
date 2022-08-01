package com.example.myhealthpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

interface ChangeMainpageFragment{
    fun change(requestData : Int)
}

class MainActivity : AppCompatActivity(),ChangeMainpageFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        initEvent()
    }

    override fun change(requestData: Int) {
        //버튼 보이게, 혹은 안보이게 조작하기
        when(requestData){
            0 -> {
                val matchingFragment = MainPage_MatchingFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, matchingFragment).commit()
            }
            1 -> {
                val matchingFragment = MainPage_Matching_Result_fragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, matchingFragment).commit()
            }
            2 -> {
                val matchingFragment = MainPage_Matching_Result_fragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, matchingFragment).commit()
            }
        }
    }

    fun initEvent(){
        val mainpageMatchingFragment = MainPage_MatchingFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, mainpageMatchingFragment).commit()

        val mainpageMatchRecieveFragment = MainPage_Match_Recieve_Fragment()
        val matchRecieveBtn = findViewById<ImageButton>(R.id.recieveMatchBtn)
        matchRecieveBtn.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, mainpageMatchRecieveFragment).commit()
        }
    }
}