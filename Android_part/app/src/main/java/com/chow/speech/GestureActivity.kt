package com.chow.speech

import android.content.Intent
import android.gesture.Gesture
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.gesture.GestureOverlayView
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.cameraalbumtest.R
import java.util.*

class GestureActivity : AppCompatActivity() {
    private val welcomeCode = 1
    private lateinit var editText: EditText
    private lateinit var gestureView: GestureOverlayView
    private lateinit var gesture: Gesture
    private var tts: TextToSpeech? = null
    private var times = 1
    private lateinit var gestureLib:GestureLibrary

//    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture)
        val welcomeIntent = Intent(this, WelcomePage::class.java)
        startActivityForResult(welcomeIntent,welcomeCode)

//        gestureLib = GestureLibraries.fromFile(
//            Environment.getExternalStorageDirectory().absolutePath + "/mygestures"
//        )
//        if (Build.VERSION.SDK_INT >= 30) {
//            if (!Environment.isExternalStorageManager()) {
//                val getpermission = Intent()
//                getpermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
//                startActivity(getpermission)
//            }
//        }
//        tts = TextToSpeech(this, TextToSpeech.OnInitListener { status: Int ->
//            if (status == TextToSpeech.SUCCESS) {
//                val result = tts?.setLanguage(Locale.CHINESE)
//                if (result == TextToSpeech.LANG_AVAILABLE) {
//                    tts?.speak(
//                        "请绘制第"+times+"个手势",
//                        TextToSpeech.QUEUE_ADD,
//                        null
//                    )
//                }
//            }
//        })
//        //获取手势编辑视图
//        gestureView = findViewById(R.id.gesture)
//        //设置手势绘制颜色
//        gestureView.gestureColor = Color.RED
//        //设置手势的绘制宽度
//        gestureView.gestureStrokeWidth = 4f
//        //绑定监听器
//        gestureView.addOnGesturePerformedListener { _, gesture ->
//            this.gesture = gesture
//            if(times<=3){
//                requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0x123)
//            }
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0x123 && grantResults != null && grantResults[0] == 0) {
            //加载dialog_save.xml界面布局代表的视图
            val saveDialog = layoutInflater.inflate(R.layout.dialog_save, null)
            val gestureId:TextView = saveDialog.findViewById(R.id.gestureId)
            gestureId.text = when(times){
                1 -> "手势1--拍照"
                2 -> "手势2--播放/暂停"
                3 -> "手势3--朗读电子书"
                else -> {"手势"}
            }

            val imageView: ImageView = saveDialog.findViewById(R.id.show)
            val bitmap = gesture.toBitmap(128, 128, 10, -0x10000)
            imageView.setImageBitmap(bitmap)
            AlertDialog.Builder(this@GestureActivity).setView(saveDialog)
                .setPositiveButton(R.string.bn_save) { _, _ ->
                    gestureLib.addGesture(times++.toString(), gesture)
                    gestureLib.save()
                    if(times<=3){
                        tts = TextToSpeech(this, TextToSpeech.OnInitListener { status: Int ->
                            if (status == TextToSpeech.SUCCESS) {
                                val result = tts?.setLanguage(Locale.CHINESE)
                                if (result == TextToSpeech.LANG_AVAILABLE) {
                                    tts?.speak(
                                        "请绘制第"+times+"个手势",
                                        TextToSpeech.QUEUE_ADD,
                                        null
                                    )
                                }
                            }
                        })
                    }else{
                        finish()
                    }
                }.setNegativeButton(R.string.bn_cancel, null).show()

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == welcomeCode){
            gestureLib = GestureLibraries.fromFile(
                Environment.getExternalStorageDirectory().absolutePath + "/mygestures"
            )
            if (Build.VERSION.SDK_INT >= 30) {
                if (!Environment.isExternalStorageManager()) {
                    val getpermission = Intent()
                    getpermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    startActivity(getpermission)
                }
            }
            tts = TextToSpeech(this, TextToSpeech.OnInitListener { status: Int ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(Locale.CHINESE)
                    if (result == TextToSpeech.LANG_AVAILABLE) {
                        tts?.speak(
                            "请绘制第"+times+"个手势",
                            TextToSpeech.QUEUE_ADD,
                            null
                        )
                    }
                }
            })
            //获取手势编辑视图
            gestureView = findViewById(R.id.gesture)
            //设置手势绘制颜色
            gestureView.gestureColor = Color.RED
            //设置手势的绘制宽度
            gestureView.gestureStrokeWidth = 4f
            //绑定监听器
            gestureView.addOnGesturePerformedListener { _, gesture ->
                this.gesture = gesture
                if(times<=3){
                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0x123)
                }
            }
        }
    }
}