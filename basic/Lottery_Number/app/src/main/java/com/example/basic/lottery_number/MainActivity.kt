package com.example.basic.lottery_number

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val clearButton: Button by lazy {
        findViewById<Button>(R.id.clearButton)
    }
    private val addButton: Button by lazy {
        findViewById<Button>(R.id.addButton)
    }
    private val runButton: Button by lazy {
        findViewById<Button>(R.id.runButton)
    }
    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.NumberPicker)
    }
    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(   // TextView가 리스트에 인자로 들어가 초기화
            findViewById<TextView>(R.id.textView1),
            findViewById<TextView>(R.id.textView2),
            findViewById<TextView>(R.id.textView3),
            findViewById<TextView>(R.id.textView4),
            findViewById<TextView>(R.id.textView5),
            findViewById<TextView>(R.id.textView6)
        )
    }

    private var didRun = false  // 숫자 생성 가능 여부 판단
    private val pickNumberSet = hashSetOf<Int>()    // 중복 방지를 위해 Set 사용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1   // NumberPicker 숫자 범위 설정
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }

    private fun initRunButton() {
        runButton.setOnClickListener{
            val list = getRandomNumber()

            didRun = true

            list.forEachIndexed { index, number ->      // 리스트의 index와 number값 모두 리턴되는 함수
                val textView = numberTextViewList[index]

                textView.text = number.toString()
                textView.visibility = View.VISIBLE

                setNumberBackground(number, textView)
            }
        }
    }

    private fun initAddButton() {
        addButton.setOnClickListener{

            if(didRun) {    // 이미 숫자를 자동생성한 경우
                Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener   // 에러 메시지 출력 후에 이후 내용이 실행되지 않도록
            }
            if(pickNumberSet.size >= 6) {   // Set이 다 찬 경우
                Toast.makeText(this, "번호는 6개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(pickNumberSet.contains(numberPicker.value)) {    // 이미 뽑힌 번호를 선택한 경우
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.visibility = View.VISIBLE
            textView.text = numberPicker.value.toString()

            setNumberBackground(numberPicker.value, textView)

            pickNumberSet.add(numberPicker.value)
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach {  // forEach :: 리스트의 TextView를 하나하나 꺼내어 줌
                it.visibility = View.GONE
            }
            didRun = false
        }
    }

    private fun setNumberBackground(number:Int, textView: TextView) {      // drawable 값 가져와 숫자 범위에 맞는 background 설정
        when (number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>()   // 1~45까지의 숫자를 리스트에 담아줌
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)) {    // 이미 선택된 번호는 제외 후 랜덤 generate
                        continue
                    }
                    this.add(i)
                }
            }
        numberList.shuffle()    // 리스트 shuffle
        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)  // numberPicker로 이미 선택한 수만큼 제외 후, 랜덤 발생시켜 새로운 리스트 생성

        return newList.sorted()     // 선택된 번호를 오름차순 정렬 후 반환
    }
}