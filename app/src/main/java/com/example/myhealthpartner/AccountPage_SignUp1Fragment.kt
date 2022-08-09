package com.example.myhealthpartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class AccountPage_SignUp1Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_page_sign_up1_fragment, container, false)
        val userData = initData()
        initEvent(view, userData)
        return view
    }

    fun initData(): UserData { //json파일 읽어오기 작업
        val jsonObject : String
        jsonObject = requireActivity().assets.open("userData.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val userData = gson.fromJson(jsonObject, UserData::class.java)
        return userData
    }


    fun initEvent(myView : View, userData : UserData){
        val dupCheck = false
        val dupCheckBtn = myView.findViewById<Button>(R.id.dupCheckBtn)
        val changeFragment = context as ChangeFragment
        val nextBtn = myView.findViewById<Button>(R.id.nextBtn)

        dupCheckBtn.setOnClickListener {
            for (index in 0 until userData.user.size) {

            }
        }
        nextBtn.setOnClickListener {
            changeFragment.change(2)

        }
    }

    data class UserData(
        val user : ArrayList<User>
    )

    data class User(
        val id : String,
        val pw : String,
        val userDataList : ArrayList<UserDataList>,
        val findUserDataList : ArrayList<FindUserDataList>

    )
    data class UserDataList(
        val name : String,
        val tel : String,
        val email : String
    )

    data class FindUserDataList(
        val nickname : String,
        val address : String,
        val exerciseType : String,
        val age : Int,
        val exerciseTime : String
    )
}