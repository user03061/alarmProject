package com.example.a24hour;

public class Time {
    var hour: Int = 0
    var minute: Int = 0
    var amAndpm: String = ""
    var month: String = ""
    var day: String = ""

    fun getHour(): Int {
        return hour
    }

    fun getMinute(): Int {
        return minute
    }

    fun getamAndpm(): String {
        return amAndpm
    }

    fun getMonth(): String {
        return month
    }

    fun getDay(): String {
        return day
    }

    fun setHour(hour: Int) {
        this.hour = hour
    }

    fun setMinute(minute: Int) {
        this.minute = minute
    }

    fun setamAndpm(amAndpm: String) {
        this.amAndpm = amAndpm
    }

    fun setMonth(month: String) {
        this.month = month
    }

    fun setDay(day: String) {
        this.day = day
    }
}
