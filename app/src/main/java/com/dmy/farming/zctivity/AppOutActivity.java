package com.dmy.farming.zctivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;


import com.dmy.farming.R;

import org.bee.activity.BaseActivity;

public class AppOutActivity extends BaseActivity {

	private ImageView bg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appout);
		
		bg = (ImageView) findViewById(R.id.bg);
		
		Intent intent = getIntent();
		int flag = intent.getIntExtra("flag", 0);
		if(flag == 1) {
			bg.setBackgroundResource(R.drawable.closed);
		} else if(flag == 2) {
			bg.setBackgroundResource(R.drawable.expired_568h);
		}
		
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {                
        return true;
    }
}
