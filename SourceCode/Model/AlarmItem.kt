package Model

import android.os.Parcel
import android.os.Parcelable

data class AlarmItem(
    val id: Int = 0, // 기본값 설정
    val amPm: String,
    val hour: Int,
    val minute: Int,
    val month: String,
    val day: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        amPm = parcel.readString() ?: "",
        hour = parcel.readInt(),
        minute = parcel.readInt(),
        month = parcel.readString() ?: "",
        day = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(amPm)
        parcel.writeInt(hour)
        parcel.writeInt(minute)
        parcel.writeString(month)
        parcel.writeString(day)
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
