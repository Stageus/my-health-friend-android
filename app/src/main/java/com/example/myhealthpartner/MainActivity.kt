package com.example.myhealthpartner

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import org.w3c.dom.Text

interface ChangeMainpageFragment{
    fun change(requestData : Int)
    fun change(requestData : Int,userExerciseChecked : String,userTimeChecked : String)
}


class MainActivity : AppCompatActivity(),ChangeMainpageFragment {
    var latTemp : Double? = null
    var lngTemp : Double? = null
    private lateinit var getResultAddress : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        latTemp = intent.getSerializableExtra("Lat") as? Double
        lngTemp = intent.getSerializableExtra("Lng") as? Double
        val mainpageMatchingFragment = MainPage_MatchingFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, mainpageMatchingFragment).commit()
        Log.d("Lat : ", "${latTemp}")
        Log.d("Lng : ", "${lngTemp}")
        initEvent()
    }
    var fragmentNum = 0
    override fun change(requestData: Int) {
        //버튼 보이게, 혹은 안보이게 조작하기
        fragmentNum = requestData
        when(requestData){
            0 -> { //기본 매칭프래그먼트
                val mainFragment = MainPage_MatchingFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, mainFragment).commit()
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            1 -> { //매칭결과 프래그먼트 -  안 쓰임
                val matchResultFragment = MainPage_Matching_Result_fragment()
                val bundle = Bundle()
                bundle.putDouble("Lat", latTemp!!)
                bundle.putDouble("Lng", lngTemp!!)
                matchResultFragment.arguments = bundle
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, matchResultFragment)
                transaction.addToBackStack(null).commitAllowingStateLoss()
            }
            2 -> { // 받은 매칭 프래그먼트
                val recieveFragment = MainPage_Match_Recieve_Fragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, recieveFragment)
                transaction.addToBackStack(null).commitAllowingStateLoss()
            }
        }
    }
    override fun change(requestData: Int,userExerciseChecked : String,userTimeChecked : String) {
        //버튼 보이게, 혹은 안보이게 조작하기
        fragmentNum = requestData
        when(requestData){
            1 -> { //매칭결과 프래그먼트 - 쓰임
                val matchResultFragment = MainPage_Matching_Result_fragment()
                val bundle = Bundle()
                bundle.putDouble("Lat", latTemp!!)
                bundle.putDouble("Lng", lngTemp!!)
                bundle.putString("timeChecked", userExerciseChecked)
                bundle.putString("exerciseChecked", userTimeChecked)
                matchResultFragment.arguments = bundle
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, matchResultFragment)
                transaction.addToBackStack(null).commitAllowingStateLoss()
            }
        }
    }

    fun navEvent(){
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navBtn = findViewById<ImageButton>(R.id.navBtn)
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
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        navHistoryBtn.setOnClickListener{
            val intent = Intent(applicationContext, MatchingHistoryActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        navmatchingBtn.setOnClickListener{
            val count = supportFragmentManager.backStackEntryCount
            if(count == 0 ){
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            else{
                val mainpageMatchingFragment = MainPage_MatchingFragment()
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, mainpageMatchingFragment)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        navBoardBtn.setOnClickListener{
            val intent = Intent(applicationContext, BoardPageActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    fun initEvent(){
        val loginData = this.getSharedPreferences("loginData", 0)
        val logoutTextView = findViewById<TextView>(R.id.logoutTextView)
        val matchRecieveBtn = findViewById<ImageButton>(R.id.notiBtn)
        navEvent()

        //네비게이션의 로그아웃 버튼
        logoutTextView.setOnClickListener{
            logoutEvent()
        }

        matchRecieveBtn.setOnClickListener{
            val intent = Intent(applicationContext, MatchingRecieveActivity::class.java)
            intent.putExtra("Lat",latTemp)
            intent.putExtra("Lng",lngTemp)
            startActivity(intent)
        }

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
            finish()
            loginData.edit().clear().apply()
            dialog2.dismiss()
        }
        dialogViewTemp.findViewById<Button>(R.id.noBtn).setOnClickListener {
            dialog2.dismiss()
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        Log.d("test", count.toString())
        if(count == 0){
            logoutEvent()
        }
        else{
            super.onBackPressed()
        }
    }
}