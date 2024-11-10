package com.example.alramproject

import Model.AlarmItem
import android.content.Context
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.example.alramproject.databinding.ActivityAlarmitemBinding

class AdapterActivity(private val context: Context, private val alarms: MutableList<AlarmItem>) : BaseAdapter() {

    private val selectedItems = mutableListOf<Boolean>()

    init {
        for (i in alarms.indices) {
            selectedItems.add(false)
        }
    }

    override fun getCount(): Int = alarms.size

    override fun getItem(position: Int): AlarmItem = alarms[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ActivityAlarmitemBinding
        val view: View

        if (convertView == null) {
            binding = ActivityAlarmitemBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            binding = convertView.tag as ActivityAlarmitemBinding
            view = convertView
        }

        val item = getItem(position)

        binding.amAndpm.text = item.amPm
        binding.hourSet.text = item.hour.toString()
        binding.minSet.text = item.minute.toString()
        binding.date.text = "${item.month}월"
        binding.date2.text = "${item.day}일"

        // 스위치 클릭 시 선택 여부 변경
        val switch: Switch = binding.root.findViewById(R.id.itemSet1)
        switch.isChecked = selectedItems[position]

        switch.setOnClickListener {
            selectedItems[position] = !selectedItems[position]
        }

        return view
    }

    fun getSelectedAlarms(): List<AlarmItem> {
        return alarms.filterIndexed { index, _ -> selectedItems[index] }
    }

    fun updateAlarms(newAlarms: List<AlarmItem>) {
        alarms.clear()
        alarms.addAll(newAlarms)
        selectedItems.clear()
        selectedItems.addAll(List(newAlarms.size) { false })
        notifyDataSetChanged()
    }
}