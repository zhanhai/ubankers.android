package cn.com.ubankers.www.sns.view;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

public class SnsFragmentAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;
	private FragmentManager fm;
	private int count;

	public SnsFragmentAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
	}

	public SnsFragmentAdapter(FragmentManager fm,
			ArrayList<Fragment> fragments) {
		super(fm);
		this.fm = fm;
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(fragments!=null){
			count = fragments.size();	
		}
		return count;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void setFragments(ArrayList<Fragment> fragments) {
		if (this.fragments != null) {
			FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : this.fragments) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			this.fragments = fragments;
			notifyDataSetChanged();
		
		}
		
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		Object obj = super.instantiateItem(container, position);
		return obj;
	}

}
