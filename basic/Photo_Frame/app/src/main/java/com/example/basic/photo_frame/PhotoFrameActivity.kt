package com.example.basic.photo_frame

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity: AppCompatActivity() {

    private var currentPosition = 0     // 몇 번째 인덱스까지 돌았는지를 저장하는 변수

    private val photoList = mutableListOf<Uri>()

    private var timer: Timer? = null    // nullable로 timer 변수 초기화

    private val photoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.photoImageView)
    }

    private val backgroundPhotoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.backgroundPhotoImageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoframe)
        Log.d("PhotoFrame", "onCreate!")

        getPhotoUriFromIntent()
    }

    private fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize", 0)       // 로드 할 사진의 개수를 받아옴
        for (i in 0..size)  {
            intent.getStringExtra("photo$i")?.let {     // let :: null이 아닐때만 실행하게 함
                photoList.add(Uri.parse(it))        // Uri로 변환해서 photoList에 추가시켜줌
            }
        }
    }

    private fun startTimer() {      // 5초에 한 번씩 사진 전환
        timer = timer(period = 5 * 1000)  {
            runOnUiThread {         // 타이머는 main Thread X, So runOnUiThread를 열어야함
                Log.d("PhotoF", "5초가 지나감!")

                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0     // 마지막 인덱스의 사진일 경우 다시 첫번째 인덱스로 바꿔줌
                                    else currentPosition + 1

                backgroundPhotoImageView.setImageURI(photoList[current])

                photoImageView.alpha = 0f       // photoImageView를 완전히 투명하게 함
                photoImageView.setImageURI(photoList[next])
                photoImageView.animate()        // fade in 애니메이션 효과
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("PhotoFrame", "onStop!  timer cancle")

        timer?.cancel()     // 앱 중지 시 타이머가 계속 background에서 돌고 있는 것을 방지하기 위해
    }

    override fun onStart() {
        super.onStart()
        Log.d("PhotoFrame", "onStart!  timer start")

        startTimer()    // 앱 실행 시 timer 시작
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PhotoFrame", "onDestroy!  timer cancle")

        timer?.cancel()
    }
}