package com.example.prolog365.db

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import java.time.LocalDate

@Entity(tableName = "table_schedule")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val idx: Long = 0,
    val scheduleName: String,
    val date: String,
    val phoneNumber: String,
    val picture: String, // file path of picture
)

@Database(entities = [ScheduleEntity::class], version = 1)
abstract class ScheduleDatabase : RoomDatabase(){
    abstract fun scheduleDao() : ScheduleDao
}

@Dao
interface ScheduleDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ScheduleEntity)

    @Query("Select * FROM table_schedule")
    suspend fun getAll(): List<ScheduleEntity>

    @Delete
    fun delete(entity : ScheduleEntity)

    @Query("DELETE FROM table_schedule")
    fun clear()

    @Query("SELECT * FROM table_schedule WHERE scheduleName = :scheduleName")
    fun getScheduleWithName(scheduleName: String) : List<ScheduleEntity>

    @Query("SELECT * FROM table_schedule WHERE date = :date")
    fun getScheduleWithDate(date: String) : List<ScheduleEntity>

    @Query("SELECT * FROM table_schedule WHERE phoneNumber = :phonenumber")
    fun getScheduleWithPhonenumber(phonenumber: String) : List<ScheduleEntity>
}

class ScheduleDB {
    companion object {
        var scheduleDatabase : ScheduleDatabase? = null

        @SuppressLint("UseRequireInsteadOfGet")
        fun initDB(fragment: Fragment) {
            if (scheduleDatabase == null) {
                scheduleDatabase =
                    fragment.activity?.let {
                        Room.databaseBuilder(
                            it,
                            ScheduleDatabase::class.java,
                            "table_schedule"
                        ).build()
                    }
            }
        }

        suspend fun insertDB(_scheduleName: String, _date: LocalDate, _phoneNumber: String, _picture: String){

            var format_phonenumber = PhonebookDB.formatPhonenumber(_phoneNumber)
            val entity = ScheduleEntity(scheduleName = _scheduleName, date = _date.toString(), phoneNumber = format_phonenumber, picture = _picture)
            scheduleDatabase?.scheduleDao()?.insert(entity)
        }

        suspend fun logDB(){
            val scheduleList = scheduleDatabase?.scheduleDao()?.getAll()
            scheduleList?.forEachIndexed{
                index, scheduleEntity ->
                Log.d("MyLog", "Schedule Index : " + scheduleEntity.idx + " | Schedule Name : " + scheduleEntity.scheduleName + " | Date : " + scheduleEntity.date + " | Phone Number : " + scheduleEntity.phoneNumber + " | Picture Location : " + scheduleEntity.picture)
            }
        }

        suspend fun getScheduleWithPhonenumber(phonenumber: String): List<ScheduleEntity>? {
            return scheduleDatabase?.scheduleDao()?.getScheduleWithPhonenumber(phonenumber)
        }

        suspend fun getScheduleWithDate(date: LocalDate): List<ScheduleEntity>? {
            return scheduleDatabase?.scheduleDao()?.getScheduleWithDate(date.toString())
        }

        suspend fun getEverySchedule(): List<ScheduleEntity>?{
            return scheduleDatabase?.scheduleDao()?.getAll()
        }

        suspend fun clearDB(){
            scheduleDatabase?.scheduleDao()?.clear()
        }
    }



}