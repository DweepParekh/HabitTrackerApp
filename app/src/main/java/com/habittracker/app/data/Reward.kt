package com.habittracker.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val pointCost: Int,
    val timesRedeemed: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
