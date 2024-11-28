package com.example.alramproject

import ViewModel.AlarmDatabaseHelper
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alramproject.databinding.ActivityDeleteScreenBinding

class DelTimeBarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteScreenBinding
    private lateinit var dbHelper: AlarmDatabaseHelper
    private lateinit var arrayAdapter: AdapterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = AlarmDatabaseHelper(this)

        // DB에서 알람 목록을 불러와서 어댑터에 연결
        val alarms = dbHelper.getAllAlarms().toMutableList()
        arrayAdapter = AdapterActivity(this, alarms)
        binding.delList.adapter = arrayAdapter

        binding.delOk.setOnClickListener {
            val selectedAlarms = arrayAdapter.getSelectedAlarms()

            if (selectedAlarms.isNotEmpty()) {
                // 선택된 알람 삭제
                selectedAlarms.forEach { alarm ->
                    dbHelper.deleteAlarm(alarm.id)
                    // 삭제 후 알람목록 갱신
                    val updatedAlarms = dbHelper.getAllAlarms().toMutableList()

                    // alarmMain에 반영
                    val resultIntent = Intent()
                    resultIntent.putExtra("deletedAlarmId", alarm.id)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }
}
