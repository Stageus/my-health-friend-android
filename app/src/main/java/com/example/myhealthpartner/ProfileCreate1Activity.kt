package com.example.myhealthpartner

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.NetworkOnMainThreadException
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*
import java.util.jar.Manifest


class ProfileCreate1Activity : AppCompatActivity() {
    private lateinit var progressDialog : AppCompatDialog
    private lateinit var getResultAddress : ActivityResultLauncher<Intent>
    lateinit var imageResult : ActivityResultLauncher<Intent>

    var latTemp : Double? = null
    var lngTemp : Double? = null
    //이미지를 결과값을 받는 변수



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getResultAddress = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode == RESULT_OK){
                val addressText = findViewById<TextView>(R.id.addressText)
                val addressString = result.data?.getStringExtra("address")
                addressText.text = addressString
                latTemp = result.data?.getSerializableExtra("Lat") as Double?
                lngTemp = result.data?.getSerializableExtra("Lng") as Double?
                Log.d("ttt1", "  ${latTemp}")
                Log.d("ttt2", "  ${lngTemp}")
            }
        }
        //실행할 때 현재 위치 전페이지서 전달받아오기
        setContentView(R.layout.create_profile_page)
        latTemp = intent.getSerializableExtra("Lat") as Double
        lngTemp = intent.getSerializableExtra("Lng") as Double

        val checkboxLinear = findViewById<LinearLayout>(R.id.checkboxLinear)
        checkboxLinear.visibility = View.GONE
        val profileImage = findViewById<ImageView>(R.id.profileImage)
        profileImage.visibility = View.GONE
        var imageFile : File //이미지를 파일형태로 저장 -> 추후 서버에 보낼때 보낼 변수

        imageResult = registerForActivityResult( //oncreate에서만 정의가능 -> 모듈화 불가능
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            if(result.resultCode == RESULT_OK){ // 이미지를 선택하면..
                progressOff()
                //uri형태로 받아온 데이터 저장
                val imageUri = result.data?.data
                imageUri.let{
                    imageFile = File(getRealPathFromURI(it!!)) //서버 업로드를 위한 파일형태 변경
                    Glide.with(this) //이미지 적용
                        .load(imageUri)
                        .circleCrop()
                        .into(findViewById(R.id.profileImage))
                }
            }else { //갤러리창에서 뒤로가기할 시
                progressOff()
            }
        }
        initEvent()
    }

    companion object{
        const val REQ_GALLERY = 1

    }
    //로딩바 시작
    fun progressOn(){
        progressDialog = AppCompatDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(R.layout.loading_progress)
        progressDialog.show()

    }
    //로딩바 종료
    fun progressOff(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss()
        }
    }

    //이미지의 실제 경로를위한 함수
    fun getRealPathFromURI(uri : Uri) : String{
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")){
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)
        if(cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    //갤러리 접근을 위한 함수
    private fun selectGallery(){
        progressOn()
        val readPermission = ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if(readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQ_GALLERY
            )
        }else{
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*")
            imageResult.launch(intent)
        }


    }

    //위도 경도로 주소 반환
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

    //경고메시지
    fun alertDialog(alertMessageTemp :String){
        val dialogTemp = AlertDialog.Builder(this)
        val dialog = dialogTemp.create()
        val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog,null)
        val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
        alertMessage.text = alertMessageTemp
        dialog.setView(dialogViewTemp)
        dialog.show()
        dialogViewTemp.findViewById<Button>(R.id.confirmButton).setOnClickListener {
            dialog.dismiss()
        }
    }



    fun initEvent(){
        val checkboxLinear = findViewById<LinearLayout>(R.id.checkboxLinear)
        val fitnessListBtn = findViewById<ImageButton>(R.id.fitnessListBtn)
        val setprofileBtn = findViewById<Button>(R.id.setProfileBtn)
        val profileImage = findViewById<ImageView>(R.id.profileImage)

        //ui 시각, 비시각화
        setprofileBtn.setOnClickListener {
            selectGallery()
            setprofileBtn.visibility = View.GONE
            profileImage.visibility = View.VISIBLE
        }

        //프로필 이미지 넣기
        profileImage.setOnClickListener{
            try {
                selectGallery()
            }
            catch (e : Exception){
                alertDialog("사진 불러오기 실패")
            }

        }

        //장르 목록
        fitnessListBtn.setOnClickListener {
            if(checkboxLinear.visibility == View.GONE){
                checkboxLinear.visibility = View.VISIBLE
                fitnessListBtn.setImageResource(R.drawable.triangle)
            }
            else{
                checkboxLinear.visibility = View.GONE
                fitnessListBtn.setImageResource(R.drawable.reverse_triangle)

            }
        }

        //중복체크에 필요한 변수들
        val dupCheckBtn = findViewById<Button>(R.id.dupCheckBtn)
        val jsonObject : String
        jsonObject = assets.open("userData.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val userData = gson.fromJson(jsonObject,UserData::class.java)
        val nickNameEditText = findViewById<EditText>(R.id.nickNameEditText)
        var dupCheckValue = false
        var nickNameUseAble = false

        val view = findViewById<View>(R.id.layout)
        val cuf = CommonUsedFunctionClass()
        cuf.changeBtnColor(view,dupCheckBtn,R.drawable.rounded_gray,R.drawable.rounded_silver)


        //닉네임 중복체크
        dupCheckBtn.setOnClickListener {
            for(index in 0 until userData.user.size){
                if(userData.user[index].findUserDataList[0].nickname.toString() == nickNameEditText.text.toString()) {
                    //하나라도 중복된게 있다면
                    dupCheckValue = true
                }
                Log.d("ddd: ","${userData.user[index].findUserDataList[0].nickname }")
            }
            if ((dupCheckValue == false) && (nickNameEditText.text.length > 1))
            {
                alertDialog("사용가능합니다!")
                nickNameEditText.isEnabled = false
                nickNameEditText.setBackgroundResource(R.drawable.rounded_bright_silver)
                nickNameUseAble = true
            }
            else {
                alertDialog("이미 있거나 사용불가한 \n닉네임입니다.")
                dupCheckValue = false
            }
        }


        //다음 페이지 넘어가는 이벤트
        val startBtn = findViewById<Button>(R.id.startBtn)
        cuf.changeBtnColor(view,startBtn,R.drawable.rounded_gray,R.drawable.rounded_silver)

        startBtn.setOnClickListener{
            if(nickNameUseAble == true) {
                val intent = Intent(this, MainActivity::class.java)
                //바뀌면 바뀐 위치 기반으로
                intent.putExtra("Lat",latTemp)
                intent.putExtra("Lng",lngTemp)
                startActivity(intent)//현재 맵 수정에 따른 좌표
                finish()
            }
            else{
                alertDialog("닉네임 중복체크를 \n먼저 해주세요.")
            }
        }

        // 시작할 때 자기 위치 기반으로 기본 주소 설정
        val addressText = findViewById<TextView>(R.id.addressText)
        val latlngTemp = LatLng(latTemp!!, lngTemp!!)
        addressText.text = getAddressFromCode(latlngTemp)

        // 주소지 변경
        val addressChangeBtn = findViewById<Button>(R.id.addressChangeBtn)
        addressChangeBtn.setOnClickListener {
            val intent = Intent(this, ProfileCreate2Activity::class.java)
            intent.putExtra("Lat",latTemp)
            intent.putExtra("Lng",lngTemp)
            getResultAddress.launch(intent)
        }
    }
}