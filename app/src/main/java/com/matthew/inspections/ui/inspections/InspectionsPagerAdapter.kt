package com.matthew.inspections.ui.inspections

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter

class InspectionsPagerAdapter(activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {

    override fun getItemCount() = 3

    /**
     * 0 = Past
     * 1 = Current
     * 2 = Future
     */
    override fun createFragment(position: Int): Fragment {
        return InspectionsListFragment.getInstance(position)
    }

}