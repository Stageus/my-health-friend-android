package com.example.myhealthpartner

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
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

        //실제론 DB가 들어가는 부분
        if(findViewById<TextView>(R.id.nickname).text == "갓효석"){
            imageSetTestCode()
        }
        changeRPM(loginData)

    }

    fun imageSetTestCode(){
        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val scale = resources.displayMetrics.density
        profileImage.background
        val padding_5p = (20 * scale + 0.5f).toInt()
        profileImage.setPadding(0,0,0,0)
        Glide.with(this) //이미지 적용
            .load(R.mipmap.ronnie)
            .circleCrop()
            .into(profileImage)
    }

    fun changeRPM(loginData: SharedPreferences) {
        val tempRPM = loginData.getInt("rpm",0)
        if(tempRPM < 50) {
            findViewById<TextView>(R.id.RPM).setTextColor(resources.getColor(R.color.black, null))
            findViewById<ImageView>(R.id.rpmImage).setImageResource(R.mipmap.blackheart)
        }
        else if((tempRPM >= 50) && (tempRPM <70)){
            findViewById<TextView>(R.id.RPM).setTextColor(resources.getColor(R.color.bronze,null))
            findViewById<ImageView>(R.id.rpmImage).setImageResource(R.mipmap.bronzeheart)
        }
        else if((tempRPM >= 70) && (tempRPM <90)){
            findViewById<TextView>(R.id.RPM).setTextColor(resources.getColor(R.color.silver,null))
            findViewById<ImageView>(R.id.rpmImage).setImageResource(R.mipmap.silverheart)
        }
        else if((tempRPM >= 90) && (tempRPM <110)){
            findViewById<TextView>(R.id.RPM).setTextColor(resources.getColor(R.color.gold,null))
            findViewById<ImageView>(R.id.rpmImage).setImageResource(R.mipmap.goldheart)
        }
        else if((tempRPM >= 110) && (tempRPM <130)){
            findViewById<TextView>(R.id.RPM).setTextColor(resources.getColor(R.color.platinum,null))
            findViewById<ImageView>(R.id.rpmImage).setImageResource(R.mipmap.platinumheart)
        }
        else if((tempRPM >= 130) && (tempRPM <150)){
            findViewById<TextView>(R.id.RPM).setTextColor(resources.getColor(R.color.diamond,null))
            findViewById<ImageView>(R.id.rpmImage).setImageResource(R.mipmap.diaheart)
        }
        else if(tempRPM >= 150){
            findViewById<TextView>(R.id.RPM).setTextColor(resources.getColor(R.color.red,null))
            findViewById<ImageView>(R.id.rpmImage).setImageResource(R.mipmap.redheart)
        }
        else{
            findViewById<TextView>(R.id.RPM).setTextColor(resources.getColor(R.color.black,null))
            findViewById<ImageView>(R.id.rpmImage).setImageResource(R.mipmap.blackheart)
        }
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
        checkExercise()
        initData()
    }
}