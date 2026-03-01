package com.habittracker.app.data

import androidx.lifecycle.LiveData
import java.util.Calendar

class HabitRepository(private val database: AppDatabase) {
    private val habitDao = database.habitDao()
    private val rewardDao = database.rewardDao()
    private val userStatsDao = database.userStatsDao()
    
    val allHabits: LiveData<List<Habit>> = habitDao.getAllHabits()
    val allRewards: LiveData<List<Reward>> = rewardDao.getAllRewards()
    val userStats: LiveData<UserStats?> = userStatsDao.getUserStats()
    
    suspend fun insertHabit(habit: Habit) = habitDao.insertHabit(habit)
    
    suspend fun updateHabit(habit: Habit) = habitDao.updateHabit(habit)
    
    suspend fun deleteHabit(habit: Habit) = habitDao.deleteHabit(habit)
    
    suspend fun completeHabit(habitId: Long) {
        val habit = habitDao.getHabitById(habitId) ?: return
        
        if (habit.isCompletedToday()) return
        
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val lastCompleted = Calendar.getInstance().apply {
            if (habit.lastCompletedDate != 0L) {
                timeInMillis = habit.lastCompletedDate
            }
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val newStreak = if (habit.lastCompletedDate != 0L && 
                             lastCompleted.timeInMillis == yesterday.timeInMillis) {
            habit.currentStreak + 1
        } else {
            1
        }
        
        val newLongestStreak = maxOf(habit.longestStreak, newStreak)
        
        val updatedHabit = habit.copy(
            lastCompletedDate = System.currentTimeMillis(),
            currentStreak = newStreak,
            longestStreak = newLongestStreak,
            totalCompletions = habit.totalCompletions + 1
        )
        
        habitDao.updateHabit(updatedHabit)
        
        val totalPoints = habit.pointsPerCompletion + updatedHabit.calculateStreakBonus()
        val xpGained = habit.pointsPerCompletion
        
        userStatsDao.addPoints(totalPoints)
        userStatsDao.addXP(xpGained)
        
        val currentStats = userStatsDao.getUserStatsSync()
        if (currentStats != null) {
            val newLevel = currentStats.calculateLevel(currentStats.totalXP)
            if (newLevel > currentStats.level) {
                val xpForCurrentLevel = currentStats.currentXP - (currentStats.level * 100)
                userStatsDao.updateUserStats(
                    currentStats.copy(
                        level = newLevel,
                        currentXP = xpForCurrentLevel
                    )
                )
            }
        }
    }
    
    suspend fun insertReward(reward: Reward) = rewardDao.insertReward(reward)
    
    suspend fun updateReward(reward: Reward) = rewardDao.updateReward(reward)
    
    suspend fun deleteReward(reward: Reward) = rewardDao.deleteReward(reward)
    
    suspend fun redeemReward(rewardId: Long): Boolean {
        val reward = rewardDao.getRewardById(rewardId) ?: return false
        val stats = userStatsDao.getUserStatsSync() ?: return false
        
        if (stats.availablePoints >= reward.pointCost) {
            userStatsDao.spendPoints(reward.pointCost)
            rewardDao.updateReward(reward.copy(timesRedeemed = reward.timesRedeemed + 1))
            return true
        }
        return false
    }
    
    suspend fun initializeUserStats() {
        if (userStatsDao.getUserStatsSync() == null) {
            userStatsDao.insertUserStats(UserStats())
        }
    }
    
    suspend fun getStatistics(): Map<String, Any> {
        val habitCount = habitDao.getHabitCount()
        val totalCompletions = habitDao.getTotalCompletions() ?: 0
        val longestStreak = habitDao.getLongestStreak() ?: 0
        val stats = userStatsDao.getUserStatsSync()
        
        val completionRate = if (habitCount > 0) {
            (totalCompletions.toFloat() / habitCount.toFloat() * 100).toInt()
        } else {
            0
        }
        
        return mapOf(
            "totalHabits" to habitCount,
            "completionRate" to completionRate,
            "totalPoints" to (stats?.totalPointsEarned ?: 0),
            "longestStreak" to longestStreak
        )
    }
}
