package com.habittracker.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey
    val id: Int = 1,
    val level: Int = 1,
    val currentXP: Int = 0,
    val totalXP: Int = 0,
    val availablePoints: Int = 0,
    val totalPointsEarned: Int = 0
) {
    fun xpNeededForNextLevel(): Int {
        return level * 100
    }
    
    fun calculateLevel(totalXP: Int): Int {
        var xp = totalXP
        var currentLevel = 1
        
        while (xp >= currentLevel * 100) {
            xp -= currentLevel * 100
            currentLevel++
        }
        
        return currentLevel
    }
}
