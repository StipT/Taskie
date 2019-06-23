package com.tstipanic.taskie.ui.task_list_screen.activity

import com.tstipanic.taskie.R
import com.tstipanic.taskie.ui.base.BaseActivity
import com.tstipanic.taskie.ui.task_list_screen.fragments.TasksFragment
import com.tstipanic.taskie.ui.task_list_screen.fragments.about_viewpager_fragments.ViewPagerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutResourceId() = R.layout.activity_main

    override fun setUpUi() {
        setUpNavigationListener()
        showFragment(TasksFragment.newInstance())
    }

    private fun setUpNavigationListener() {
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_list -> showFragment(TasksFragment.newInstance())
                R.id.nav_about -> showFragment(ViewPagerFragment.newInstance())
                else -> showFragment(TasksFragment.newInstance())
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}