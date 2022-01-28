package com.example.intermediate.push_notification_receiver_firebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private val resultTextView: TextView by lazy {
        findViewById(R.id.resultTextview)
    }

    private val firebaseToken: TextView by lazy {
        findViewById(R.id.firebaseTokenTextView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        updateResult()
    }

    override fun onNewIntent(intent: Intent?) {     // ACTIVITY_SINGLE_TOP일 경우 onNewIntent 호출
        super.onNewIntent(intent)

        setIntent(intent)       // 새로 들어온 intent로 교체
        updateResult(true)
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token       // Firebase Messaging token 가져오기
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    firebaseToken.text = token
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent: Boolean = false) {        // 갱신 여부 확인
        // intent를 받아왔을 때 notificationType이 있으면 notification을 눌러 들어왔다고 판단, null일 경우는 icon(런처)을 눌러 실행했다고 판단
        resultTextView.text = (intent.getStringExtra("notificationType") ?: "앱 런처") +
            if (isNewIntent) {
                "(으)로 갱신했습니다."
            } else {
                "(으)로 실행했습니다."
            }
    }
}