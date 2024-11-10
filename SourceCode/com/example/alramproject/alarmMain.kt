package com.example.alramproject

import Model.AlarmItem
import ViewModel.AlarmDatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.example.alramproject.databinding.ActivityMainscreenBinding
import java.text.SimpleDateFormat
import java.util.*

class alarmMain : AppCompatActivity() {

    private lateinit var binding: ActivityMainscreenBinding
    private lateinit var arrayAdapter: AdapterActivity
    private lateinit var handler: Handler
    private lateinit var mFormat: SimpleDateFormat
    private lateinit var dbHelper: AlarmDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = AlarmDatabaseHelper(this)

        // DB에서 알람 불러오기
        val alarms = dbHelper.getAllAlarms().toMutableList() // DB에서 알람을 불러와서 MutableList로 변환
        arrayAdapter = AdapterActivity(this, alarms)
        binding.listView.adapter = arrayAdapter

        binding.plusBtn.setOnClickListener {
            val intent = Intent(this, TimeSetActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.delBtn.setOnClickListener {
            val intent = Intent(this, DelTimeBarActivity::class.java)
            startActivityForResult(intent, DELETE_REQUEST_CODE)
        }

        // 현재 시간 표시
        handler = Handler(Looper.getMainLooper())
        mFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        binding.current.textSize = 42f
        binding.current.gravity = Gravity.CENTER

        handler.post(object : Runnable {
            override fun run() {
                val cal = Calendar.getInstance()
                val strTime = mFormat.format(cal.time)
                binding.current.text = strTime
                handler.postDelayed(this, 1000)
            }
        })
    }

    // 알람 추가 후 반환된 결과를 처리하는 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // 추가된 알람 정보 받기
            val newAlarm = data.getParcelableExtra<AlarmItem>("newAlarm")

            if (newAlarm != null) {
                // DB에 알람 추가
                dbHelper.insertAlarm(newAlarm)

                // DB에서 알람 리스트 불러오기
                val updatedAlarms = dbHelper.getAllAlarms().toMutableList()

                // 어댑터 업데이트
                arrayAdapter.updateAlarms(updatedAlarms)
                binding.listView.adapter = arrayAdapter
            }
        }

        if (requestCode == DELETE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // 삭제된 알람 정보를 받기
            val deletedAlarmId = data.getIntExtra("deletedAlarmId", -1)

            if (deletedAlarmId != -1) {
                // DB에서 알람 삭제
                dbHelper.deleteAlarm(deletedAlarmId)

                // DB에서 알람 리스트 불러오기
                val updatedAlarms = dbHelper.getAllAlarms().toMutableList()

                arrayAdapter.updateAlarms(updatedAlarms)
                binding.listView.adapter = arrayAdapter
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 2
        private const val DELETE_REQUEST_CODE = 3
    }
}
