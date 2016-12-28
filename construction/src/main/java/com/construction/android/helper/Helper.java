package com.construction.android.helper;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Helper {
	@SuppressLint("NewApi")
	public static void getListViewSize(ListView myListView) {

		  ListAdapter listAdapter = myListView.getAdapter();
		    if (listAdapter == null) {
		        return;
		    }
		    int desiredWidth = MeasureSpec.makeMeasureSpec(myListView.getWidth(), MeasureSpec.AT_MOST);
		    int totalHeight = 0;
		    View view = null;
		    for (int i = 0; i < listAdapter.getCount(); i++) {
		        view = listAdapter.getView(i, view, myListView);
		        if (i == 0) {
		            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
		        }
		        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
		        totalHeight += view.getMeasuredHeight();
		    }
		    ViewGroup.LayoutParams params = myListView.getLayoutParams();
		    params.height = totalHeight + (myListView.getDividerHeight() * (listAdapter.getCount() - 1));
		    myListView.setLayoutParams(params);
		    myListView.requestLayout();
		}
	
	

}
