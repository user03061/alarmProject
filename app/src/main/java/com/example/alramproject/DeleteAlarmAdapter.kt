package com.example.alramproject

import Model.AlarmItem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Switch
import com.example.alramproject.databinding.ActivityAlarmitemBinding

class DeleteAlarmAdapter(private val context: Context, private var alarms: MutableList<AlarmItem>) : BaseAdapter() {

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

        // 알람의 세부 정보 설정
        binding.amAndpm.text = item.amPm
        binding.hourSet.text = item.hour.toString()
        binding.minSet.text = item.minute.toString()
        binding.date.text = "${item.month}월"
        binding.date2.text = "${item.day}일"

        val switch: Switch = binding.root.findViewById(R.id.itemSet1)
        switch.isChecked = selectedItems[position]

        switch.setOnClickListener {
            selectedItems[position] = !selectedItems[position]
        }

        return view
    }

    // 알람 리스트 가져오는 메서드
    fun getAlarms(): MutableList<AlarmItem> {
        return alarms
    }

    // 선택된 알람들을 반환하는 메서드
    fun getSelectedAlarms(): List<AlarmItem> {
        return alarms.filterIndexed { index, _ -> selectedItems[index] }
    }

    // 알람을 리스트에서 제거하는 메서드
    fun removeItem(item: AlarmItem) {
        alarms.remove(item)
        selectedItems.clear()
        for (i in alarms.indices) {
            selectedItems.add(false)
        }
        notifyDataSetChanged()
    }
}
