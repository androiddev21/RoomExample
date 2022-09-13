package com.example.room.data.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user_data")
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val age: Int,
    @Embedded
    val address: Address,
    val profilePhoto: Bitmap
): Parcelable

@Parcelize
data class Address(val city: String, val street: String): Parcelable