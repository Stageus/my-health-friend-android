package com.example.myhealthpartner

import android.app.AlertDialog
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class ProfileCreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_profile_page)
        initEvent()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun initEvent(){
        val createProfile = findViewById<Button>(R.id.createProfileBtn)
        createProfile.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        val findAdressBtn = findViewById<Button>(R.id.findAdressBtn)
        findAdressBtn.setOnClickListener{


            val dialogTemp = AlertDialog.Builder(this)
            val dialog = dialogTemp.create()
            val dialogView = layoutInflater.inflate(R.layout.address_dialog1,null)
            val findBtn = dialogView.findViewById<Button>(R.id.findBtn)
            val confirmBtn = dialogView.findViewById<Button>(R.id.confirmButton)
            dialog.setView(dialogView)
            dialog.show()
            findBtn.setOnClickListener{
                val addressInput = findViewById<EditText>(R.id.addressEditText)git
                val geocoder = Geocoder(this);
                val str: String = addressInput.getText().toString()
                var list = geocoder.getFromLocationName(str, 10)
                if (list != null) {
                    val city = ""
                    val country = ""
                    if (list.size == 0) {
                        //address_result.setText("올바른 주소를 입력해주세요. ")
                    } else {
                        val address: Address = list.get(0)
                        val lat: Double = address.getLatitude()
                        val lon: Double = address.getLongitude()
                    }
                }
                else{

                }
            }
            confirmBtn.setOnClickListener{
                dialog.dismiss()
            }

        }



    }
}