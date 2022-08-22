package com.example.myhealthpartner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStateManagerControl
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    val tab1Fragment = MyProfilePageIntroduceFragment()
    val tab2Fragment = MyProfilePageVideoFragment()
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0) return tab1Fragment
        else return tab2Fragment
    }
}


class MyProfilePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myprofile_page)

        val viewPager2 = findViewById<ViewPager2>(R.id.fragmentBox)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        val viewPagerFragment = ViewPagerAdapter(this)
        viewPager2.adapter = viewPagerFragment

        val tabTitles = listOf<String>("상세소개", "나의 운동 영상")
        TabLayoutMediator(tabLayout, viewPager2, {tab, position -> tab.text = tabTitles[position]}).attach()
        initEvent()
    }

    @SuppressLint("SetTextI18n")
    fun initData() {
        val loginData = this.getSharedPreferences("loginData", 0)
        val exerciseType = checkExercise()
        val exerciseTime = checkTime()

        findViewById<TextView>(R.id.nickname).text = loginData.getString("nickname","")
        findViewById<TextView>(R.id.career).text = "운동구력 - ${loginData.getString("career","")}"
        findViewById<TextView>(R.id.genre).text = "관심장르 - $exerciseType"
        findViewById<TextView>(R.id.ability).text = "수행능력 - ${loginData.getString("ability", "")}"
        findViewById<TextView>(R.id.time).text = "운동시간 - $exerciseTime"
        findViewById<TextView>(R.id.address).text = "주소 - ${loginData.getString("address", "")}"
        findViewById<TextView>(R.id.RPM).text = "${loginData.getInt("rpm", 0)}RPM"



    }

    fun checkExercise() : String{
        val loginData = this.getSharedPreferences("loginData", 0)
        var exercise = ""
        val originExercise = loginData.getString("exerciseType", "")
        for(index in 0 until 4){
            when(index) {
                1 -> if(originExercise!![index] == 'T') exercise += "파워리프팅"
                2 -> if(originExercise!![index] == 'T') exercise += " 보디빌딩"
                3 -> if(originExercise!![index] == 'T') exercise += " 크로스핏"
                4 -> if(originExercise!![index] == 'T') exercise += " 기타"
            }
        }
        return exercise
    }

    fun checkTime() : String{
        val loginData = this.getSharedPreferences("loginData", 0)
        var time = ""
        val originExercise = loginData.getString("exerciseTime", "")
        for(index in 0 until 4){
            when(index) {
                1 -> if(originExercise!![index] == 'T') time += "오전"
                2 -> if(originExercise!![index] == 'T') time += " 오후"
                3 -> if(originExercise!![index] == 'T') time += " 밤"
                4 -> if(originExercise!![index] == 'T') time += " 새벽"
            }
        }
        return time
    }

    fun initEvent(){
        val changProfileBtn = findViewById<Button>(R.id.changeProfileBtn)
//        changProfileBtn.setOnClickListener {
//            val intent = Intent(applicationContext, ProfileCreate1Activity::class.java)
//            intent.putExtra("Lat",latTemp)
//            intent.putExtra("Lng",lngTemp)
//            startActivity(intent)
//
//        }
        checkExercise()

        initData()
    }
}