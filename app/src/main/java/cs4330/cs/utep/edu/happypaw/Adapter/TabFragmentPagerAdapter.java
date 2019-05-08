package cs4330.cs.utep.edu.happypaw.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cs4330.cs.utep.edu.happypaw.Fragment.TripFragment;
import cs4330.cs.utep.edu.happypaw.Fragment.VetTabFragment;
import cs4330.cs.utep.edu.happypaw.R;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String[] tabNames;

    public TabFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.tabNames =  context.getResources().getStringArray(R.array.tabs);

    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new TripFragment();
        else
            return new VetTabFragment();
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return tabNames.length;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }
}
