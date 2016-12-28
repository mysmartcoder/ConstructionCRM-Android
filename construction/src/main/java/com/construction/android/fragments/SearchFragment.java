package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class SearchFragment extends Fragment implements OnClickListener {

	private MyFragmentActivity mActivity;
	private View rootView;

	private EditText mEditTextSearch;
	private ImageView mImageViewSearch;
	private ListView mListViewTasks;

	public SearchFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_search, container, false);

		mActivity = (MyFragmentActivity) getActivity();
		mActivity.setTitle(R.string.lbl_search);

		getWidgetRefrence(rootView);
		registerEvents();

		return rootView;
	}

	/**
	 * Method call will get IDs from xml file.
	 * @param v
	 */
	public void getWidgetRefrence(View v) {

		mEditTextSearch = (EditText) v.findViewById(R.id.view_searchbar_edittex_text);
		mImageViewSearch = (ImageView) v.findViewById(R.id.view_searchbar_imageview_send);
		mListViewTasks = (ListView) v.findViewById(R.id.fragment_search_listview_tasks);
	}

	/**
	 * Method call will register OnClick() Events.
	 */
	public void registerEvents() {
		mImageViewSearch.setOnClickListener(this);

	}

	/**
	 * Method call will fire onClick event;
	 */
	@Override
	public void onClick(View v) {

		if (v == mImageViewSearch) {

			Toast.makeText(mActivity, "Search Task", Toast.LENGTH_SHORT).show();
		}

	}

}
