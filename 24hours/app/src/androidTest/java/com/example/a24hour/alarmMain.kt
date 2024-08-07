package com.example.a24hour

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.a24hour.databinding.ActivityMainscreenBinding
import java.text.SimpleDateFormat
import java.util.*


class alarmMain : AppCompatActivity() {

    private lateinit var binding: ActivityMainscreenBinding
    private lateinit var arrayAdapter: AdapterActivity
    private lateinit var listView: ListView
    private lateinit var textView: TextView
    private lateinit var handler: Handler
    private lateinit var mFormat: SimpleDateFormat
    private var adapterPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        arrayAdapter = AdapterActivity(this)

        binding.listView.adapter = arrayAdapter
        
        listView = binding.listView
        listView.adapter = arrayAdapter

        // ListView의 항목을 클릭했을 때 시간 변경 가능
        listView.setOnItemClickListener { parent, view, position, id ->
            adapterPosition = position
            arrayAdapter.removeItem(position)
            val intent = Intent(this@alarmMain, TimeSetActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        // Handler를 사용해서 실시간으로 시간을 출력
        handler = Handler(Looper.getMainLooper())
        mFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        textView = binding.current

        handler.post(object : Runnable {
            override fun run() {
                val cal = Calendar.getInstance()
                val strTime = mFormat.format(cal.time)
                textView.text = strTime
                textView.textSize = 30f
                handler.postDelayed(this, 1000)
            }
        })
    }

    companion object {
        private const val REQUEST_CODE = 2
    }
}
