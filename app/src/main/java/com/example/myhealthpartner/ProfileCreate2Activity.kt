package com.example.myhealthpartner

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.NetworkOnMainThreadException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(emul, 18F))
        getAddressFromCode(mMap.cameraPosition.target)
        Log.d("ttt", "${mMap.cameraPosition.target}")
        addressOutput.text = getAddressFromCode(mMap.cameraPosition.target)

        val cuf = CommonUsedFunctionClass()
        val view = findViewById<View>(R.id.layout)
        cuf.changeBtnColor(view,findBtn,R.drawable.rounded_signature_purple2,R.drawable.rounded_signature_purple)

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
                latTemp = mMap.cameraPosition.target.latitude
                lngTemp = mMap.cameraPosition.target.longitude
            }
    }

    fun initEvent(){
        val view = findViewById<View>(R.id.layout)
        val confirmBtn = findViewById<Button>(R.id.button)
        val cuf = CommonUsedFunctionClass()
        cuf.changeBtnColor(view,confirmBtn,R.drawable.rounded_signature_purple2,R.drawable.rounded_signature_purple)

        confirmBtn.setOnClickListener{
            var addressOutput = findViewById<TextView>(R.id.address)//intent로 키는 activity는 중요치 않은 듯?
            val intent = Intent().apply {
                putExtra("address", addressOutput.text.toString())
                putExtra("Lat", latTemp)
                putExtra("Lng", lngTemp)
                Log.d("ttt1 : ","${latTemp}")
                Log.d("ttt1 : ","${lngTemp}")
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}