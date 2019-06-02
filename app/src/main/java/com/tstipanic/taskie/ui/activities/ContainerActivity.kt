package com.tstipanic.taskie.ui.activities

import com.tstipanic.taskie.R
import com.tstipanic.taskie.common.EXTRA_SCREEN_TYPE
import com.tstipanic.taskie.common.EXTRA_TASK_ID
import com.tstipanic.taskie.common.gone
import com.tstipanic.taskie.ui.activities.base.BaseActivity
import com.tstipanic.taskie.ui.fragments.TaskDetailsFragment
import kotlinx.android.synthetic.main.activity_main.*

class ContainerActivity: BaseActivity() {

    override fun getLayoutResourceId() = R.layout.activity_main

    override fun setUpUi() {
        bottomNav.gone()
        val screenType = intent.getStringExtra(EXTRA_SCREEN_TYPE)
        val id = intent.getStringExtra(EXTRA_TASK_ID)
        if (screenType.isNotEmpty()) {
            when (screenType) {
                SCREEN_TASK_DETAILS -> showFragment(TaskDetailsFragment.newInstance(id))
            }
        } else {
            finish()
        }
    }

    companion object{
        const val SCREEN_TASK_DETAILS = "task_details"
    }
}