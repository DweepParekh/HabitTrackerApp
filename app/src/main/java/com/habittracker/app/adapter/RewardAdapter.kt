package com.habittracker.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.habittracker.app.R
import com.habittracker.app.data.Reward

class RewardAdapter(
    private val onRedeemClick: (Reward) -> Unit,
    private val onLongClick: (Reward) -> Unit,
    private val availablePoints: () -> Int
) : RecyclerView.Adapter<RewardAdapter.RewardViewHolder>() {
    
    private var rewards = emptyList<Reward>()
    
    class RewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRewardName: TextView = itemView.findViewById(R.id.tvRewardName)
        val tvRewardCost: TextView = itemView.findViewById(R.id.tvRewardCost)
        val btnRedeem: MaterialButton = itemView.findViewById(R.id.btnRedeem)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reward, parent, false)
        return RewardViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val reward = rewards[position]
        
        holder.tvRewardName.text = reward.name
        holder.tvRewardCost.text = "${reward.pointCost} points"
        
        val canAfford = availablePoints() >= reward.pointCost
        holder.btnRedeem.isEnabled = canAfford
        holder.btnRedeem.alpha = if (canAfford) 1.0f else 0.5f
        
        holder.btnRedeem.setOnClickListener {
            if (canAfford) {
                onRedeemClick(reward)
            }
        }
        
        holder.itemView.setOnLongClickListener {
            onLongClick(reward)
            true
        }
    }
    
    override fun getItemCount() = rewards.size
    
    fun setRewards(rewards: List<Reward>) {
        this.rewards = rewards
        notifyDataSetChanged()
    }
}
