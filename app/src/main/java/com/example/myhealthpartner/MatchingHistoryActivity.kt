package com.example.myhealthpartner

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager

class MatchingHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.matcing_history_page)
        val matchingHistoryFragment = MatchingHistoryFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, matchingHistoryFragment).commit()
        initEvent()
    }

    fun navEvent(){
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navBtn = findViewById<ImageView>(R.id.navBtn)
        val navProfileBtn = findViewById<TextView>(R.id.navProfileBtn)
        val navmatchingBtn = findViewById<TextView>(R.id.navMatchingBtn)
        val navBoardBtn = findViewById<TextView>(R.id.navBoardBtn)
        val navHistoryBtn = findViewById<TextView>(R.id.navHistoryBtn)
        navBtn.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }
        navProfileBtn.setOnClickListener{
            val intent = Intent(applicationContext, MyProfilePageActivity::class.java)
            startActivity(intent)
            finish()
        }

        navHistoryBtn.setOnClickListener{

            drawerLayout.closeDrawer(GravityCompat.START)

        }

        navmatchingBtn.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        navBoardBtn.setOnClickListener{
            val intent = Intent(applicationContext, BoardPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun initEvent(){
        navEvent()

    }
}