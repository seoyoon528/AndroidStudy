package com.example.basic.pomodoro_timer

import android.annotation.SuppressLint
import android.content.IntentSender
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val remainMinutesTextView: TextView by lazy {
        findViewById(R.id.remainMinutesTextView)
    }

    private val remainSecondsTextView: TextView by lazy {
        findViewById(R.id.remainSecondsTextView)
    }

    private val seekBar: SeekBar by lazy {
        findViewById(R.id.seekBar)
    }

    private val soundPool = SoundPool.Builder().build()

    private var currentCountDownTimer: CountDownTimer? = null
    private var tickingSoundId: Int? = null
    private var bellSoundId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        initSounds()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {     // 사용자가 직접 SeekBar를 조작하는 경우에만
                        updateRemainTime(progress * 60 * 1000L)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {      // SeekBar에 손을 다시 대면 실행 (timer 다시 셋업 시)
                    stopCountDown()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {       // SeekBar에서 손을 떼면 실행
                    seekBar ?: return       // SeekBar null 처리

                    if (seekBar.progress == 0 ) {       // SeekBar가 0일 때는 타이머를 멈춤
                        stopCountDown()
                    } else {
                        startCountDown()
                    }
                }
            }
        )
    }

    private fun createCountDownTimer(initialMillis: Long) =
        object : CountDownTimer(initialMillis, 1000L) {     // CountDownTimer :: 추상 클래스
            override fun onTick(millisUntilFinished: Long) {    // onTick :: 1초에 한 번씩 불림
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }

    private fun updateRemainTime(remainMillis: Long) {      // 분, 초 TextView 갱신
        val remainSeconds = remainMillis / 1000

        remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMillis: Long) {
        seekBar.progress = (remainMillis / 1000 / 60).toInt()
    }

    private fun initSounds() {
        tickingSoundId = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSoundId = soundPool.load(this, R.raw.timer_bell, 1)
    }

    private fun startCountDown() {
        currentCountDownTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)
        currentCountDownTimer?.start()

        tickingSoundId?.let { soundId ->
        soundPool.play(soundId, 1F, 1F, 0, -1, 1F)
        }
    }

    private fun stopCountDown() {
        currentCountDownTimer?.cancel()     // 현재 실행하고 있던 CountDownTimer 멈춤
        currentCountDownTimer = null
        soundPool.autoPause()
    }

    private fun completeCountDown() {
        updateRemainTime(0)     // time과 seekBar를 0으로 셋팅
        updateSeekBar(0)

        soundPool.autoPause()
        bellSoundId?.let { soundId ->       // 종료음 울리기
            soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
        }
    }
}