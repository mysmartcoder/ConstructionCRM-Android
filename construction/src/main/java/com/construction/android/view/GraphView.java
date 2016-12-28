package com.construction.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.construction.android.R;

public class GraphView extends View {
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private float[] value_degree;
	private int[] COLORS = { getResources().getColor(R.color.color_material), getResources().getColor(R.color.color_external_condition),
			getResources().getColor(R.color.color_laber), getResources().getColor(R.color.color_space),
			getResources().getColor(R.color.color_equipment), getResources().getColor(R.color.color_desigm) };
	RectF rectf = new RectF(10, 10, getResources().getDimension(R.dimen.dimen_300), getResources().getDimension(R.dimen.dimen_300));
	int temp = 0;

	float values[] = { 15, 15, 35, 5, 10, 20 };

	public GraphView(Context context) {
		super(context);
		setValues(values);
	}

	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setValues(values);
	}

	public void setValues(float[] values) {
		values = calculateData(values);

		value_degree = new float[values.length];
		for (int i = 0; i < values.length; i++) {
			value_degree[i] = values[i];
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		{
			temp = 0;
			for (int i = 0; i < value_degree.length; i++) {
				if (i == 0) {
					paint.setColor(COLORS[i]);
					canvas.drawArc(rectf, 0, 360, true, paint);
				} else {
					temp += (int) value_degree[i - 1];
					paint.setColor(COLORS[i]);
					canvas.drawArc(rectf, temp, value_degree[i], true, paint);
				}
			}
		}
	}

	private float[] calculateData(float[] data) {
		float total = 0;
		for (int i = 0; i < data.length; i++) {
			total += data[i];
		}
		for (int i = 0; i < data.length; i++) {
			data[i] = 360 * (data[i] / total);
		}
		return data;

	}

}