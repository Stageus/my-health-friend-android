package com.example.myhealthpartner

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
        val logoutTextView = findViewById<TextView>(R.id.logoutTextView)
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

        logoutTextView.setOnClickListener{
            logoutEvent()
        }
    }
    fun initEvent(){
        navEvent()

    }

    fun logoutEvent()
    {
        val loginData = this.getSharedPreferences("loginData", 0)
        val dialogTemp2 = AlertDialog.Builder(this)
        val dialog2 = dialogTemp2.create()
        val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog_yes_no,null)
        val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
        alertMessage.text = "로그아웃 하시겠습니까?"
        dialog2.setView(dialogViewTemp)
        dialog2.show()
        dialogViewTemp.findViewById<Button>(R.id.yesBtn).setOnClickListener{
            val i = Intent(this, AccountActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            loginData.edit().clear().apply()
            dialog2.dismiss()
        }
        dialogViewTemp.findViewById<Button>(R.id.noBtn).setOnClickListener {
            dialog2.dismiss()
        }
    }
}