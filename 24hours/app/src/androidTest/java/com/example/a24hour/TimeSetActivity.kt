package com.example.a24hour;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a24hour.databinding.ActivityTimepickerBinding
import java.text.SimpleDateFormat;
import java.util.*;

class TimeSetActivity : AppCompatActivity() {

    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityTimepickerBinding? = null;
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    private lateinit var timePicker: TimePicker
    private lateinit var okBtn: Button
    private lateinit var cancelBtn: Button
    private var hour: Int = 0
    private var minute: Int = 0
    private lateinit var amPm: String
    private lateinit var currentTime: Date
    private lateinit var stMonth: String
    private lateinit var stDay: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTimepickerBinding.inflate(layoutInflater)
        setContentView(binding.root);

        currentTime = Calendar.getInstance().time
        val day = SimpleDateFormat("dd", Locale.getDefault());
        val month = SimpleDateFormat("MM", Locale.getDefault());

        stMonth = month.format(currentTime);
        stDay = day.format(currentTime);

        okBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = timePicker.hour
                minute = timePicker.minute
            } else {
                hour = timePicker.currentHour
                minute = timePicker.currentMinute
            }

            amPm = amAndpm(hour)
            hour = timeSet(hour)

            val sendIntent = Intent(this@TimeSetActivity, alarmMain::class.java)
            sendIntent.putExtra("hour", hour);
            sendIntent.putExtra("minute", minute);
            sendIntent.putExtra("am_pm", amPm);
            sendIntent.putExtra("month", stMonth);
            sendIntent.putExtra("day", stDay);
            setResult(RESULT_OK, sendIntent);
            finish();
        }

        // 취소버튼 누를 시 TimePickerActivity 종료
        cancelBtn.setOnClickListener {
            finish();
        }
    }

    private fun timeSet(hour: Int): Int {
        return if (hour > 12) {
            hour - 12
        } else {
            hour
        }
    }

    private fun amAndpm(hour: Int): String {
        return if (hour > 12) {
            "오후"
        } else {
            "오전"
        }
    }
}
