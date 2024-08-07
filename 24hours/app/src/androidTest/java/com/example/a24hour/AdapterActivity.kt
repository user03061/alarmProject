package com.example.a24hour

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.a24hour.databinding.ActivityAlarmlistitemBinding

class AdapterActivity(private val context: Context) : BaseAdapter() {

    private val listviewItem = ArrayList<Time>()
    private val arrayList: ArrayList<Time> = listviewItem // 백업 arrayList

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ActivityAlarmlistitemBinding
        var convertView = convertView

        if (convertView == null) {
            binding = ActivityAlarmlistitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            convertView = binding.root
            convertView.tag = binding
        } else {
            binding = convertView.tag as ActivityAlarmlistitemBinding
        }

        val time = arrayList[position]
        binding.amAndpm.text = time.amAndpm
        binding.hourSet.text = "${time.hour} 시"
        binding.minSet.text = "${time.minute} 분"
        binding.date.text = "${time.month} 월"
        binding.date2.text = "${time.day} 일"

        return convertView!!
    }

    fun addItem(hour: Int, minute: Int, amAndpm: String, month: String, day: String) {
        val time = Time()
        time.hour = hour
        time.minute = minute
        time.amAndpm = amAndpm
        time.month = month
        time.day = day

        listviewItem.add(time)
    }

    fun removeItem(position: Int) {
        if (listviewItem.size > 0) {
            listviewItem.removeAt(position)
        }
    }

    fun removeItem() {
        if (listviewItem.size > 0) {
            listviewItem.removeAt(listviewItem.size - 1)
        }
    }
}