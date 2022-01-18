package com.example.basic.secret_diary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity: AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())  //  Handler :: Thread와 Thread간의 통신 방법
                                                                                                        // getMainLooper :: Main Thread의 Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)

        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("detail", ""))

        val runnable = Runnable {   // Thread에서 일어나는 기능
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("detail", diaryEditText.text.toString())
            }
        }

        diaryEditText.addTextChangedListener {      // 내용이 수정 될 때마다 리스너가 호출되어 새로 저장
            handler.removeCallbacks(runnable)   // 이전의 Pending 되어있는 Runnable이 있다면 지워줌
            handler.postDelayed(runnable, 500)   // postDelayed :: 몇 초 이후에 Runnable 실행 -> 작성 후 500ms 이후에 수정된 내용 저장됨
        }
    }
}