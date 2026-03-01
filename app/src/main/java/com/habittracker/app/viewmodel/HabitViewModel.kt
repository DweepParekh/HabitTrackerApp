package com.habittracker.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.habittracker.app.data.*
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HabitRepository
    
    val allHabits: LiveData<List<Habit>>
    val allRewards: LiveData<List<Reward>>
    val userStats: LiveData<UserStats?>
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = HabitRepository(database)
        allHabits = repository.allHabits
        allRewards = repository.allRewards
        userStats = repository.userStats
        
        viewModelScope.launch {
            repository.initializeUserStats()
        }
    }
    
    fun insertHabit(name: String, description: String, points: Int) = viewModelScope.launch {
        val habit = Habit(
            name = name,
            description = description,
            pointsPerCompletion = points
        )
        repository.insertHabit(habit)
    }
    
    fun updateHabit(habit: Habit) = viewModelScope.launch {
        repository.updateHabit(habit)
    }
    
    fun deleteHabit(habit: Habit) = viewModelScope.launch {
        repository.deleteHabit(habit)
    }
    
    fun completeHabit(habitId: Long) = viewModelScope.launch {
        repository.completeHabit(habitId)
    }
    
    fun insertReward(name: String, cost: Int) = viewModelScope.launch {
        val reward = Reward(
            name = name,
            pointCost = cost
        )
        repository.insertReward(reward)
    }
    
    fun updateReward(reward: Reward) = viewModelScope.launch {
        repository.updateReward(reward)
    }
    
    fun deleteReward(reward: Reward) = viewModelScope.launch {
        repository.deleteReward(reward)
    }
    
    fun redeemReward(rewardId: Long, onSuccess: () -> Unit, onFailure: () -> Unit) = viewModelScope.launch {
        val success = repository.redeemReward(rewardId)
        if (success) {
            onSuccess()
        } else {
            onFailure()
        }
    }
    
    fun getStatistics(callback: (Map<String, Any>) -> Unit) = viewModelScope.launch {
        val stats = repository.getStatistics()
        callback(stats)
    }
}
