package com.example.myhealthpartner

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import org.w3c.dom.Text

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
        val loginData = this.getSharedPreferences("loginData", 0)
        val logoutTextView = findViewById<TextView>(R.id.logoutTextView)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val testBtn = findViewById<ImageButton>(R.id.navBtn)
        logoutTextView.setOnClickListener{
            val dialogTemp2 = AlertDialog.Builder(this)
            val dialog2 = dialogTemp2.create()
            val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog_yes_no,null)
            val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
            alertMessage.text = "로그아웃 하시겠습니까?"
            dialog2.setView(dialogViewTemp)
            dialog2.show()
            dialogViewTemp.findViewById<Button>(R.id.yesBtn).setOnClickListener{
                finish()
                loginData.edit().clear().apply()
                dialog2.dismiss()
            }
            dialogViewTemp.findViewById<Button>(R.id.noBtn).setOnClickListener {
                dialog2.dismiss()
            }
        }
        testBtn.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }
        val mainpageMatchingFragment = MainPage_MatchingFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, mainpageMatchingFragment).commit()

        val mainpageMatchRecieveFragment = MainPage_Match_Recieve_Fragment()
        val matchRecieveBtn = findViewById<ImageButton>(R.id.notiBtn)
        matchRecieveBtn.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, mainpageMatchRecieveFragment).commit()
        }
    }
}