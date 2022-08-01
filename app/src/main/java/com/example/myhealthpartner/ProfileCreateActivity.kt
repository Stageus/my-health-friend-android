package com.example.myhealthpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.FragmentTransaction



class ProfileCreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_profile_page)
        initEvent()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun initEvent(){
        val createProfile = findViewById<Button>(R.id.createProfileBtn)
        createProfile.setOnClickListener{
            Log.d("errorcode","error_1 detect")
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}