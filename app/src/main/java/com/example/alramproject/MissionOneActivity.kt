package com.example.alramproject

import ViewModel.AlarmDatabaseHelper
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alramproject.databinding.ActivityMisson1Binding
import java.text.SimpleDateFormat
import java.util.*

class MissionOneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMisson1Binding
    private var countDown: Int = 0
    private lateinit var dbHelper: AlarmDatabaseHelper
    private lateinit var arrayAdapter: AdapterActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisson1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = AlarmDatabaseHelper(this)
        val alarms = dbHelper.getAllAlarms().toMutableList()
        arrayAdapter = AdapterActivity(this, alarms)

        val currentTime = getCurrentTime()
        binding.alramMissionOneTime.text = currentTime

        countDown = (30..50).random()
        binding.touchCount.text = countDown.toString()


        binding.imageButton.setOnClickListener {
            countDown -= 1
            binding.touchCount.text = countDown.toString()

            if (countDown <= 0) {
                val activeAlarms = alarms.filter { it.on_off }

                activeAlarms.forEach { alarm ->
                    alarm.on_off = false
                    dbHelper.updateAlarm(alarm)
                    arrayAdapter.updateSwitches()
                }

                arrayAdapter.notifyDataSetChanged()

                Toast.makeText(this, "미션 성공, 알람 종료", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
        val currentTime = dateFormat.format(Date())

        return currentTime
    }
}
