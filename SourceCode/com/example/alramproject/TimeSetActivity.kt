package com.example.alramproject

import ViewModel.AlarmDatabaseHelper
import Model.AlarmItem
import android.content.Intent
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.alramproject.databinding.ActivityTimepickerBinding

class TimeSetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimepickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimepickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TimePicker와 버튼을 정확히 참조
        val timePicker = findViewById<TimePicker>(R.id.Time_Picker)
        val confirmBtn = findViewById<Button>(R.id.CheckBtn)
        val cancellationBtn = findViewById<Button>(R.id.CancellationBtn)

        confirmBtn.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            val amPm = if (hour < 12) "AM" else "PM"
            val month = binding.monthEditText.text.toString()
            val day = binding.dayEditText.text.toString()

            if (month.isEmpty() || day.isEmpty()) {
                //Toast 메시지로 경고표시(?아니면 if문으로 막기?)
                return@setOnClickListener
            }

            val newAlarm = AlarmItem(
                amPm = amPm,
                hour = hour,
                minute = minute,
                month = month,
                day = day
            )

            val resultIntent = Intent()
            resultIntent.putExtra("newAlarm", newAlarm)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        cancellationBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
