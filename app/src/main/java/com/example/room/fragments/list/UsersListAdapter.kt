package com.example.room.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.room.R
import com.example.room.data.model.User
import com.example.room.databinding.ItemUserBinding

class UsersListAdapter : RecyclerView.Adapter<UsersListAdapter.UserViewHolder>() {

    private val users = mutableListOf<User>()

    inner class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = users[position]
        holder.binding.tvName.text = holder.binding.root.context.getString(
            R.string.user_name_place_holder,
            currentItem.firstName,
            currentItem.lastName
        )
        holder.binding.tvAge.text = currentItem.age.toString()
        holder.binding.tvAddress.text = holder.binding.root.context.getString(
            R.string.user_address_place_holder,
            currentItem.address.city,
            currentItem.address.street
        )

        holder.binding.ivPhoto.load(currentItem.profilePhoto)

        holder.binding.itemLayout.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.binding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = users.size

    fun setData(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }
}