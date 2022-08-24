package com.example.myhealthpartner

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

interface ChangeFragment{
    fun change(requestData : Int)
}

class AccountActivity : AppCompatActivity(), ChangeFragment {

    //위치 정보 불러오기 위한 변수들
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10

    var latTemp : Double? = null
    var lngTemp : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_page)
        mLocationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (checkPermissionForLocation(this)) {
            startLocationUpdates()
        }
    }

    override fun change(requestData: Int) { //페이지 이동을 위한 함수
        when(requestData){
            0 -> { //로그인페이지로
                val signinFragment = AccountPage_SignInFragment()
                val bundle = Bundle()
                bundle.putDouble("Lat", latTemp!!)
                bundle.putDouble("Lng", lngTemp!!)
                Log.d("It work ","${latTemp}")
                signinFragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, signinFragment).commit()
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            1 -> { //1번째 회원가입페이지
                val signupFragment1 = AccountPage_SignUp1Fragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, signupFragment1)
                transaction.addToBackStack(null).commitAllowingStateLoss()
            }
            2-> { //2번째 회원가입페이지
                val signup2Fragment = AccountPage_SingUp2Fragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, signup2Fragment)
                transaction.addToBackStack(null).commitAllowingStateLoss()
            }
            3 -> { //3번째 회원가입페이지
                val signup3Fragment = AccountPage_SignUp3Fragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, signup3Fragment)
                transaction.addToBackStack(null).commitAllowingStateLoss()

            }
            4 -> { //프로필 생성 페이지로
                val intent = Intent(applicationContext, ProfileCreate1Activity::class.java)
                intent.putExtra("Lat",latTemp)
                intent.putExtra("Lng",lngTemp)
                Log.d("koko1 : ", "${latTemp}")
                Log.d("koko2 : ", "${lngTemp}")
                startActivity(intent)
            }
            5 -> { // 메칭페이지로
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.putExtra("Lat",latTemp)
                intent.putExtra("Lng",lngTemp)
                Log.d("koko3 : ", "${latTemp}")
                Log.d("koko4 : ", "${lngTemp}")
                startActivity(intent)
            }
            6 -> { //pw찾기 페이지로
                val findpwFragment = AccountPage_FindPwFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, findpwFragment)
                transaction.addToBackStack(null).commitAllowingStateLoss()
            }
            7 ->{ //pw변경 페이지로
                val changePwFragment = AccountPage_ChangePwFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentBox, changePwFragment)
                transaction.addToBackStack(null).commitAllowingStateLoss()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun initEvent(){
        change(0)
    }


    private fun startLocationUpdates() {
        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        latTemp = mLastLocation.latitude // 갱신 된 위도
        lngTemp = mLastLocation.longitude // 갱신 된 경도
        initEvent()
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

}