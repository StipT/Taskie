package com.tstipanic.taskie.ui.fragments

import androidx.fragment.app.Fragment
import com.tstipanic.taskie.R
import com.tstipanic.taskie.ui.fragments.base.BaseFragment

class AboutAppFragment: BaseFragment() {
    override fun getLayoutResourceId(): Int = R.layout.fragment_about_app

    companion object {
        fun newInstance(): Fragment {
            return AboutAppFragment()
        }
    }
}