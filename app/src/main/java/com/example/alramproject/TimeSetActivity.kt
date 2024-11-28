package com.example.alramproject

import Model.AlarmItem
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alramproject.databinding.ActivityTimepickerBinding

class TimeSetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimepickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimepickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 버튼과 TimePicker를 binding으로 참조
        val timePicker = binding.TimePicker
        val confirmBtn = binding.CheckBtn
        val cancellationBtn = binding.CancellationBtn

        confirmBtn.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            val amPm = if (hour < 12) "AM" else "PM"
            val monthText = binding.monthEditText.text.toString()
            val dayText = binding.dayEditText.text.toString()

            val month = monthText.toIntOrNull()
            val day = dayText.toIntOrNull()
            val month31 = arrayOf(1, 3, 5, 7, 8, 10, 12)

            // 월과 일이 비어있거나 잘못된 값일 경우
            if (month == null || day == null) {
                Toast.makeText(this, "월과 일을 올바르게 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 월과 일의 유효성 체크
            if (month in 1..12) {
                if ((month in month31 && day in 1..31) || (month !in month31 && day in 1..30)) {
                    // 유효한 날짜일 경우 AlarmItem 생성 및 반환
                    val newAlarm = AlarmItem(
                        id = 0, // ID는 기본값으로 설정
                        amPm = amPm,
                        hour = hour,
                        minute = minute,
                        month = month.toString(),
                        day = day.toString(),
                        on_off = false // 기본값으로 알람 비활성화
                    )

                    val resultIntent = Intent()
                    resultIntent.putExtra("newAlarm", newAlarm)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                } else {
                    val maxDay = if (month in month31) 31 else 30
                    Toast.makeText(
                        this,
                        "잘못된 날짜입니다: ${month}월은 1~${maxDay}일까지만 가능합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "잘못된 월입니다: 월은 1~12 사이의 값이어야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        cancellationBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
