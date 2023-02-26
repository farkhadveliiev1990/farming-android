package org.bee.view;

import android.content.DialogInterface;
import android.view.KeyEvent;
import com.dmy.farming.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MyDialog {

	private Dialog mDialog;
	public TextView positive;
	public TextView negative;

	public MyDialog(Context context, String title, String message) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_layout, null);

		mDialog = new Dialog(context, R.style.dialog_full);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(keyCode==KeyEvent.KEYCODE_BACK){
					return true;
				}
				return false;
			}
		});

		TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
		dialog_title.setVisibility(View.GONE);

		TextView dialog_message = (TextView) view.findViewById(R.id.dialog_message);
		dialog_message.setText(message);

		positive = (TextView) view.findViewById(R.id.positive);
		negative = (TextView) view.findViewById(R.id.negative);
	}

	public void show() {
		mDialog.show();
	}

	public void dismiss() {
		mDialog.dismiss();
	}

}
