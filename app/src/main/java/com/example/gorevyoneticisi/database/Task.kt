package com.example.gorevyoneticisi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_data_table")
data class Task (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    var id: Int,
    @ColumnInfo(name = "task_description")
    var description: String,
    @ColumnInfo(name = "task_period")
    var period: String,
)