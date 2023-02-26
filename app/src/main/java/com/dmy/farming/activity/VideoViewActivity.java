package com.dmy.farming.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dmy.farming.R;

import org.bee.activity.BaseActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 展示视频内容
 *
 * @author Administrator
 *
 */
public class VideoViewActivity extends BaseActivity implements View.OnClickListener {
	private static final String TAG = "ShowVideoActivity";

	private RelativeLayout loadingLayout;
	private SeekBar seekBar;

	private String localFilePath;
	private String remotepath;
	private SimpleDateFormat dateFormat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.video_view_activity);

		localFilePath = getIntent().getStringExtra("localpath");
		remotepath = getIntent().getStringExtra("remotepath");

		Window window = getWindow();
		android.view.WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
		windowLayoutParams.alpha = 0.95f;

		/*
		if (!Utils.CheckNetworkState(FullVideoActivity.this)) {
			Utils.showDialog(FullVideoActivity.this, "", "手机无网络，请检查网络", "确定");
			return;
		}
		*/

		dateFormat = new SimpleDateFormat("mm:ss");

		init();
	}

	private SurfaceView surfaceView;
	private MediaPlayer player;
	private int playState = 0; // 0未播放，1播放中，2暂停，3停止
	// private MediaPlayer player;
	private SurfaceHolder surfaceHolder;
	private ImageView pause,exit;
	private LinearLayout replay; // 重播
	private TextView playtime;
	private TextView totletime;

	public void init() {
		surfaceView = (SurfaceView) findViewById(R.id.surface);
		replay = (LinearLayout) findViewById(R.id.replay);
		exit = (ImageView) findViewById(R.id.exit);
		pause = (ImageView) findViewById(R.id.pause);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		playtime = (TextView) findViewById(R.id.text_play_time);
		totletime = (TextView) findViewById(R.id.text_totle_time);
		surfaceView.setOnClickListener(this);
		replay.setOnClickListener(this);
		pause.setOnClickListener(this);
		exit.setOnClickListener(this);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				player.seekTo(seekBar.getProgress());

			}
		});
		surfaceHolder = surfaceView.getHolder(); // SurfaceHolder是SurfaceView的控制接口
