package com.example.myhealthpartner

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class TestNavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        initEvent()

    }

    fun initEvent(){
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val testBtn = findViewById<ImageButton>(R.id.navBtn)
        testBtn.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}