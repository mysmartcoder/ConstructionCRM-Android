package com.construction.android.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.view.LineVeriticalView;
import com.construction.android.view.LineView;

@SuppressLint("InflateParams")
public class LineChartReportFragment extends Fragment implements OnClickListener {

	private MyFragmentActivity mActivity;
	private View rootView;
	private LineView lineView;
	private LineVeriticalView lineViewVertical;

	private GridView mLinearLayoutActor;
	private LinearLayout mLinearLayoutContent;
	
	private String mStringJsonData="";
	private String[] mArrayColor ;
	ArrayList<String> listActor;

	public LineChartReportFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_line_chart_reports, container, false);

		mActivity = (MyFragmentActivity) getActivity();
		mActivity.setTitle(R.string.lbl_report_line_chart);

		getWidgetRefrence(rootView);
		registerOnClick();

		
		mStringJsonData = getArguments().getString(getString(R.string.bunble_report_data));
		setChartData();
		
		lineViewVertical.setDrawDotLine(true);
		lineViewVertical.setShowPopup(LineView.SHOW_POPUPS_NONE);
		
		lineView.setDrawDotLine(true);
		lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);
		return rootView;
	}

	/**
	 * Method call will get IDs from xml file.
	 * 
	 * @param v
	 */
	private void getWidgetRefrence(View v) {

		lineView = (LineView) v.findViewById(R.id.line_view);
		lineViewVertical = (LineVeriticalView) v.findViewById(R.id.line_view_vertical);
		mLinearLayoutActor = (GridView) v.findViewById(R.id.fragment_line_chart_report_linear_actor);
		mLinearLayoutContent = (LinearLayout)v.findViewById(R.id.fragment_line_chart_report_linear_main_content);

	}

	/**
	 * Method call will register OnClick() Events.
	 */
	private void registerOnClick() {


	}

	/**
	 * Method call will fire onClick event;
	 */
	@Override
	public void onClick(View v) {


	}
	
	/**
	 * Method will set chart data.
	 */
	public void setChartData()
	{
		try {
			int size = 0;
			ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
//			mLinearLayoutActor.removeAllViews();
			JSONObject mJsonObjectMain = new JSONObject(mStringJsonData);
			JSONArray mJsonArrayMain = mJsonObjectMain.getJSONArray("planActors");
			mArrayColor = new String[mJsonArrayMain.length()];
			listActor = new ArrayList<String>();
			
			for (int i = 0; i < mJsonArrayMain.length(); i++) {
				JSONObject mJsonObjectActor = mJsonArrayMain.getJSONObject(i);
				mArrayColor[i] = getRandomColor();
				listActor.add(mJsonObjectActor.getString("actorName"));
//				addActor(mJsonObjectActor.getString("actorName"), mArrayColor[i], i);
				
				JSONObject mJsonObjectPerctange = mJsonObjectActor.getJSONObject("percentages");
				size = mJsonObjectPerctange.length();
				
				ArrayList<Integer> mArrayListMaterial = new ArrayList<Integer>();
				for (int j = 0; j < size; j++) {
					
					String value = mJsonObjectPerctange.getString(String.valueOf(j+1));
					int perctangeValue = 0;
					if(value!=null & !value.equalsIgnoreCase("null"))
						perctangeValue = Integer.parseInt(value);
					mArrayListMaterial.add(perctangeValue);
					
//					System.out.println("value..."+j+"  : "+perctangeValue);
				}
				dataLists.add(mArrayListMaterial);
			}
			
			ArrayList<String> test = new ArrayList<String>();
			for (int i = 0; i < size; i++) {
				test.add(String.valueOf(i + 1));
			}

			lineView.setBottomTextList(test);
			lineView.setColorArray(mArrayColor);
			lineView.setDataList(dataLists);
			
			lineViewVertical.setBottomTextList(test);
			lineViewVertical.setColorArray(mArrayColor);
			lineViewVertical.setDataList(dataLists);
			
			mLinearLayoutActor.setAdapter(new ActorAdapter());
			mLinearLayoutContent.setVisibility(View.VISIBLE);
			
			if(mJsonArrayMain.length() == 0)
			{
				mLinearLayoutContent.setVisibility(View.GONE);
				mActivity.getCommonMethod().showDialog(getString(R.string.app_name), getString(R.string.alt_reports_data_not_founds), true);
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Method will add actor
	 */
	public void addActor(String actor_name, String colorString, int pos)
	{
		View mView = (View)mActivity.getLayoutInflater().inflate(R.layout.view_chart_actor, null);
		if(pos % 2 ==0 )
			mView.setBackgroundColor(getResources().getColor(R.color.ready_task_txt_bg));
		else
			mView.setBackgroundColor(getResources().getColor(R.color.white));
		
		TextView mTextView = (TextView)mView.findViewById(R.id.view_chart_actor_textview_material);
		View mViewColor = (View)mView.findViewById(R.id.view_chart_actor_view);
		mViewColor.setBackgroundColor(Color.parseColor(colorString));
		mTextView.setText(actor_name);
		mLinearLayoutActor.addView(mView);
	}
	
	/**
	 * Method will generate random color
	 * @return color
	 */
	public String getRandomColor() {
	    String[] letters = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
	    String color = "#";
	    for (int i = 0; i < 6; i++ ) {
	        color += letters[(int) Math.floor(Math.random() * 16)];
	    }
	    return color;
	}

	
	/**
	 * Load actor data in listview
	 * @author npatel
	 */
	public class ActorAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listActor.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				
				holder = new ViewHolder();
				convertView = mActivity.getLayoutInflater().inflate(R.layout.view_chart_actor, null);

				holder.mTextViewTitle = (TextView) convertView.findViewById(R.id.view_chart_actor_textview_material);
				holder.mView = (View) convertView.findViewById(R.id.view_chart_actor_view);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.mTextViewTitle.setText(listActor.get(position));
			holder.mView.setBackgroundColor(Color.parseColor(mArrayColor[position]));
			
			return convertView;
		}
		
	}
	
	
	public class ViewHolder {
		TextView mTextViewTitle;
		View mView;
	}
}
