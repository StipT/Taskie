package com.tstipanic.taskie.ui.task_list_screen.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tstipanic.taskie.R
import com.tstipanic.taskie.ui.task_list_screen.fragments.TasksFragment
import com.tstipanic.taskie.ui.task_list_screen.fragments.about_viewpager_fragments.AboutAppFragment
import com.tstipanic.taskie.ui.task_list_screen.fragments.about_viewpager_fragments.AboutMeFragment

class TaskPagerAdapter(fm: FragmentManager, val context: Context?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> AboutAppFragment.newInstance()
        1 -> AboutMeFragment.newInstance()
        else -> TasksFragment.newInstance()
    }

    override fun getCount() = 2


    override fun getPageTitle(position: Int): CharSequence? =
        when (position) {
            0 -> context?.getString(R.string.about_app_tab)
            1 -> context?.getString(R.string.about_me_tab)
            else -> ""
        }
}