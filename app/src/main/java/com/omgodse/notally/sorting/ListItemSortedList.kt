package com.omgodse.notally.sorting

import androidx.recyclerview.widget.SortedList
import com.omgodse.notally.model.ListItem

class ListItemSortedList(callback: Callback<ListItem>) :
    SortedList<ListItem>(ListItem::class.java, callback) {

    override fun updateItemAt(index: Int, item: ListItem?) {
        updateChildStatus(item, index)
        super.updateItemAt(index, item)
        if (item?.isChild == false) {
            item.children = item.children.map { findById(it.id)!!.second }.toMutableList()
        }
    }

    override fun add(item: ListItem?): Int {
        val position = super.add(item)
        if (item?.isChild == true) {
            updateChildInParent(position, item)
        }
        return position
    }

    fun add(item: ListItem, isChild: Boolean?) {
        if (isChild != null) {
            forceItemIsChild(item, isChild)
        }
        add(item)
    }

    fun forceItemIsChild(item: ListItem, newValue: Boolean, resetBefore: Boolean = false) {
        if (resetBefore) {
            if (item.isChild) {
                // In this case it was already a child and moved to other position,
                // therefore reset the child association
                removeChildFromParent(item)
                item.isChild = false
            }
        }
        if (item.isChild != newValue) {
            if (!item.isChild) {
                item.children.clear()
            } else {
                removeChildFromParent(item)
            }
            item.isChild = newValue
        }
        if (item.isChild) {
            updateChildInParent(item.order!!, item)
        }
    }

    override fun removeItemAt(index: Int): ListItem {
        val item = this[index]
        val removedItem = super.removeItemAt(index)
        if (item?.isChild == true) {
            removeChildFromParent(item)
        }
        return removedItem
    }

    private fun separateChildrenFromParent(item: ListItem) {
        findParent(item)?.let { (_, parent) ->
            val childIndex = parent.children.indexOfFirst { child -> child.id == item.id }
            // If a child becomes a parent it inherits its children below it
            val separatedChildren =
                if (childIndex < parent.children.lastIndex)
                    parent.children.subList(childIndex + 1, parent.children.size)
                else listOf()
            item.children.clear()
            item.children.addAll(separatedChildren)
            while (parent.children.size >= childIndex + 1) {
                parent.children.removeAt(childIndex)
            }
        }
    }

    private fun updateChildStatus(item: ListItem?, index: Int) {
        val wasChild = this[index].isChild
        if (item?.isChild == true) {
            updateChildInParent(index, item)
        } else if (wasChild && item?.isChild == false) {
            // Child becomes parent
            separateChildrenFromParent(item)
        }
    }

    fun removeChildFromParent(item: ListItem) {
        findParent(item)?.let { (_, parent) ->
            val childIndex = parent.children.indexOfFirst { child -> child.id == item.id }
            parent.children.removeAt(childIndex)
        }
    }

    fun initializeChildren() {
        this.forEach {
            if (it.isChild) {
                updateChildInParent(it.order!!, it)
            }
        }
    }

    private fun updateChildInParent(position: Int, item: ListItem) {
        var childIndex: Int? = null
        var parentInfo = findParent(item)
        var parent: ListItem? = null
        if (parentInfo == null) {
            val parentPosition = findLastIsNotChild(position - 1)!!
            childIndex = position - parentPosition - 1
            parent = this[parentPosition]
        } else {
            parent = parentInfo.second
            childIndex = parent.children.indexOfFirst { child -> child.id == item.id }
            parent.children.removeAt(childIndex)
        }
        parent!!.children.add(childIndex, item)
        parent.children.addAll(childIndex + 1, item.children)
        item.children.clear()
    }

    /** @return position of the found item and its difference to index */
    private fun findLastIsNotChild(index: Int): Int? {
        var position = index
        while (this[position].isChild) {
            if (position < 0) {
                return null
            }
            position--
        }
        return position
    }
}