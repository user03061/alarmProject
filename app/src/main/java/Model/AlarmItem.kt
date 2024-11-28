package Model

import android.os.Parcel
import android.os.Parcelable

data class AlarmItem(
    val id: Int = 0, // 기본값 설정
    val amPm: String,
    val hour: Int,
    val minute: Int,
    val month: String,
    val day: String,
    var on_off: Boolean // Boolean으로 수정
) : Parcelable {

    // Parcel로부터 데이터 읽기
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        amPm = parcel.readString() ?: "",
        hour = parcel.readInt(),
        minute = parcel.readInt(),
        month = parcel.readString() ?: "",
        day = parcel.readString() ?: "",
        on_off = parcel.readByte() != 0.toByte() // Boolean 값 읽기
    )

    // Parcel로 데이터 쓰기
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(amPm)
        parcel.writeInt(hour)
        parcel.writeInt(minute)
        parcel.writeString(month)
        parcel.writeString(day)
        parcel.writeByte(if (on_off) 1 else 0) // Boolean 값을 Byte로 저장
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<AlarmItem> {
        override fun createFromParcel(parcel: Parcel): AlarmItem {
            return AlarmItem(parcel)
        }

        override fun newArray(size: Int): Array<AlarmItem?> {
            return arrayOfNulls(size)
        }
    }
}
