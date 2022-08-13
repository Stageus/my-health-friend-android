package com.example.myhealthpartner

import android.app.Dialog
import android.media.Image
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        initEvent()
    }


    fun initEvent(){
        val matchingFragment = MainPage_MatchingFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, matchingFragment).commit()
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val naviBtn = findViewById<ImageButton>(R.id.navBtn)
        naviBtn.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }

    }
}