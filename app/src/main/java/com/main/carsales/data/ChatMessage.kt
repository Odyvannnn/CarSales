package com.main.carsales.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ChatMessage(val id: String, val text: String, val toId: String, val fromId: String,
                  val timestamp: Long, val adId: String) : Parcelable {
    constructor() : this("", "", "", "", -1, "")
}
