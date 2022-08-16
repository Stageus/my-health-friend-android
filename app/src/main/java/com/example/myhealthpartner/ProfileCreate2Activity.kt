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
import android.widget.ImageButton
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
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraIdleListener {
    private lateinit var mMap: GoogleMap

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10

    var latTemp : Double? = null
    var lngTemp : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_profile_map_page)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initEvent()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mLocationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        mMap = googleMap
        mMap.setOnCameraMoveListener (this)
        mMap.setOnCameraIdleListener (this)

        if (checkPermissionForLocation(this)) {
            startLocationUpdates()
        }

        val setCurrentLocationBtn = findViewById<ImageButton>(R.id.currentLocationBtn)
        setCurrentLocationBtn.setOnClickListener{
            val emul = LatLng(latTemp as Double, lngTemp as Double)
            mMap.clear()
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(emul, 20F))
            onCameraMove()
            getAddressFromCode(mMap.cameraPosition.target)
            Log.d("ttt", "${mMap.cameraPosition.target}")
            var addressOutput = findViewById<TextView>(R.id.address)
            addressOutput.text = getAddressFromCode(mMap.cameraPosition.target)
        }

        val findBtn = findViewById<Button>(R.id.findBtn)
        val addressOutput = findViewById<TextView>(R.id.address)
        val addressInput = findViewById<TextView>(R.id.inputAddress)

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

                val text1 = findViewById<TextView>(R.id.showLatLng)
                text1.text = "lat lng is :" +mMap.cameraPosition.target.latitude + ", "+mMap.cameraPosition.target.longitude + "\n삭제 예정 텍스트입니다."

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


    private fun startLocationUpdates() {
        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        mLastLocation = location
        latTemp = mLastLocation.latitude.toDouble() // 갱신 된 위도
        lngTemp = mLastLocation.longitude.toDouble() // 갱신 된 경도
    }

    private fun checkPermissionForLocation(context: Context): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()

            } else {
                Log.d("ttt", "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(this, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCameraMove() {
        val text1 = findViewById<TextView>(R.id.showLatLng)
        text1.text = "lat lng is :" +mMap.cameraPosition.target.latitude + ", "+mMap.cameraPosition.target.longitude
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
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}