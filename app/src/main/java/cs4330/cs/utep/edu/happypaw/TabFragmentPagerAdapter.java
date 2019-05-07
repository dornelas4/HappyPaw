package cs4330.cs.utep.edu.happypaw;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cs4330.cs.utep.edu.happypaw.Fragment.TripFragment;
import cs4330.cs.utep.edu.happypaw.Fragment.VetFragment;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public TabFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new TripFragment();
        else
            return new VetFragment();
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
//        switch (position) {
//            case 0:
//                return mContext.getString(R.string.category_usefulinfo);
//            case 1:
//                return mContext.getString(R.string.category_places);
//            case 2:
//                return mContext.getString(R.string.category_food);
//            case 3:
//                return mContext.getString(R.string.category_nature);
//            default:
//                return null;
//        }
        return "Test";
    }
}
