package com.example.alramproject

import Model.AlarmItem
import ViewModel.AlarmDatabaseHelper
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
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
    private lateinit var alarmManager: AlarmManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = AlarmDatabaseHelper(this)
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        // 정확한 알람 권한 요청
        requestExactAlarmPermission()


        // DB에서 알람 불러오기
        val alarms = dbHelper.getAllAlarms().toMutableList()
        arrayAdapter = AdapterActivity(this, alarms)
        binding.listView.adapter = arrayAdapter

        // 스위치 활성화 이벤트 처리
        arrayAdapter.onSwitchChangeListener = { alarm, isChecked ->
            alarm.on_off = isChecked
            dbHelper.updateAlarmOnOff(alarm.id, isChecked)
            if (isChecked) {
                setAlarm(alarm)
            } else {
                cancelAlarm(alarm.id)
            }
        }

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

    private fun setAlarm(alarm: AlarmItem) {
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("alarmId", alarm.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, alarm.month.toInt() - 1)
            set(Calendar.DAY_OF_MONTH, alarm.day.toInt())
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }



    private fun cancelAlarm(alarmId: Int) {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val newAlarm = data.getParcelableExtra<AlarmItem>("newAlarm")
            if (newAlarm != null) {
                dbHelper.insertAlarm(newAlarm)
                val updatedAlarms = dbHelper.getAllAlarms().toMutableList()
                arrayAdapter.updateAlarms(updatedAlarms)
            }
        }

        if (requestCode == DELETE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val deletedAlarmId = data.getIntExtra("deletedAlarmId", -1)
            if (deletedAlarmId != -1) {
                dbHelper.deleteAlarm(deletedAlarmId)
                val updatedAlarms = dbHelper.getAllAlarms().toMutableList()
                arrayAdapter.updateAlarms(updatedAlarms)
            }
        }
    }

    private fun requestExactAlarmPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)

            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 2
        private const val DELETE_REQUEST_CODE = 3
    }
}
