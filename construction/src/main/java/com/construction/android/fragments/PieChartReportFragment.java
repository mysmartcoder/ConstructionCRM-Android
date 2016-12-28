package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.ReportParser;
import com.construction.android.view.GraphView;

@SuppressLint("InflateParams")
public class PieChartReportFragment extends Fragment implements OnClickListener {

	private MyFragmentActivity mActivity;
	private View rootView;

	private TextView mTextViewMaterial;
	private TextView mTextViewExCondition;
	private TextView mTextViewSpace;
	private TextView mTextViewDesign;
	private TextView mTextViewLabor;
	private TextView mTextViewEquipment;

	private ReportParser mReportParser;

	private GraphView mGraphView;

	private LinearLayout mLinearLayoutMainContent;
	
	public PieChartReportFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_pie_chart_reports, container, false);

		mActivity = (MyFragmentActivity) getActivity();
		mActivity.setTitle(R.string.lbl_report_pie_chart);

		getWidgetRefrence(rootView);
		registerOnClick();

		mReportParser = new ReportParser();
		mReportParser = getArguments().getParcelable(getString(R.string.bunble_report_data));
		
		if (mReportParser.getTotal().equalsIgnoreCase("0.0")) {

			mActivity.getCommonMethod().showDialog(getString(R.string.app_name), getString(R.string.alt_reports_data_not_founds), true);
		} else {

			mLinearLayoutMainContent.setVisibility(View.VISIBLE);
			float mValue[] = { Float.parseFloat(mReportParser.getMaterialConstraint()), Float.parseFloat(mReportParser.getExternalConstraint()),
					Float.parseFloat(mReportParser.getManpowerConstraint()), Float.parseFloat(mReportParser.getSpaceConstraint()),
					Float.parseFloat(mReportParser.getEquipmentConstraint()), Float.parseFloat(mReportParser.getDesignConstraint()) };

			mGraphView.setValues(mValue);
		}

		return rootView;
	}

	/**
	 * Method call will get IDs from xml file.
	 * 
	 * @param v
	 */
	private void getWidgetRefrence(View v) {
		mTextViewMaterial = (TextView) v.findViewById(R.id.fragment_pie_chart_report_textview_material);
		mTextViewExCondition = (TextView) v.findViewById(R.id.fragment_pie_chart_report_textview_external_condtion);
		mTextViewSpace = (TextView) v.findViewById(R.id.fragment_pie_chart_report_textview_space);
		mTextViewDesign = (TextView) v.findViewById(R.id.fragment_pie_chart_report_textview_desing);
		mTextViewLabor = (TextView) v.findViewById(R.id.fragment_pie_chart_report_textview_labor);
		mTextViewEquipment = (TextView) v.findViewById(R.id.fragment_pie_chart_report_textview_equipment);
		mGraphView = (GraphView) v.findViewById(R.id.fragment_make_ready_task_graphview);
		
		mLinearLayoutMainContent = (LinearLayout)v.findViewById(R.id.fragment_pie_chart_report_linear_main_view);

	}
	
	/**
	 * Method call will register OnClick() Events.
	 */
	private void registerOnClick() {
		mTextViewMaterial.setOnClickListener(this);
		mTextViewExCondition.setOnClickListener(this);
		mTextViewSpace.setOnClickListener(this);
		mTextViewDesign.setOnClickListener(this);
		mTextViewLabor.setOnClickListener(this);
		mTextViewEquipment.setOnClickListener(this);

	}

	/**
	 * Method call will fire onClick event;
	 */
	@Override
	public void onClick(View v) {

		if (v == mTextViewMaterial) {

			Toast.makeText(mActivity, "Material "+mReportParser.getMaterialConstraint(), Toast.LENGTH_SHORT).show();

		} else if (v == mTextViewExCondition) {

			Toast.makeText(mActivity, "External "+mReportParser.getExternalConstraint(), Toast.LENGTH_SHORT).show();

		} else if (v == mTextViewSpace) {

			Toast.makeText(mActivity, "Space "+mReportParser.getSpaceConstraint(), Toast.LENGTH_SHORT).show();

		} else if (v == mTextViewDesign) {

			Toast.makeText(mActivity, "Design "+mReportParser.getDesignConstraint(), Toast.LENGTH_SHORT).show();

		} else if (v == mTextViewLabor) {

			Toast.makeText(mActivity, "Manpower "+mReportParser.getManpowerConstraint(), Toast.LENGTH_SHORT).show();

		} else if (v == mTextViewEquipment) {

			Toast.makeText(mActivity, "Equipment "+mReportParser.getEquipmentConstraint(), Toast.LENGTH_SHORT).show();
		}

	}

}
