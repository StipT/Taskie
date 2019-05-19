package com.tstipanic.taskie.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tstipanic.taskie.R
import com.tstipanic.taskie.ui.adapters.TaskPagerAdapter
import com.tstipanic.taskie.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_pager.*

class ViewPagerFragment: BaseFragment() {



    override fun getLayoutResourceId(): Int = R.layout.fragment_pager



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        viewPager.adapter = TaskPagerAdapter(childFragmentManager, context?.applicationContext)
        tabs.setupWithViewPager(viewPager)
    }

    companion object {
        fun newInstance(): Fragment {
            return ViewPagerFragment()
        }
    }
}