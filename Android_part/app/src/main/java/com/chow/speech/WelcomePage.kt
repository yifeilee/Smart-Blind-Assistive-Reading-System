package com.chow.speech

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cameraalbumtest.R

class WelcomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)
//        var mStartVideoHandler: Handler = Handler()
//        var mStartVideoRunnable: Runnable =object: Runnable {
//            override fun run(){
//                mStartVideoHandler.postDelayed(this, 2000)
//            }
//        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                WaitingActivity.this.finish();
//                Toast.makeText(WaitingActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//            }
//        },2000);
    }
}