//        surfaceHolder.setFixedSize(1080, 820); //显示的分辨率,不设置为视频默认
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // Surface类型

		//Utils.showProgressDialog(this, "正在加载..", true, 0);

		// 因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if (player != null) {
					player.release();
					player = null;
					mTimer.cancel();
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// if(listMediaPlayer.size()<=position){
				// 必须在surface创建后才能初始化MediaPlayer,否则不会显示图像
				player = new MediaPlayer();
				player.setAudioStreamType(AudioManager.STREAM_MUSIC);
				player.setLooping(false);
				player.setDisplay(holder);
				player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mediaPlayer) {
						replay.setVisibility(View.VISIBLE);
						seekBar.setProgress(0);
						player.seekTo(0);
						pause.setBackgroundResource(R.drawable.paly);
						playtime.setText(dateFormat.format(new Date(0)));
					}
				});

				startVideo();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
									   int width, int height) {

			}
		});
	}

	void startVideo() {
		try {
			Log.e(TAG, "show video view file:" + localFilePath + " remotepath:" + remotepath);// + " secret:" + secret);
			if (localFilePath != null && new File(localFilePath).exists()) {

				File file = new File(localFilePath);
				FileInputStream is;
				FileDescriptor fd = null;
				try {
					is = new FileInputStream(file);
					fd = is.getFD();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (file.exists() && fd != null) {
					player.setDataSource(fd);
				} else {
					player.setDataSource(remotepath);
				}

				player.prepareAsync();
				player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {
						replay.setVisibility(View.GONE);
						//Utils.removeProgressDialog();
						player.start();
						totletime.setText(dateFormat.format(new Date(player.getDuration())));
						seekBar.setMax(player.getDuration());
						setProgress();
					}
				});

			} else if (!TextUtils.isEmpty(remotepath) && !remotepath.equals("null")) {
				Log.e(TAG, "download remote video file");


				player.setDataSource(remotepath);
				player.prepareAsync();
				player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {
						replay.setVisibility(View.GONE);
						//Utils.removeProgressDialog();
						player.start();
					}
				});
			/*
			Map<String, String> maps = new HashMap<String, String>();
			if (!TextUtils.isEmpty(secret)) {
				maps.put("share-secret", secret);
			}
			*/
				//downloadVideo(remotepath);
			} else {

			}
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// 设置显示视频显示在SurfaceView上
		/*
		try {
			if (mb != null) {
				String localurl = mb.getLocalUrl();
				if (localurl == null) {
					localurl = "";
				}

			} else {
				player.setDataSource(videourl);
				player.prepareAsync();
				player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {
						replay.setVisibility(View.GONE);
						Utils.removeProgressDialog();
						player.start();
					}
				});
			}
		}
		*/
	}

	Timer mTimer;
	TimerTask mTimerTask;
	void setProgress(){
		mTimer = new Timer();
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (player != null) {
					Message os = new Message();
					os.what = UPDATE;
					handler.sendMessage(os);
				}
			}
		};

		//每隔1000毫秒检测一下播放进度
		mTimer.schedule(mTimerTask, 0, 1000);
	}

	private static final int UPDATE = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == UPDATE) {
				if (player != null) {
					playtime.setText(dateFormat.format(new Date(player.getCurrentPosition())));
					seekBar.setProgress(player.getCurrentPosition());
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.exit:
				mTimer.cancel();
				seekBar.setProgress(0);
				finish();
				break;
			case R.id.replay:
				replay.setVisibility(View.GONE);
				if (player != null) {
					player.start();
				}
				break;
			case R.id.pause:
				if (player != null) {
					if (player.isPlaying()) {
						player.pause();
						pause.setBackgroundResource(R.drawable.paly);
					} else {
						player.start();
						pause.setBackgroundResource(R.drawable.pause);
					}
				}
				break;
			default:
				break;
		}
	}

//	public String getLocalFilePath(String remoteUrl){
//		String localPath;
//		if (remoteUrl.contains("/")) {
//			localPath = PathUtil.getInstance().getVideoPath().getAbsolutePath()
//					+ "/" + remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1)
//					+ ".mp4";
//		} else {
//			localPath = PathUtil.getInstance().getVideoPath().getAbsolutePath()
//					+ "/" + remoteUrl + ".mp4";
//		}
//		return localPath;
//	}
//
//	/**
//	 * 播放本地视频
//	 * @param localPath 视频路径
//	 */
//	private void showLocalVideo(String localPath){
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.fromFile(new File(localPath)),
//				"video/mp4");
//		startActivity(intent);
//		finish();
//	}
//
//
//	/**
//	 * 下载视频文件
//	 */
//	private void downloadVideo(final String remoteUrl) {
//
//		if (TextUtils.isEmpty(localFilePath)) {
//			localFilePath = getLocalFilePath(remoteUrl);
//		}
//		if (new File(localFilePath).exists()) {
//			showLocalVideo(localFilePath);
//			return;
//		}
//		loadingLayout.setVisibility(View.VISIBLE);
//
//		EMCallBack callback = new EMCallBack() {
//
//			@Override
//			public void onSuccess() {
//				runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						loadingLayout.setVisibility(View.GONE);
//						progressBar.setProgress(0);
//						showLocalVideo(localFilePath);
//					}
//				});
//			}
//
//			@Override
//			public void onProgress(final int progress,String status) {
//				Log.d("ease", "video progress:" + progress);
//				runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						progressBar.setProgress(progress);
//					}
//				});
//
//			}
//
//			@Override
//			public void onError(int error, String msg) {
//				Log.e("###", "offline file transfer error:" + msg);
//				File file = new File(localFilePath);
//				if (file.exists()) {
//					file.delete();
//				}
//			}
//		};
//
//		ChatMng.getInstance().downloadFile(remoteUrl, localFilePath, callback);
//	}
//
//	@Override
//	public void onBackPressed() {
//		finish();
//	}
//
//


}
