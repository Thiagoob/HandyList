package com.celerocommerce.handylist.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonTypeConverter {
        @TypeConverter
        inline fun <reified  T> fromJson(json: String): T {
            return Gson().fromJson(json, object : TypeToken<T>() {}.type)
        }

        @TypeConverter
        inline fun <reified T> toJson(value: T): String {
            return Gson().toJson(value)
        }
}