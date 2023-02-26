package org.bee;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.external.activeandroid.app.Application;
import org.bee.Utils.CustomExceptionHandler;
import org.bee.activity.DebugCancelDialogActivity;
import org.bee.activity.DebugTabActivity;
import com.dmy.farming.R;
import com.dmy.farming.api.data.SESSION;

public class BeeFrameworkApp extends Application implements OnClickListener{
    private static BeeFrameworkApp instance;
    private ImageView bugImage;
    public Context currContext;

    private WindowManager wManager ;
    private boolean flag = true;
    
    public Handler messageHandler;

    public static BeeFrameworkApp getInstance()
    {
        if (instance == null) {
            instance = new BeeFrameworkApp();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
//        FrontiaApplication.initFrontiaApplication(this);

        final Context mContext = this;
        new Runnable() {

            @Override
            public void run() {
                initConfig();
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + BeeFrameworkConst.LOG_DIR_PATH;
                File storePath = new File(path);
                storePath.mkdirs();
                Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                        path, null));
            }
        }.run();
    }
    
    void initConfig() {
        SharedPreferences shared;
        shared =  this.getSharedPreferences("userInfo", 0);
        SESSION.getInstance().updateValue(this, shared.getString("uid", ""), shared.getString("sid", "")
                ,shared.getString("logo", ""), shared.getString("nick", ""), shared.getInt("usertype",-1),shared.getString("password",""));
    }
	
    public void showBug(final Context context)
    {
        BeeFrameworkApp.getInstance().currContext = context;
        wManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL |
                LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        wmParams.x = 0;
        wmParams.y = 0;

        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        if(bugImage != null) { //判断bugImage是否存在，如果存在则移除，必须加在 new ImageView(context) 前面
        	wManager.removeView(bugImage);
        }
        
        bugImage = new ImageView(context);
        bugImage.setImageResource(R.drawable.bug);
        
        wManager.addView(bugImage, wmParams);
        bugImage.setOnClickListener(this);
        
        bugImage.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				 
				DebugCancelDialogActivity.parentHandler = messageHandler;
				Intent intent = new Intent(BeeFrameworkApp.getInstance().currContext,DebugCancelDialogActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				flag = false;
				return false;
			}
		});
        
        messageHandler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                	wManager.removeView(bugImage);
                	bugImage = null; // 必须要把bugImage清空，否则再次进入debug模式会与102行冲突
                }
            }
        };
    }

    public void onClick(View v) {
    	if(flag != false) {
        Intent intent = new Intent(BeeFrameworkApp.getInstance().currContext,DebugTabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    	}
        flag = true;
    }

}
