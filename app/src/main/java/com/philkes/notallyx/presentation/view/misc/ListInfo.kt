package com.philkes.notallyx.presentation.view.misc

import android.content.Context
import com.philkes.notallyx.R
import java.text.DateFormat
import java.util.Date
import org.ocpsoft.prettytime.PrettyTime

sealed interface ListInfo {

    val title: Int

    val key: String
    val defaultValue: String

    fun getEntryValues(): Array<String>

    fun getEntries(context: Context): Array<String>

    fun convertToValues(ids: Array<Int>, context: Context): Array<String> {
        return Array(ids.size) { index ->
            val id = ids[index]
            context.getString(id)
        }
    }
}

object View : ListInfo {
    const val list = "list"
    const val grid = "grid"

    override val title = R.string.view
    override val key = "view"
    override val defaultValue = list

    override fun getEntryValues() = arrayOf(list, grid)

    override fun getEntries(context: Context): Array<String> {
        val ids = arrayOf(R.string.list, R.string.grid)
        return convertToValues(ids, context)
    }
}

object Theme : ListInfo {
    const val dark = "dark"
    const val light = "light"
    const val followSystem = "followSystem"

    override val title = R.string.theme
    override val key = "theme"
    override val defaultValue = followSystem

    override fun getEntryValues() = arrayOf(dark, light, followSystem)

    override fun getEntries(context: Context): Array<String> {
        val ids = arrayOf(R.string.dark, R.string.light, R.string.follow_system)
        return convertToValues(ids, context)
    }
}

object DateFormat : ListInfo {
    const val none = "none"
    const val relative = "relative"
    const val absolute = "absolute"

    override val title = R.string.creation_date_format
    override val key = "dateFormat"
    override val defaultValue = relative

    override fun getEntryValues() = arrayOf(none, relative, absolute)

    override fun getEntries(context: Context): Array<String> {
        val none = context.getString(R.string.none)
        val date = Date(System.currentTimeMillis() - 86400000)
        val relative = PrettyTime().format(date)
        val absolute = DateFormat.getDateInstance(DateFormat.FULL).format(date)
        return arrayOf(none, relative, absolute)
    }
}

object TextSize : ListInfo {
    const val small = "small"
    const val medium = "medium"
    const val large = "large"

    override val title = R.string.text_size
    override val key = "textSize"
    override val defaultValue = medium

    override fun getEntryValues() = arrayOf(small, medium, large)

    override fun getEntries(context: Context): Array<String> {
        val ids = arrayOf(R.string.small, R.string.medium, R.string.large)
        return convertToValues(ids, context)
    }

    fun getEditBodySize(textSize: String): Float {
        return when (textSize) {
            small -> 14f
            medium -> 16f
            large -> 18f
            else -> throw IllegalArgumentException("Invalid : $textSize")
        }
    }

    fun getEditTitleSize(textSize: String): Float {
        return when (textSize) {
            small -> 18f
            medium -> 20f
            large -> 22f
            else -> throw IllegalArgumentException("Invalid : $textSize")
        }
    }

    fun getDisplayBodySize(textSize: String): Float {
        return when (textSize) {
            small -> 12f
            medium -> 14f
            large -> 16f
            else -> throw IllegalArgumentException("Invalid : $textSize")
        }
    }

    fun getDisplayTitleSize(textSize: String): Float {
        return when (textSize) {
            small -> 14f
            medium -> 16f
            large -> 18f
            else -> throw IllegalArgumentException("Invalid : $textSize")
        }
    }
}

object ListItemSorting : ListInfo {
    const val noAutoSort = "noAutoSort"
    const val autoSortByChecked = "autoSortByChecked"

    override val title = R.string.checked_list_item_sorting
    override val key = "checkedListItemSorting"
    override val defaultValue = noAutoSort

    override fun getEntryValues() = arrayOf(noAutoSort, autoSortByChecked)

    override fun getEntries(context: Context): Array<String> {
        val ids = arrayOf(R.string.no_auto_sort, R.string.auto_sort_by_checked)
        return convertToValues(ids, context)
    }
}

object NotesSorting : ListInfo {
    const val autoSortByCreationDate = "autoSortByCreationDate"
    const val autoSortByModifiedDate = "autoSortByModifiedDate"
    const val autoSortByTitle = "autoSortByTitle"

    override val title = R.string.notes_sorted_by
    override val key = "notesSorting"
    const val directionKey = "notesSortingDirection"
    override val defaultValue = autoSortByCreationDate
    val defaultValueDirection = SortDirection.DESC.name

    override fun getEntryValues() =
        arrayOf(autoSortByCreationDate, autoSortByModifiedDate, autoSortByTitle)

    override fun getEntries(context: Context): Array<String> {
        val ids = arrayOf(R.string.creation_date, R.string.modified_date, R.string.title)
        return convertToValues(ids, context)
    }

    fun getSortIconResId(sortBy: String): Int {
        return when (sortBy) {
            autoSortByModifiedDate -> R.drawable.edit_calendar
            autoSortByTitle -> R.drawable.sort_by_alpha
            else -> R.drawable.calendar_add_on
        }
    }
}

enum class SortDirection(val textResId: Int, val iconResId: Int) {
    ASC(R.string.ascending, R.drawable.arrow_upward),
    DESC(R.string.descending, R.drawable.arrow_downward),
}

object BiometricLock : ListInfo {
    const val enabled = "enabled"
    const val disabled = "disabled"

    override val title = R.string.biometric_lock
    override val key = "biometricLock"
    override val defaultValue = disabled

    override fun getEntryValues() = arrayOf(enabled, disabled)

    override fun getEntries(context: Context): Array<String> {
        val ids = arrayOf(R.string.enabled, R.string.disabled)
        return convertToValues(ids, context)
    }
}
