package com.example.basic.secret_diary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    private val numberPicker1: NumberPicker by lazy {   // lazy하게 초기화 :: MainActivity가 생성될 시점에는 아직 view가 그려지지 않음. OnCreate시 그려짐
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }

    private var changePasswordMode = false      // 비밀번호를 바꾸는 동안에는 다른 동작을 제한하기 위함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener {

            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호를 변경 중입니다.", Toast.LENGTH_SHORT).show()
            }

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)  //  sharedPreferences :: preferences 파일을 다른 앱과 같이 사용 할 수 있도록 share해줌.
                                                                                                                                                                                        // 다른 앱들과 공유하지 않기위해 MODE_PRIVATE으로 설정
            val  passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"    // 비밀번호 3자리 설정

            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                // 패스워드 성공
                startActivity(Intent(this, DiaryActivity::class.java))  // 패스워드가 맞을 때 DiaryActivity Start
            }  else  {
                // 패스워드 실패
                showErrorAlertDialog()
            }
        }

        changePasswordButton.setOnClickListener {
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val  passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (changePasswordMode) {
                    // 번호를 저장하는 기능
                    passwordPreferences.edit(true) {    // edit :: sharedPreferences에 값을 저장 할 때 사용
                        putString("password", passwordFromUser)     // commit :: UI thread를 잠깐 block하고 데이터 저장될 때까지 기다림,  apply :: 기다리지 않고 비동기적 저장
                    }
                    changePasswordMode = false
                    changePasswordButton.setBackgroundColor(Color.BLACK)

            } else {
                    // changePasswordMode가 활성화 :: 비밀번호가 맞는지를 체크
                    if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                        // 패스워드 성공
                        changePasswordMode = true
                        Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
                        changePasswordButton.setBackgroundColor(Color.RED)
                    }  else  {
                            // 패스워드 실패
                            showErrorAlertDialog()
                    }
                }
        }
    }
    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패~")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}