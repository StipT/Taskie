package com.tstipanic.taskie.ui.activities.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tstipanic.taskie.R
import com.tstipanic.taskie.common.showFragment
import com.tstipanic.taskie.ui.fragments.TasksFragment
import com.tstipanic.taskie.ui.fragments.ViewPagerFragment
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutResourceId())
        setUpNavigationListener()
        setUpUi()
    }

    protected fun showFragment(fragment: Fragment){
        showFragment(R.id.fragmentContainer, fragment)
    }

    abstract fun getLayoutResourceId(): Int
    abstract fun setUpUi()

    private fun setUpNavigationListener() {
        bottom_navigation.setOnNavigationItemSelectedListener{
            lateinit var selectedFragment: Fragment
            when(it.itemId){
                R.id.nav_list -> selectedFragment = TasksFragment.newInstance()
                R.id.nav_about -> selectedFragment = ViewPagerFragment.newInstance()
            }

            showFragment(fragment = selectedFragment)
            return@setOnNavigationItemSelectedListener true
        }

    }
}