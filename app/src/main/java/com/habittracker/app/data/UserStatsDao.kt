package com.habittracker.app.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun getUserStats(): LiveData<UserStats?>
    
    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getUserStatsSync(): UserStats?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserStats(stats: UserStats)
    
    @Update
    suspend fun updateUserStats(stats: UserStats)
    
    @Query("UPDATE user_stats SET availablePoints = availablePoints + :points, totalPointsEarned = totalPointsEarned + :points WHERE id = 1")
    suspend fun addPoints(points: Int)
    
    @Query("UPDATE user_stats SET availablePoints = availablePoints - :points WHERE id = 1")
    suspend fun spendPoints(points: Int)
    
    @Query("UPDATE user_stats SET currentXP = currentXP + :xp, totalXP = totalXP + :xp WHERE id = 1")
    suspend fun addXP(xp: Int)
}
