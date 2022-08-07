package com.example.myhealthpartner

import android.app.AlertDialog
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception
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

    fun getGeocodeFromAddress(address: String): Address {
        val coder = Geocoder(this)
        val geocodedAddress: List<Address> = coder.getFromLocationName(address, 50)
        Log.d("GmapViewFragment", "Geocode from Address ${geocodedAddress}${geocodedAddress.get(0).longitude}")
        return geocodedAddress[0]
    }

//    fun getDistanceFromCurrentLocation(current: LatLng, dest: LatLng): ArrayList<Int> {
//        val currentLocation = Location("현재위치")
//        currentLocation.latitude = current.latitude
//        currentLocation.longitude = current.longitude
//        val destLocation = Location("목적지")
//        destLocation.latitude = dest.latitude
//        destLocation.longitude = dest.longitude
//        val distance: Float = currentLocation.distanceTo(destLocation)
//        Log.d("GmapViewFragment","Current Lat: ${currentLocation.latitude}, Lng: ${currentLocation.longitude}")
//        return
//    }



    fun initEvent(){
        val createProfile = findViewById<Button>(R.id.createProfileBtn)
        createProfile.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        val findAdressBtn = findViewById<Button>(R.id.findAdressBtn)
        findAdressBtn.setOnClickListener{
            val dialogTemp1 = AlertDialog.Builder(this)
            val dialog1 = dialogTemp1.create()
            val dialogView = layoutInflater.inflate(R.layout.address_dialog1,null)
            val findBtn = dialogView.findViewById<Button>(R.id.findBtn)
            val editText = dialogView.findViewById<EditText>(R.id.questionTextView)
            val searchResultTextView = dialogView.findViewById<TextView>(R.id.addressResultTextView)
            val confirmBtn = dialogView.findViewById<Button>(R.id.confirmButton)
            dialog1.setView(dialogView)
            dialog1.show()
            findBtn.setOnClickListener{
                try{
                    val tempAddress = getGeocodeFromAddress("용정공원로 33")
                    searchResultTextView.text = tempAddress.getAddressLine(0)
                }
                catch(e: Exception) {
                    val dialogTemp2 = AlertDialog.Builder(this)
                    val dialog2 = dialogTemp2.create()
                    val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog,null)
                    val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
                    alertMessage.text = "유효하지 않은 주소\n(혹은 입력)입니다"
                    dialog2.setView(dialogViewTemp)
                    dialog2.show()
                    dialogViewTemp.findViewById<Button>(R.id.confirmButton).setOnClickListener{
                        dialog2.dismiss()
                    }
                }
            }
            confirmBtn.setOnClickListener{
                if (dialogView.findViewById<TextView>(R.id.addressResultTextView).text == "")
                {
                    val dialogTemp2 = AlertDialog.Builder(this)
                    val dialog2 = dialogTemp2.create()
                    val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog,null)
                    val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
                    alertMessage.text = "유효하지 않은 주소\n(혹은 입력)입니다"
                    dialog2.setView(dialogViewTemp)
                    dialog2.show()
                    dialogViewTemp.findViewById<Button>(R.id.confirmButton).setOnClickListener{
                        dialog2.dismiss()
                    }
                }
                else {
                    findViewById<TextView>(R.id.addressTextView).text = dialogView.findViewById<TextView>(R.id.addressResultTextView).text
                    dialog1.dismiss()
                }
            }
        }
    }
}