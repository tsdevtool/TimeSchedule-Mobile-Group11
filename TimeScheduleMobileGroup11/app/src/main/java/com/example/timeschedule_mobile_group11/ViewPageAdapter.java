package com.example.timeschedule_mobile_group11;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.timeschedule_mobile_group11.fragment.ExamScheduleFragment;
import com.example.timeschedule_mobile_group11.fragment.HomeFragment;
import com.example.timeschedule_mobile_group11.fragment.OtherFragment;
import com.example.timeschedule_mobile_group11.fragment.ScheduleFragment;

public class ViewPageAdapter extends FragmentStateAdapter {


    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new ScheduleFragment();
            case 2:
                return new ExamScheduleFragment();
            case 3:
                return new OtherFragment();
            default:
                return new HomeFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
