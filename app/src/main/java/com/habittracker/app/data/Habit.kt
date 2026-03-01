package com.habittracker.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val pointsPerCompletion: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val lastCompletedDate: Long = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalCompletions: Int = 0
) {
    fun isCompletedToday(): Boolean {
        if (lastCompletedDate == 0L) return false
        
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val lastCompleted = Calendar.getInstance().apply {
            timeInMillis = lastCompletedDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        return today.timeInMillis == lastCompleted.timeInMillis
    }
    
    fun calculateStreakBonus(): Int {
        return when {
            currentStreak >= 30 -> 5
            currentStreak >= 14 -> 3
            currentStreak >= 7 -> 2
            currentStreak >= 3 -> 1
            else -> 0
        }
    }
}
