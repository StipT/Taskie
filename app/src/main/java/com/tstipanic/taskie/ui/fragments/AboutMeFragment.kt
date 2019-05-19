package com.tstipanic.taskie.ui.fragments

import androidx.fragment.app.Fragment
import com.tstipanic.taskie.R
import com.tstipanic.taskie.ui.fragments.base.BaseFragment

class AboutMeFragment: BaseFragment() {
    override fun getLayoutResourceId(): Int = R.layout.fragment_about_me

    companion object {
        fun newInstance(): Fragment {
            return AboutMeFragment()
        }
    }
}