package com.example.myhealthpartner

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.regex.Pattern


class AccountPage_SignUp3Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_page_sign_up3_fragment, container, false)
        initEvent(view)
        return view
    }

    fun warningAlert(msg : String){
        val dialogTemp2 = AlertDialog.Builder(context)
        val dialog2 = dialogTemp2.create()
        val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog,null)
        val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
        alertMessage.text = msg
        dialog2.setView(dialogViewTemp)
        dialog2.show()
        dialogViewTemp.findViewById<Button>(R.id.confirmButton).setOnClickListener{
            dialog2.dismiss()
        }
    }

    fun checkTel(myView: View) : Boolean{
        val tel = myView.findViewById<EditText>(R.id.phoneNumEditText)
        if(!Pattern.matches("^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$",tel.text.toString())){
            warningAlert("올바른 전화번호를 입력해주세요!")
            return false
        }

        else{
            tel.isEnabled = false
            tel.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.bright_silver))
            return true
        }
    }



    fun initEvent(myView : View){
        var telChecked = false

        val changeFragment = context as ChangeFragment
        val signupBtn = myView.findViewById<Button>(R.id.signUpBtn)
        val certificationLayout = myView.findViewById<LinearLayout>(R.id.certificationLayout)
        val getCertificationBtn = myView.findViewById<Button>(R.id.getCertificationBtn)
        certificationLayout.visibility = View.GONE

        getCertificationBtn.setOnClickListener {
            telChecked = checkTel(myView)
            if(telChecked == true){
                certificationLayout.visibility = View.VISIBLE
            }

        }


        signupBtn.setOnClickListener {
            if(telChecked == true){
                warningAlert("회원가입 성공!")
                changeFragment.change(0)
            }
            else{
                warningAlert("전화번호인증을 해주세요!")
            }

        }


    }
}