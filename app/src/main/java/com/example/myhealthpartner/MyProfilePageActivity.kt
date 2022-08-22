package com.example.myhealthpartner

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
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
        findViewById<TextView>(R.id.RPM).text = "${loginData.getInt("rpm", 0)}RPM"
        setAddress()

        //실제론 DB가 들어가는 부분
        if(findViewById<TextView>(R.id.nickname).text == "갓효석"){
            imageSetTestCode()
        }
        setRPM(loginData)
        setBadge(loginData)

    }

    //보안을 위한 주소입력
    fun setAddress(){
        val loginData = this.getSharedPreferences("loginData", 0)
        val oldAddress = loginData.getString("address","")
        var newAddress = ""
        var cuttingIndex = -1
        for (index in oldAddress!!.length-1 downTo 0) {
            if(oldAddress[index] == ' '){
                cuttingIndex = index
                break
            }
        }
        val indexRange = IntRange(0,cuttingIndex)
        newAddress = oldAddress.slice(indexRange)
        Log.d("dddddd ","${cuttingIndex}")
        findViewById<TextView>(R.id.address).text = "거주지역 - " + newAddress + "..."
    }

    fun imageSetTestCode(){
        val profileImage = findViewById<ImageView>(R.id.profileImage)

        profileImage.background
        profileImage.setPadding(0,0,0,0)
        Glide.with(this) //이미지 적용
            .load(R.mipmap.ronnie)
            .circleCrop()
            .into(profileImage)
    }

    fun setBadge(loginData: SharedPreferences){
        val badgeArrayList = arrayListOf<ImageView>()
        badgeArrayList.add(findViewById<ImageView>(R.id.badge1))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge2))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge3))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge4))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge5))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge6))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge7))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge8))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge9))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge10))

        //잘못 짠 코드, 근데 해결책을 모르겠다
        for (index in 0 until badgeArrayList.size) {
            val stringTemp = "badge" + (index+1).toString()
            val badgeData = loginData.getString(stringTemp, "")
            if(badgeData == "koala"){
                badgeArrayList[index].setImageResource(R.mipmap.koalabadge)
                badgeArrayList[index].setPadding(0,0,0,0)
            }
        }


    }

    fun setRPM(loginData: SharedPreferences) {
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
                0 -> if(originExercise!![index] == 'T') exercise += "파워리프팅"
                1 -> if(originExercise!![index] == 'T') exercise += " 보디빌딩"
                2 -> if(originExercise!![index] == 'T') exercise += " 크로스핏"
                3 -> if(originExercise!![index] == 'T') exercise += " 기타"
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
                0 -> if(originExercise!![index] == 'T') time += "오전"
                1 -> if(originExercise!![index] == 'T') time += " 오후"
                2 -> if(originExercise!![index] == 'T') time += " 밤"
                3 -> if(originExercise!![index] == 'T') time += " 새벽"
            }
        }
        return time
    }

    fun initEvent(){
        checkExercise()
        initData()
        findViewById<ImageButton>(R.id.editBtn).setOnClickListener{
            val intent = Intent(this, MyProfileEditPage::class.java)
            val loginData = this.getSharedPreferences("id", 0).toString()
            intent.putExtra("id",loginData)
            startActivity(intent)
            finish()
        }

    }
}