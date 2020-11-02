package com.celerocommerce.handylist.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.celerocommerce.handylist.models.Customer
import com.celerocommerce.handylist.persistence.main.CustomerDao
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity

@Database(entities = [CustomerPersistenceEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getCustomerDao(): CustomerDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }

}