package com.example.myhealthpartner

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
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
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*


class MyProfileEditPage : AppCompatActivity() {
    private lateinit var progressDialog : AppCompatDialog
    private lateinit var getResultAddress : ActivityResultLauncher<Intent>

    var latTemp : Double? = null
    var lngTemp : Double? = null

    //이미지를 결과값을 받는 변수
    lateinit var imageResult : ActivityResultLauncher<Intent>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //실행할 때 현재 위치 전페이지서 전달받아오기
        setContentView(R.layout.myprofile_edit_page)
        getResultAddress = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            if(result.resultCode == RESULT_OK){
                val addressText = findViewById<TextView>(R.id.addressText)
                val addressString = result.data?.getStringExtra("address")
                addressText.text = addressString
                val loginData = this.getSharedPreferences("loginData", 0)
                latTemp = loginData!!.getString("Lat","")!!.toDouble()
                lngTemp = loginData!!.getString("Lng","")!!.toDouble()
                Log.d("ttt1", "  ${latTemp}")
                Log.d("ttt2", "  ${lngTemp}")
            }
        }

        val checkboxLinear1 = findViewById<LinearLayout>(R.id.checkboxLinear1)
        checkboxLinear1.visibility = View.GONE
        val checkboxLinear2 = findViewById<LinearLayout>(R.id.checkboxLinear2)
        checkboxLinear2.visibility = View.GONE

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

    fun initData(): UserData? { //json파일 읽어오기 작업
        val jsonObject : String
        jsonObject = assets.open("userData.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val userData = gson.fromJson(jsonObject, UserData::class.java)
        return userData
    }



    fun initEvent(){
        val userDataFromJson = initData()
        val userPrefrence = this.getSharedPreferences("loginData", 0)
        val userIndex = userPrefrence.getInt("userIndex",0)
        val userData_ = userDataFromJson!!.user[userIndex]


        val checkboxLinear1 = findViewById<LinearLayout>(R.id.checkboxLinear1)
        val checkboxLinear2 = findViewById<LinearLayout>(R.id.checkboxLinear2)
        val fitnessListBtn = findViewById<ImageButton>(R.id.fitnessListBtn)
        val timeListBtn = findViewById<ImageButton>(R.id.timeListBtn)
        val setprofileBtn = findViewById<Button>(R.id.setProfileBtn)
        val profileImage = findViewById<ImageView>(R.id.profileImage)


        //초기값 세팅

        val exerciseType = userData_.findUserDataList[0].exerciseType
        val exerciseTime = userData_.findUserDataList[0].exerciseTime
        if(exerciseType[0] == 'T'){
            findViewById<CheckBox>(R.id.exerCheck1).isChecked = true
        }
        if(exerciseType[1] == 'T'){
            findViewById<CheckBox>(R.id.exerCheck2).isChecked = true
        }
        if(exerciseType[2] == 'T'){
            findViewById<CheckBox>(R.id.exerCheck3).isChecked = true
        }
        if(exerciseType[3] == 'T'){
            findViewById<CheckBox>(R.id.exerCheck4).isChecked = true
        }

        if(exerciseTime[0] == 'T'){
            findViewById<CheckBox>(R.id.timeCheck1).isChecked = true
        }
        if(exerciseTime[1] == 'T'){
            findViewById<CheckBox>(R.id.timeCheck2).isChecked = true
        }
        if(exerciseTime[2] == 'T'){
            findViewById<CheckBox>(R.id.timeCheck3).isChecked = true
        }
        if(exerciseTime[3] == 'T'){
            findViewById<CheckBox>(R.id.timeCheck4).isChecked = true
        }

        //userData_.findUserDataList[0].badgedatalist[]

        // 시작할 때 자기 주소 기반으로 기본 주소 설정
        val addressText = findViewById<TextView>(R.id.addressText)
        val latlngTemp = LatLng(userData_.findUserDataList[0].lat,userData_.findUserDataList[0].lng)
        addressText.text = getAddressFromCode(latlngTemp)

        val introduceEditText = findViewById<TextView>(R.id.introduceEditText)
        introduceEditText.text = userData_.findUserDataList[0].introduce

        val careerEditText = findViewById<TextView>(R.id.careerEditText)
        careerEditText.text = userData_.findUserDataList[0].career

        val abilityEditText = findViewById<TextView>(R.id.abilityEditText)
        abilityEditText.text = userData_.findUserDataList[0].ability

        val badgeArrayList = arrayListOf<ImageView>()

        badgeArrayList.add(findViewById<ImageView>(R.id.badge1))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge2))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge3))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge4))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge5))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge6))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge7))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge8))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge9))
        badgeArrayList.add(findViewById<ImageView>(R.id.badge10))

        for (index in 0 until badgeArrayList.size) {
            val badgeData = userData_.findUserDataList[0].badgedatalist[index].badge
            val scale = resources.displayMetrics.density
            val padding_5p = (5 * scale + 0.5f).toInt()
            badgeArrayList[index].setPadding(padding_5p,padding_5p,padding_5p,padding_5p)
            if(badgeData == "koala"){
                badgeArrayList[index].setImageResource(R.mipmap.koalabadge)
            }
        }

        //ui 기능 세팅

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
            if(checkboxLinear1.visibility == View.GONE){
                checkboxLinear1.visibility = View.VISIBLE
                fitnessListBtn.setImageResource(R.drawable.triangle)
            }
            else{
                checkboxLinear1.visibility = View.GONE
                fitnessListBtn.setImageResource(R.drawable.reverse_triangle)

            }
        }

        timeListBtn.setOnClickListener {
            if(checkboxLinear2.visibility == View.GONE){
                checkboxLinear2.visibility = View.VISIBLE
                fitnessListBtn.setImageResource(R.drawable.triangle)
            }
            else{
                checkboxLinear2.visibility = View.GONE
                fitnessListBtn.setImageResource(R.drawable.reverse_triangle)
            }
        }

        // 뱃지 수정하는 이벤트 (나중에 db 수정 필요)
        for (index in 0 until badgeArrayList.size) {
            badgeArrayList[index].setOnClickListener{
                val dialogTemp = AlertDialog.Builder(this)
                val dialog = dialogTemp.create()
                val dialogViewTemp = layoutInflater.inflate(R.layout.choice_badge_dialog,null)
                dialog.setView(dialogViewTemp)
                dialog.show()
                dialogViewTemp.findViewById<ImageButton>(R.id.badgeOption1).setOnClickListener{
                    badgeArrayList[index].setImageResource(R.mipmap.liftbreak)
                    dialog.dismiss()
                }
                dialogViewTemp.findViewById<ImageButton>(R.id.badgeOption2).setOnClickListener {
                    badgeArrayList[index].setImageResource(R.mipmap.redheart)
                    dialog.dismiss()
                }
                dialogViewTemp.findViewById<ImageButton>(R.id.badgeOption3).setOnClickListener{
                    badgeArrayList[index].setImageResource(R.mipmap.koalabadge)
                    dialog.dismiss()
                }
            }
        }

        //다음 페이지 넘어가는 이벤트
        val view = findViewById<View>(R.id.layout)
        val cuf = CommonUsedFunctionClass()
        val saveBtn = findViewById<Button>(R.id.saveBtn)
        cuf.changeBtnColor(view,saveBtn,R.drawable.rounded_gray,R.drawable.rounded_silver)

        saveBtn.setOnClickListener{
            val dialogTemp2 = AlertDialog.Builder(this)
            val dialog2 = dialogTemp2.create()
            val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog_yes_no,null)
            val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
            alertMessage.text = "저장하시겠습니까?"
            dialog2.setView(dialogViewTemp)
            dialog2.show()
            dialogViewTemp.findViewById<Button>(R.id.yesBtn).setOnClickListener{
                finish()
                val intent = Intent(this, MyProfilePageActivity::class.java)
                startActivity(intent)
            }
            dialogViewTemp.findViewById<Button>(R.id.noBtn).setOnClickListener {
                dialog2.dismiss()
            }

        }

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