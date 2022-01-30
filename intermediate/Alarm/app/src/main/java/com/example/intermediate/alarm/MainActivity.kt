package com.example.intermediate.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initOnOffButton()        // 뷰 초기화해주기
        initChangeAlarmTimeButton()

        val model = fetchDataFromSharedPreferences()       // 데이터 가져오기
        renderView(model)        // 뷰에 데이터를 그려주기
    }

    private fun initOnOffButton() {
        val onOffButton = findViewById<Button>(R.id.onOffButton)
        onOffButton.setOnClickListener {

            val model = it.tag  as? AlarmDisplayModel  ?:  return@setOnClickListener     // as? :: 형 변환 실패 시 null
            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not())       // 데이터 확인
            renderView(newModel)

            if (newModel.onOff) {            // on -> 알람 등록
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)

                    if (before(Calendar.getInstance())) {        // 지나간 시간에 대한 알람을 설정하는 경우
                        add(Calendar.DATE, 1)       //  다음날의 알람으로 미뤄줌
                    }
                }

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,        // intent를  PendingIntent로 감싸 전달
                    intent, PendingIntent.FLAG_UPDATE_CURRENT)        // FLAG_UPDATE_CURRENT :: 기존 것이 있다면 새로운 것으로 업데이트 함

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP ,        // 절대 시간 기준으로 알람 설정
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,           // 하루 뒤에 실행
                    pendingIntent
                )
            } else {            // off -> 알람 제거
                cancelAlarm()
            }
        }
    }

    private fun initChangeAlarmTimeButton() {
        val changeAlarmButton = findViewById<Button>(R.id.changeAlarmTimeButton)
        changeAlarmButton.setOnClickListener {

            val calendar = Calendar.getInstance()       // 현재 시간을 가져옴

            TimePickerDialog( this, { picker, hour, minute ->      // TimePickDialog 띄워 시간 설정

                 val model = saveAlarmModel(hour, minute, false)        // 그 시간을 가져와 데이터 저장
                 renderView(model)          // 뷰 업데이트
                 cancelAlarm()        // 기존 알람 삭제

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()

        }
    }

    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean ): AlarmDisplayModel {         // 데이터 저장
        val model = AlarmDisplayModel (
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        val sharedPreferences = getSharedPreferences("time", Context.MODE_PRIVATE)

        // with :: sharedPreferences.edit() 에서 할 수 있는 작업들을 scope에 저장
       with(sharedPreferences.edit()) {
           putString(ALARM_KEY, model.makeDataForDB())
           putBoolean(ONOFF_KEY, model.onOff)
           commit()
        }

        return model
    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)      //  Shared Preferences를 가져와서

        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "9:30") ?: "9:30"       //  DB 값을 꺼냄       // getString은 Nullable
        val onOffDBValue = sharedPreferences.getBoolean(ONOFF_KEY, false)
        val alarmData = timeDBValue.split(":")

        val alarmModel = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[1].toInt(),
            onOff = onOffDBValue
        )

        //  알람이 등록 되어있는지 확인하기 위해 Broadcast를 가져와 PendingIntent 등록 여부 확인
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)         // FLAG_NO_CREATE :: 있으면 가져오고 없으면 만들지 않음

        if ((pendingIntent == null) and alarmModel.onOff) {     // pendingIntent = null :: 알람이 등록되어있지 않은데, alarmModel.onOff :: 데이터는 켜져있는 경우
            alarmModel.onOff = false        //  데이터를 꺼줌
        }
        else if ((pendingIntent != null) and alarmModel.onOff.not()) {      //  알람은 켜져있는데, 데이터가 꺼져있는 경우
            pendingIntent.cancel()      // 알람을 취소함
        }

        return alarmModel
    }

    private fun renderView(model: AlarmDisplayModel) {
        findViewById<TextView>(R.id.ampmTextView).apply {
            text = model.ampmText
        }

        findViewById<TextView>(R.id.timeTextView).apply {
            text = model.timeText
        }

        findViewById<Button>(R.id.onOffButton).apply {
            text = model.onOffText
            tag = model
        }
    }

    private fun cancelAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)         // 등록되어 있는 알람이 있다면
        pendingIntent?.cancel()
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val ALARM_REQUEST_CODE = 1000
    }

}