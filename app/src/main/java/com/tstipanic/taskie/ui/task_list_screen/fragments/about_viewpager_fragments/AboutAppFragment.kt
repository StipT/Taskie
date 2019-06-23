package com.tstipanic.taskie.ui.task_list_screen.fragments.about_viewpager_fragments

import androidx.fragment.app.Fragment
import com.tstipanic.taskie.R
import com.tstipanic.taskie.ui.base.BaseFragment

class AboutAppFragment: BaseFragment() {
    override fun getLayoutResourceId(): Int = R.layout.fragment_about_app

    companion object {
        fun newInstance(): Fragment {
            return AboutAppFragment()
        }
    }
}