package com.example.myhealthpartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

class AccountPage_FindPwFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_page_find_pw_fragment, container, false)
        val pwEdit = view.findViewById<EditText>(R.id.pwEditText)
        val pwcheckEdit = view.findViewById<EditText>(R.id.pwCheckEditText)
        pwEdit.visibility = View.GONE
        pwcheckEdit.visibility = View.GONE
        initEvent(view)
        return view
    }
    fun initEvent(view: View){
        val changeBtn = view.findViewById<Button>(R.id.re)
        changeBtn.setOnClickListener{
            val idEdit = view.findViewById<EditText>(R.id.idEditText)
            val nameEdit = view.findViewById<EditText>(R.id.nameEditText)
            //idEdit.setBackgroundResource()
            idEdit.isEnabled =false
            nameEdit.isEnabled =false

            val pwEdit = view.findViewById<EditText>(R.id.pwEditText)
            val pwcheckEdit = view.findViewById<EditText>(R.id.pwCheckEditText)
            pwEdit.visibility = View.VISIBLE
            pwcheckEdit.visibility = View.VISIBLE
        }
    }
}