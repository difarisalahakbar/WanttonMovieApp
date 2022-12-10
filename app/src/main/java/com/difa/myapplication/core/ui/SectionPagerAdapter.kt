package com.difa.myapplication.core.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.difa.myapplication.item.MoviesFragment
import com.difa.myapplication.item.TvSeriesFragment

class SectionPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MoviesFragment()
            1 -> fragment = TvSeriesFragment()
        }

        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}