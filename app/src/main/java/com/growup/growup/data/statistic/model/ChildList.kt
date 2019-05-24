package com.growup.growup.data.statistic.model

import android.os.Parcel
import android.os.Parcelable.Creator
import android.os.Parcelable


class ChildList : Parcelable {

    var title: String? = null


    constructor(title: String) {
        this.title = title
    }

    protected constructor(`in`: Parcel) {
        title = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(title)
    }

    companion object {

        val creator: Creator<ChildList> = object : Creator<ChildList> {
            override fun createFromParcel(`in`: Parcel): ChildList {
                return ChildList(`in`)
            }

            override fun newArray(size: Int): Array<ChildList?> {
                return arrayOfNulls(size)
            }
        }
    }
}
