package com.example.myhealthpartner

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.NetworkOnMainThreadException
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.io.IOException

class ProfileCreate2Activity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener {
    private lateinit var mMap: GoogleMap

    var latTemp : Double? = null
    var lngTemp : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_profile_map_page)
        latTemp = intent.getSerializableExtra("Lat") as Double
        lngTemp = intent.getSerializableExtra("Lng") as Double
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Thread {
            initEvent()
        }.start()
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.setOnCameraIdleListener (this)

        var addressOutput = findViewById<TextView>(R.id.address)
        val findBtn = findViewById<Button>(R.id.findBtn)
        val addressInput = findViewById<TextView>(R.id.inputAddress)

        //시작하자마자 자기 위치 중심으로 맵 생성
        val emul = LatLng(latTemp as Double, lngTemp as Double)
        mMap.clear()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(emul, 20F))
        getAddressFromCode(mMap.cameraPosition.target)
        Log.d("ttt", "${mMap.cameraPosition.target}")
        addressOutput.text = getAddressFromCode(mMap.cameraPosition.target)


        findBtn.setOnClickListener{
            try{
                val tempAddress = getGeocodeFromAddress(addressInput.text.toString())
                addressOutput.text = tempAddress.getAddressLine(0)
                val temp = LatLng(tempAddress.latitude, tempAddress.longitude)
                val bound = LatLngBounds(
                    temp,  // SW bounds
                    temp // NE bounds
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bound, 0))
            }
            catch(e: Exception) {
            }
        }
    }


    fun getGeocodeFromAddress(address: String): Address {
        val coder = Geocoder(this)
        val geocodedAddress: List<Address> = coder.getFromLocationName(address, 50)
        Log.d("GmapViewFragment", "Geocode from Address ${geocodedAddress}${geocodedAddress.get(0).longitude}")
        return geocodedAddress[0]
    }


    fun getAddressFromCode(latLng: LatLng): String{
        try{
            val coder = Geocoder(this)
            var addressTemp = coder.getFromLocation(latLng.latitude, latLng.longitude,1)
            Log.d("ttt", "  ${addressTemp[0].getAddressLine(0)}")
            return addressTemp[0].getAddressLine(0)
        }
        catch(e: IndexOutOfBoundsException)
        {
            return ""
        }
        catch (e: IOException)
        {
            return ""
        }
        catch (e : NetworkOnMainThreadException){
            return ""
        }
    }


    override fun onCameraIdle() {
            val temp = getAddressFromCode(mMap.cameraPosition.target)
            if (temp == "") {
                //pass
            }
            else{
                var addressOutput = findViewById<TextView>(R.id.address)
                addressOutput.text = temp
            }
    }

    fun initEvent(){
        val createBtn = findViewById<Button>(R.id.button)
        createBtn.setOnClickListener{
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
            var addressOutput = findViewById<TextView>(R.id.address)

            val intent = Intent(this, ProfileCreate1Activity::class.java).apply {
                putExtra("address", addressOutput.text.toString())
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}