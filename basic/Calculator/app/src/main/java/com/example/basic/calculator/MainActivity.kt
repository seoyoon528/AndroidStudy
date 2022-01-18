package com.example.basic.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.basic.calculator.model.History
import java.lang.NumberFormatException
import kotlin.math.exp

class MainActivity : AppCompatActivity() {

    private val expressionTextView: TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView)
    }

    private val resultTextView: TextView by lazy {
        findViewById<TextView>(R.id.resultTextView)
    }

    private val historyLayout: View by lazy {
        findViewById<View>(R.id.historyLayout)
    }

    private val historyLinearLayout: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.historyLinearLayout)
    }

    lateinit var db: AppDataBase

    private var isOperator = false
    private var hasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(      // OnCreate 시 db에 값 할당
            applicationContext,
            AppDataBase::class.java,
            "historyDB"
        ).build()
    }

    fun buttonClicked(v: View) {
        when(v.id) {
            R.id.button0 -> numberButtonClicked("0")
            R.id.button1 -> numberButtonClicked("1")
            R.id.button2-> numberButtonClicked("2")
            R.id.button3 -> numberButtonClicked("3")
            R.id.button4 -> numberButtonClicked("4")
            R.id.button5 -> numberButtonClicked("5")
            R.id.button6 -> numberButtonClicked("6")
            R.id.button7 -> numberButtonClicked("7")
            R.id.button8 -> numberButtonClicked("8")
            R.id.button9 -> numberButtonClicked("9")

            R.id.buttonPlus -> operatorButtonClicked("+")
            R.id.buttonMinus -> operatorButtonClicked("-")
            R.id.buttonMulti -> operatorButtonClicked("*")
            R.id.buttonDivider -> operatorButtonClicked("/")
            R.id.buttonModulo -> operatorButtonClicked("%")

        }
    }

    private fun numberButtonClicked(number: String) {
        if (isOperator) {   // isOperator = true인 경우 operator가 사용되었으므로 다음은 숫자가 입력될 차례임을 나타냄
            expressionTextView.append(" ")
        }
        isOperator = false

        val expressionText = expressionTextView.text.split(" ")
        if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {    // expressionText가 비어있지 않으며 길이가 15자리가 넘어갈 경우 예외처리
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        else if (expressionText.last().isEmpty() && number == "0") {    // 0이 연산의 첫번째 자리에 올 경우 예외처리
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(number)
        resultTextView.text = calculateExpression()     // 실시간으로 게산 결과가 반영됨
    }

    private fun operatorButtonClicked(operator: String) {
        if (expressionTextView.text.isEmpty()) { // 숫자보다 연산자가 먼저 눌렸을 경우
            return
        }

        when {
            isOperator -> {     //  연산자를 수정하는 경우
                val text = expressionTextView.text.toString()
                expressionTextView.text = text.dropLast(1) + operator     // 마지막 부분을 하나 잘라서 없애고 operator를 다시 붙여줌
            }

            hasOperator -> {     // 연산자가 여러 번 쓰인 경우 예외 처리
                Toast.makeText(this, "연산자는 한 번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                expressionTextView.append(" $operator")     // 정상적으로 입력된 경우
            }
        }

        val ssb = SpannableStringBuilder(expressionTextView.text)   //  초록색으로 색칠
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            expressionTextView.text.length -1,
            expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE )

        expressionTextView.text = ssb
        isOperator = true
        hasOperator = true

    }


    private fun calculateExpression(): String {     // expressionTextView에서 숫자와 연산자를 받아와 결과 계산
         val expressionTexts = expressionTextView.text.split(" ")
        if (hasOperator.not() || expressionTexts.size != 3) {   // 제대로 된 연산이 아닐 경우 예외 처리
                return ""   // 초기화
        }
        else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {  // 연산자 양 옆에 숫자가 제대로 들어오지 않은 경우 예외 처리
            return ""  // 초기화
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when(op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""

        }
    }

    fun resultButtonClicked(v: View) {
        val expressionTexts = expressionTextView.text.split(" ")
        if (expressionTextView.text.isEmpty() || expressionTexts.size == 1) {
            return
        }

        if (expressionTexts.size != 3 && hasOperator) {
            Toast.makeText(this, "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val expressionText = expressionTextView.text.toString()
        val resultText = calculateExpression()

        Thread (Runnable {      // 디비 조작은 새로운 thread를 열어서 해야함
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }).start()


        resultTextView.text = ""
        expressionTextView.text = resultText

        isOperator = false
        hasOperator = false

    }

    fun clearButtonClicked(v: View) {
        expressionTextView.text = ""
        resultTextView.text = ""
        isOperator = false
        hasOperator = false
    }

    fun historyButtonClicked(v: View) {
        historyLayout.isVisible = true
        historyLinearLayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {        // 디비에서 모든 기록 최신 순으로 가져오기
                runOnUiThread{      // main Thread가 아닌 새로운 Thread를 열었기 때문에 다시 UI를 연결시켜주기 위해 UI Thread를 실행
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyView.findViewById<TextView>(R.id.expressionTextView).text = it.expression
                    historyView.findViewById<TextView>(R.id.resultTextView).text = "= ${it.result}"

                    historyLinearLayout.addView(historyView)
                }
            }
        }).start()
    }

    fun closeHistoryButtonClicked(v: View) {
        historyLayout.isVisible = false
    }

    fun historyClearButtonClicked(v: View) {
        historyLinearLayout.removeAllViews()        // 뷰에서 모든 기록 삭제

        Thread(Runnable {        // 디비에서 모든 기록 삭제
            db.historyDao().deleteAll()
        }).start()
    }

}

fun String.isNumber():  Boolean {    // String 확장 함수 직접 선언  :: 숫자값인지 판단하는 함수
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException) {
        false
    }
}