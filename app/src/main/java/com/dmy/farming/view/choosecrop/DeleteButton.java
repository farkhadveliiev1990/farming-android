package com.dmy.farming.view.choosecrop;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.activity.ChooseCropActivity;

/**
 * Created by lhz on 2016/3/30.
 */
public class DeleteButton extends FrameLayout {

	private long index;
	private TextView mTextViewContent;//the textview show content
	private TextView mTextViewDelete;//the textview as delete button
	private OnDeleteButtonClickListener mListener;
	ChooseCropActivity chooseCropActivity;
	private final int padding = 10;

	public DeleteButton(Context context) {
		super(context);
		inti(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public DeleteButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		inti(context);
	}

	public DeleteButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		inti(context);
	}

	public DeleteButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		inti(context);
	}

	protected void inti(Context context) {
		index=System.currentTimeMillis();
//		setOrientation(HORIZONTAL);
//		addTextViewContent(context);
//		addTextViewDelete(context);

		View view = LayoutInflater.from(context).inflate(R.layout.chose_crop_button, null);
		mTextViewContent = (TextView) view.findViewById(R.id.content);
		addTextViewContent(context);

		mTextViewDelete = (TextView) view.findViewById(R.id.delete);
		addTextViewDelete(context);
		addView(view);
	}

	private void addTextViewContent(Context context) {
//		mTextViewContent = new TextView(context);
//		mTextViewContent.setBackgroundColor(getResources().getColor(R.color.button_green));
//		mTextViewContent.setTextColor(Color.WHITE);
//		mTextViewContent.setPadding(padding, padding, padding, padding);
//		mTextViewContent.setText("Button");
		mTextViewContent.setClickable(true);
		mTextViewContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onContentClick(getText());
			}
		});
//		addView(mTextViewContent);
	}

	private void addTextViewDelete(Context context) {
//		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//		params.rightMargin = 6;
//		params.leftMargin = 1;
//		mTextViewDelete = new TextView(context);
//		mTextViewDelete.setBackgroundColor(Color.GRAY);
//		mTextViewDelete.setTextColor(Color.WHITE);
//		mTextViewDelete.setText("??");
//		mTextViewDelete.setPadding(16,-padding , 16, padding);
		mTextViewDelete.setClickable(true);
		mTextViewDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onDelecteClick(index);
			}
		});
//		addView(mTextViewDelete, params);

	}

	public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
		mListener = listener;
	}



	public long getIndex(){
		return  index;
	}

	public void setText(String text) {
		mTextViewContent.setText(text);
	}

	public String getText(){
		return mTextViewContent.getText().toString();
	}

	public void setTextSize(float size){
		mTextViewContent.setTextSize(size);
//		mTextViewDelete.setTextSize(size);
	}
	public void setTextColor(int color){
		mTextViewContent.setTextColor(color);
		mTextViewDelete.setTextColor(color);
	}
	public void setBgColor(int color){
		mTextViewContent.setBackgroundColor(color);
		mTextViewDelete.setBackgroundColor(color);
	}

	public interface OnDeleteButtonClickListener {
		void onDelecteClick(long index);

		void onContentClick(String text);
	}

}
