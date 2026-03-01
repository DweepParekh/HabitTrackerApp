package com.habittracker.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.habittracker.app.R
import com.habittracker.app.data.Habit

class HabitAdapter(
    private val onCompleteClick: (Habit) -> Unit,
    private val onLongClick: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {
    
    private var habits = emptyList<Habit>()
    
    class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHabitName: TextView = itemView.findViewById(R.id.tvHabitName)
        val tvHabitDescription: TextView = itemView.findViewById(R.id.tvHabitDescription)
        val tvStreak: TextView = itemView.findViewById(R.id.tvStreak)
        val tvPoints: TextView = itemView.findViewById(R.id.tvPoints)
        val btnComplete: MaterialButton = itemView.findViewById(R.id.btnComplete)
        val tvCompletedToday: TextView = itemView.findViewById(R.id.tvCompletedToday)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        
        holder.tvHabitName.text = habit.name
        holder.tvHabitDescription.text = habit.description
        
        val streakText = if (habit.currentStreak == 1) {
            "${habit.currentStreak} day streak"
        } else {
            "${habit.currentStreak} days streak"
        }
        holder.tvStreak.text = streakText
        
        val pointsWithBonus = habit.pointsPerCompletion + habit.calculateStreakBonus()
        holder.tvPoints.text = "$pointsWithBonus pts"
        
        val isCompleted = habit.isCompletedToday()
        holder.tvCompletedToday.visibility = if (isCompleted) View.VISIBLE else View.GONE
        holder.btnComplete.isEnabled = !isCompleted
        holder.btnComplete.alpha = if (isCompleted) 0.5f else 1.0f
        
        holder.btnComplete.setOnClickListener {
            if (!isCompleted) {
                onCompleteClick(habit)
            }
        }
        
        holder.itemView.setOnLongClickListener {
            onLongClick(habit)
            true
        }
    }
    
    override fun getItemCount() = habits.size
    
    fun setHabits(habits: List<Habit>) {
        this.habits = habits
        notifyDataSetChanged()
    }
}
