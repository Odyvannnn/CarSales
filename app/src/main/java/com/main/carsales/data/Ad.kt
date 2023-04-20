package com.main.carsales.data

import com.google.firebase.firestore.DocumentId

data class Ad(
    val car_brand: String? = "",
    val car_model: String? = "",
    val year: String? = "",
    val mileage: String? = "",
    val engine_power: String? = "",
    val transmission: String? = "",
    val car_drive_type: String? = "",
    val price: String? = "",
    val status: String? = "",
    val username: String? = "",
    val city: String? ="",

    val pic1: String? = "",
    val pic2: String? = "",
    val pic3: String? = "",
    val pic4: String? = "",
    val pic5: String? = "",
    val pic6: String? = "",
    val pic7: String? = "",
    val pic8: String? = "",
    val pic9: String? = "",
    val seller_uid: String? ="",

    @DocumentId
    val adId: String? =""
)
