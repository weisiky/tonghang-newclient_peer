package com.peer.activity;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peer.base.pBaseActivity;
import com.peer.fragment.ComeMsgFragment;
import com.peer.fragment.FriendsFragment;
import com.peer.fragment.HomeFragment;
import com.peer.fragment.MyFragment;
import com.peer.utils.pViewBox;

/**
 * mainactivity
 */
public class MainActivity extends pBaseActivity {

	/**
	 * fragment定义
	 */
	private HomeFragment homefragment;
	private ComeMsgFragment comemsgfragment;
	private FriendsFragment friendsfragment;
	private MyFragment myfragment;
	private Fragment[] fragments;

	private int index;
	private int currentTabIndex;

	private PageViewList pageViewaList;

	class PageViewList {
		/* bottom layout */
		private LinearLayout find, come, my, friends;
		private TextView tv_find, tv_come, tv_my, tv_friends, tv_newfriendsnum,
				showmessgenum;
		private ImageView iv_backfind, iv_backcome, iv_backfriends, iv_backmy;

		public LinearLayout ll_find, ll_come, ll_friends, ll_my;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		/* init fragment */
		homefragment = new HomeFragment();
		comemsgfragment = new ComeMsgFragment();
		friendsfragment = new FriendsFragment();
		myfragment = new MyFragment();
		fragments = new Fragment[] { homefragment, comemsgfragment,
				friendsfragment, myfragment };
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, homefragment)
				.add(R.id.fragment_container, comemsgfragment)
				.hide(comemsgfragment)
				.add(R.id.fragment_container, friendsfragment)
				.hide(friendsfragment).add(R.id.fragment_container, myfragment)
				.hide(myfragment).show(homefragment).commit();

		index = 0;
		pageViewaList.tv_find.setTextColor(getResources().getColor(
				R.color.bottomtextblue));
		pageViewaList.iv_backfind.setImageResource(R.drawable.peer_press);

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

	}

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		// return getLayoutInflater().inflate(R.layout.top_layout, null);
		return null;
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected View loadBottomLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_frabottom, null);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void onTabClicked(View v) {
		// TODO Auto-generated method stub
		pageViewaList.tv_find.setTextColor(getResources().getColor(
				R.color.bottomtextgray));
		pageViewaList.tv_come.setTextColor(getResources().getColor(
				R.color.bottomtextgray));
		pageViewaList.tv_friends.setTextColor(getResources().getColor(
				R.color.bottomtextgray));
		pageViewaList.tv_my.setTextColor(getResources().getColor(
				R.color.bottomtextgray));

		pageViewaList.iv_backfind.setImageResource(R.drawable.peer_nol);
		pageViewaList.iv_backcome.setImageResource(R.drawable.come_mess_nol);
		pageViewaList.iv_backfriends
				.setImageResource(R.drawable.find_label_nol);
		pageViewaList.iv_backmy.setImageResource(R.drawable.mysetting_nol);

		switch (v.getId()) {
		case R.id.ll_find:
			index = 0;
			pageViewaList.tv_find.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backfind.setImageResource(R.drawable.peer_press);
			break;
		case R.id.ll_come:
			index = 1;
			pageViewaList.tv_come.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backcome
					.setImageResource(R.drawable.come_mess_press);
			break;
		case R.id.ll_friends:
			index = 2;
			pageViewaList.tv_friends.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backfriends
					.setImageResource(R.drawable.find_label_press);
			break;
		case R.id.ll_my:
			index = 3;
			pageViewaList.tv_my.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backmy
					.setImageResource(R.drawable.mysetting_press);
			break;
		default:
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		currentTabIndex = index;
	}

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}

}
