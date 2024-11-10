package ViewModel

import Model.AlarmItem
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AlarmDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "alarmDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "alarms"  // 8이 잘못 붙은 부분 수정
        private const val COLUMN_ID = "id"
        private const val COLUMN_AM_PM = "am_pm"
        private const val COLUMN_HOUR = "hour"
        private const val COLUMN_MINUTE = "minute"
        private const val COLUMN_MONTH = "month"
        private const val COLUMN_DAY = "day"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_AM_PM TEXT,
                $COLUMN_HOUR INTEGER,
                $COLUMN_MINUTE INTEGER,
                $COLUMN_MONTH TEXT,
                $COLUMN_DAY TEXT
            );
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 알람 추가
    fun insertAlarm(alarm: AlarmItem) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_AM_PM, alarm.amPm)
            put(COLUMN_HOUR, alarm.hour)
            put(COLUMN_MINUTE, alarm.minute)
            put(COLUMN_MONTH, alarm.month)
            put(COLUMN_DAY, alarm.day)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // 알람 삭제
    fun deleteAlarm(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    // 알람 목록 불러오기
    fun getAllAlarms(): List<AlarmItem> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val alarms = mutableListOf<AlarmItem>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)) // id 값 가져오기
            val amPm = cursor.getString(cursor.getColumnIndex(COLUMN_AM_PM))
            val hour = cursor.getInt(cursor.getColumnIndex(COLUMN_HOUR))
            val minute = cursor.getInt(cursor.getColumnIndex(COLUMN_MINUTE))
            val month = cursor.getString(cursor.getColumnIndex(COLUMN_MONTH))
            val day = cursor.getString(cursor.getColumnIndex(COLUMN_DAY))

            // AlarmItem 생성 시 id 값도 포함시킴
            alarms.add(AlarmItem(id, amPm, hour, minute, month, day))
        }

        cursor.close()
        return alarms
    }

    // 알람 목록 업데이트
    fun updateAlarms(alarms: List<AlarmItem>) {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME") // 기존 알람 모두 삭제

        // 새 알람 목록 삽입
        alarms.forEach { alarm ->
            val values = ContentValues().apply {
                put(COLUMN_AM_PM, alarm.amPm)
                put(COLUMN_HOUR, alarm.hour)
                put(COLUMN_MINUTE, alarm.minute)
                put(COLUMN_MONTH, alarm.month)
                put(COLUMN_DAY, alarm.day)
            }
            db.insert(TABLE_NAME, null, values)
        }
        db.close()
    }
}
