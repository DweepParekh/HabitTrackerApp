package com.habittracker.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.habittracker.app.adapter.HabitAdapter
import com.habittracker.app.adapter.RewardAdapter
import com.habittracker.app.data.Habit
import com.habittracker.app.data.Reward
import com.habittracker.app.databinding.ActivityMainBinding
import com.habittracker.app.viewmodel.HabitViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: HabitViewModel by viewModels()
    private lateinit var habitAdapter: HabitAdapter
    private lateinit var rewardAdapter: RewardAdapter
    private var currentPoints = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRecyclerViews()
        setupObservers()
        setupTabLayout()
        setupFAB()
    }
    
    private fun setupRecyclerViews() {
        habitAdapter = HabitAdapter(
            onCompleteClick = { habit ->
                viewModel.completeHabit(habit.id)
                Toast.makeText(this, "Habit completed! +${habit.pointsPerCompletion + habit.calculateStreakBonus()} points", Toast.LENGTH_SHORT).show()
            },
            onLongClick = { habit ->
                showHabitOptionsDialog(habit)
            }
        )
        
        binding.habitsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = habitAdapter
        }
        
        rewardAdapter = RewardAdapter(
            onRedeemClick = { reward ->
                showRedeemConfirmDialog(reward)
            },
            onLongClick = { reward ->
                showRewardOptionsDialog(reward)
            },
            availablePoints = { currentPoints }
        )
        
        binding.rewardsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = rewardAdapter
        }
    }
    
    private fun setupObservers() {
        viewModel.allHabits.observe(this) { habits ->
            habitAdapter.setHabits(habits)
        }
        
        viewModel.allRewards.observe(this) { rewards ->
            rewardAdapter.setRewards(rewards)
        }
        
        viewModel.userStats.observe(this) { stats ->
            stats?.let {
                binding.tvLevel.text = it.level.toString()
                binding.tvXP.text = "${it.currentXP} / ${it.xpNeededForNextLevel()}"
                binding.tvPoints.text = it.availablePoints.toString()
                currentPoints = it.availablePoints
                rewardAdapter.notifyDataSetChanged()
            }
        }
    }
    
    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showHabitsTab()
                    1 -> showRewardsTab()
                    2 -> showStatsTab()
                }
            }
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
    }
    
    private fun showHabitsTab() {
        binding.habitsRecyclerView.visibility = View.VISIBLE
        binding.rewardsRecyclerView.visibility = View.GONE
        binding.statsScrollView.visibility = View.GONE
        binding.fabAdd.show()
    }
    
    private fun showRewardsTab() {
        binding.habitsRecyclerView.visibility = View.GONE
        binding.rewardsRecyclerView.visibility = View.VISIBLE
        binding.statsScrollView.visibility = View.GONE
        binding.fabAdd.show()
    }
    
    private fun showStatsTab() {
        binding.habitsRecyclerView.visibility = View.GONE
        binding.rewardsRecyclerView.visibility = View.GONE
        binding.statsScrollView.visibility = View.VISIBLE
        binding.fabAdd.hide()
        
        viewModel.getStatistics { stats ->
            runOnUiThread {
                binding.tvTotalHabits.text = stats["totalHabits"].toString()
                binding.tvCompletionRate.text = "${stats["completionRate"]}%"
                binding.tvTotalPoints.text = stats["totalPoints"].toString()
                binding.tvLongestStreak.text = "${stats["longestStreak"]} days"
            }
        }
    }
    
    private fun setupFAB() {
        binding.fabAdd.setOnClickListener {
            when (binding.tabLayout.selectedTabPosition) {
                0 -> showAddHabitDialog()
                1 -> showAddRewardDialog()
            }
        }
    }
    
    private fun showAddHabitDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_habit, null)
        val etName = dialogView.findViewById<TextInputEditText>(R.id.etHabitName)
        val etDescription = dialogView.findViewById<TextInputEditText>(R.id.etHabitDescription)
        val etPoints = dialogView.findViewById<TextInputEditText>(R.id.etPoints)
        
        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = etName.text.toString()
                val description = etDescription.text.toString()
                val points = etPoints.text.toString().toIntOrNull() ?: 10
                
                if (name.isNotBlank()) {
                    viewModel.insertHabit(name, description, points)
                    Toast.makeText(this, "Habit added!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showAddRewardDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_reward, null)
        val etName = dialogView.findViewById<TextInputEditText>(R.id.etRewardName)
        val etCost = dialogView.findViewById<TextInputEditText>(R.id.etRewardCost)
        
        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = etName.text.toString()
                val cost = etCost.text.toString().toIntOrNull() ?: 50
                
                if (name.isNotBlank()) {
                    viewModel.insertReward(name, cost)
                    Toast.makeText(this, "Reward added!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showHabitOptionsDialog(habit: Habit) {
        AlertDialog.Builder(this)
            .setTitle(habit.name)
            .setItems(arrayOf("Edit", "Delete")) { _, which ->
                when (which) {
                    0 -> showEditHabitDialog(habit)
                    1 -> showDeleteHabitDialog(habit)
                }
            }
            .show()
    }
    
    private fun showEditHabitDialog(habit: Habit) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_habit, null)
        val etName = dialogView.findViewById<TextInputEditText>(R.id.etHabitName)
        val etDescription = dialogView.findViewById<TextInputEditText>(R.id.etHabitDescription)
        val etPoints = dialogView.findViewById<TextInputEditText>(R.id.etPoints)
        
        etName.setText(habit.name)
        etDescription.setText(habit.description)
        etPoints.setText(habit.pointsPerCompletion.toString())
        
        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = etName.text.toString()
                val description = etDescription.text.toString()
                val points = etPoints.text.toString().toIntOrNull() ?: habit.pointsPerCompletion
                
                if (name.isNotBlank()) {
                    val updatedHabit = habit.copy(
                        name = name,
                        description = description,
                        pointsPerCompletion = points
                    )
                    viewModel.updateHabit(updatedHabit)
                    Toast.makeText(this, "Habit updated!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showDeleteHabitDialog(habit: Habit) {
        AlertDialog.Builder(this)
            .setTitle("Delete Habit?")
            .setMessage("Are you sure you want to delete \"${habit.name}\"? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteHabit(habit)
                Toast.makeText(this, "Habit deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showRewardOptionsDialog(reward: Reward) {
        AlertDialog.Builder(this)
            .setTitle(reward.name)
            .setItems(arrayOf("Edit", "Delete")) { _, which ->
                when (which) {
                    0 -> showEditRewardDialog(reward)
                    1 -> showDeleteRewardDialog(reward)
                }
            }
            .show()
    }
    
    private fun showEditRewardDialog(reward: Reward) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_reward, null)
        val etName = dialogView.findViewById<TextInputEditText>(R.id.etRewardName)
        val etCost = dialogView.findViewById<TextInputEditText>(R.id.etRewardCost)
        
        etName.setText(reward.name)
        etCost.setText(reward.pointCost.toString())
        
        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = etName.text.toString()
                val cost = etCost.text.toString().toIntOrNull() ?: reward.pointCost
                
                if (name.isNotBlank()) {
                    val updatedReward = reward.copy(
                        name = name,
                        pointCost = cost
                    )
                    viewModel.updateReward(updatedReward)
                    Toast.makeText(this, "Reward updated!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showDeleteRewardDialog(reward: Reward) {
        AlertDialog.Builder(this)
            .setTitle("Delete Reward?")
            .setMessage("Are you sure you want to delete \"${reward.name}\"?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteReward(reward)
                Toast.makeText(this, "Reward deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showRedeemConfirmDialog(reward: Reward) {
        AlertDialog.Builder(this)
            .setTitle("Redeem Reward?")
            .setMessage("Redeem \"${reward.name}\" for ${reward.pointCost} points?")
            .setPositiveButton("Redeem") { _, _ ->
                viewModel.redeemReward(
                    reward.id,
                    onSuccess = {
                        runOnUiThread {
                            Toast.makeText(this, "Reward redeemed! Enjoy!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onFailure = {
                        runOnUiThread {
                            Toast.makeText(this, "Not enough points!", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
