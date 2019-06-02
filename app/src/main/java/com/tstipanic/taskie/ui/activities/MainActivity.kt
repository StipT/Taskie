package com.tstipanic.taskie.ui.activities

import com.tstipanic.taskie.R
import com.tstipanic.taskie.ui.activities.base.BaseActivity
import com.tstipanic.taskie.ui.fragments.TasksFragment
import com.tstipanic.taskie.ui.fragments.ViewPagerFragment
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