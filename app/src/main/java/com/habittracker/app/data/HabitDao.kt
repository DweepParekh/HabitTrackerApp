package com.habittracker.app.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): LiveData<List<Habit>>
    
    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitById(habitId: Long): Habit?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long
    
    @Update
    suspend fun updateHabit(habit: Habit)
    
    @Delete
    suspend fun deleteHabit(habit: Habit)
    
    @Query("SELECT COUNT(*) FROM habits")
    suspend fun getHabitCount(): Int
    
    @Query("SELECT SUM(totalCompletions) FROM habits")
    suspend fun getTotalCompletions(): Int?
    
    @Query("SELECT MAX(longestStreak) FROM habits")
    suspend fun getLongestStreak(): Int?
}
