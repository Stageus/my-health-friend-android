package com.example.myhealthpartner


import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import java.io.File
import java.lang.Exception

class BoardPostPageActivity : AppCompatActivity() {
    private lateinit var progressDialog : AppCompatDialog
    lateinit var imageResult : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board_write_post_page)

        var imageFile : File
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
                    val imageView = ImageView(this)
                    Glide.with(this) //이미지 적용
                        .load(imageUri)
                        .into(imageView)
                    val layoutTemp = findViewById<LinearLayout>(R.id.postEditText)
                    layoutTemp.addView(imageView)
                }
            }else { //갤러리창에서 뒤로가기할 시
                progressOff()
            }
        }

        initEvent()
    }

    fun initEvent(){
        findViewById<Button>(R.id.uploadPostBtn).setOnClickListener {
            finish()
        }
        findViewById<ImageView>(R.id.addImageBtn).setOnClickListener {
            try {
                selectGallery()
            }
            catch (e : Exception){
                alertDialog("사진 불러오기 실패")
            }
        }
    }

    //사진을 불러오기 위한 subcode

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

    private fun selectGallery(){
        progressOn()
        val readPermission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if(readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                ProfileCreate1Activity.REQ_GALLERY
            )
        }else{
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*")
            imageResult.launch(intent)
        }
    }

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

}