package com.example.myhealthpartner

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class AccountPage_SingUp2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_page_sign_up2_fragment, container, false)
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

    fun nameCheck(myView: View) : Boolean{
        val name = myView.findViewById<EditText>(R.id.nameEditText)
        if(name.text.toString() == ""){
            return false
        }
        else{
            return true
        }
    }
    //올바른 이메일 체크
    fun emailCheck(myView: View) : Boolean{
        val email = myView.findViewById<EditText>(R.id.mailEditText)
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return false
        }
        else return true
    }

    fun initEvent(myView : View){
        val changeFragment = context as ChangeFragment
        val nextBtn = myView.findViewById<Button>(R.id.nextBtn)
        val cuf = CommonUsedFunctionClass()
        cuf.changeBtnColor(myView,nextBtn,R.drawable.rounded_gray,R.drawable.rounded_silver)

        nextBtn.setOnClickListener {
            val nameChecked  = nameCheck(myView)
            val emailChecked = emailCheck(myView)
            if(nameChecked == true){
                if(emailChecked == true){
                    //이름체크와 이메일체크 모두 통과시
                    changeFragment.change(3)
                }
                else {
                    warningAlert("올바른 이메일을 \n입력해주세요.")
                }
            }
            else{
                warningAlert("이름을 입력해주세요.")
            }

        }
    }

}