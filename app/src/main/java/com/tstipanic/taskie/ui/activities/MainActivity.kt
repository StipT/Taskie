package com.tstipanic.taskie.ui.activities

import com.tstipanic.taskie.R
import com.tstipanic.taskie.ui.activities.base.BaseActivity
import com.tstipanic.taskie.ui.fragments.TasksFragment

class MainActivity : BaseActivity() {

    override fun getLayoutResourceId() = R.layout.activity_main

    override fun setUpUi() {
        showFragment(TasksFragment.newInstance())
    }

}