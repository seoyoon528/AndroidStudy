package com.example.basic.bmi_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val heightEditText: EditText = findViewById(R.id.heightEditText)
        val weightEditText: EditText = findViewById(R.id.weightEditText)

        val resultButton: Button = findViewById(R.id.resultButton)
        resultButton.setOnClickListener{
            Log.d("MainActiviy", "ResultButton이 클릭되었습니다.")

            if (heightEditText.text.isEmpty() || weightEditText.text.isEmpty()) {   // 빈 값인 경우 예외처리
                Toast.makeText(this, "값을 채워주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener  // setOnClickListener를 나감을 명시
            }

            val height: Int = heightEditText.text.toString().toInt()
            val weight: Int = weightEditText.text.toString().toInt()
            Log.d("MainActiviy", "height : $height  weight : $weight")

            val intent = Intent(this, ResultActivity::class.java)

            intent.putExtra("height", height)
            intent.putExtra("weight", weight)

            startActivity(intent) // intent를 전달하며 ResultActivity 호출
        }
    }
}