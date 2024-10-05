package com.omgodse.notally.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.omgodse.notally.databinding.RecyclerListItemBinding
import com.omgodse.notally.preferences.Preferences
import com.omgodse.notally.recyclerview.DragCallback
import com.omgodse.notally.recyclerview.ListManager
import com.omgodse.notally.recyclerview.viewholder.MakeListVH
import com.omgodse.notally.sorting.ListItemSortedList

class MakeListAdapter(
    private val textSize: String,
    elevation: Float,
    private val preferences: Preferences,
    private val listManager: ListManager,
) : RecyclerView.Adapter<MakeListVH>() {

    private lateinit var list: ListItemSortedList
    private val callback = DragCallback(elevation, listManager)
    private val touchHelper = ItemTouchHelper(callback)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun getItemCount() = list.size()

    override fun onBindViewHolder(holder: MakeListVH, position: Int) {
        val item = list[position]
        holder.bind(item, position == 0, preferences.listItemSorting.value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeListVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerListItemBinding.inflate(inflater, parent, false)
        binding.root.background = parent.background
        return MakeListVH(binding, listManager, touchHelper, textSize)
    }

    internal fun setList(list: ListItemSortedList) {
        this.list = list
    }
}
