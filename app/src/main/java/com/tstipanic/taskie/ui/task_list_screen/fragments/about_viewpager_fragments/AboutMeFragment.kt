package com.tstipanic.taskie.ui.task_list_screen.fragments.about_viewpager_fragments

import androidx.fragment.app.Fragment
import com.tstipanic.taskie.R
import com.tstipanic.taskie.ui.base.BaseFragment

class AboutMeFragment: BaseFragment() {
    override fun getLayoutResourceId(): Int = R.layout.fragment_about_me

    companion object {
        fun newInstance(): Fragment {
            return AboutMeFragment()
        }
    }
}