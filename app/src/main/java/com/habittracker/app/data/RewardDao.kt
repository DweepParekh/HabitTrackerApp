package com.habittracker.app.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards ORDER BY pointCost ASC")
    fun getAllRewards(): LiveData<List<Reward>>
    
    @Query("SELECT * FROM rewards WHERE id = :rewardId")
    suspend fun getRewardById(rewardId: Long): Reward?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReward(reward: Reward): Long
    
    @Update
    suspend fun updateReward(reward: Reward)
    
    @Delete
    suspend fun deleteReward(reward: Reward)
}
