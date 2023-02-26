package com.dmy.farming.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.data.CITY;
import com.dmy.farming.api.data.GLOBAL_DATA;
import com.dmy.farming.api.data.RECENTCITY;
import com.droid.MyLetterListView;
import com.external.activeandroid.query.Select;

import org.bee.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class C00_CityActivity extends BaseActivity implements OnScrollListener {
	private BaseAdapter adapter;
	private ResultListAdapter resultListAdapter;
	private ListView personList;
	private ListView resultList;
	private TextView overlay; // 对话框首字母textview
	private MyLetterListView letterListView; // A-Z listview
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private OverlayThread overlayThread; // 显示首字母对话框
	private ArrayList<CITY> allCity_lists; // 所有城市列表
	private ArrayList<CITY> city_lists;// 城市列表
	private ArrayList<CITY> city_hot;
	private ArrayList<CITY> city_result;
	private ArrayList<String> city_history;
	private TextView tv_noresult;

	private LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;

	private String currentCity; // 用于保存定位到的城市
	private int locateProcess = 1; // 记录当前定位的状态 正在定位-定位成功-定位失败
	private boolean isNeedFresh;

	EditText edit_search;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c00_city);

		View button_right = findViewById(R.id.button_right);
		button_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		personList = (ListView) findViewById(R.id.list_view);
		allCity_lists = new ArrayList<CITY>();
		city_hot = new ArrayList<CITY>();
		city_result = new ArrayList<CITY>();
		city_history = new ArrayList<String>();
		resultList = (ListView) findViewById(R.id.search_result);
		edit_search = (EditText) findViewById(R.id.edit_search);
		tv_noresult = (TextView) findViewById(R.id.tv_noresult);
		edit_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.toString() == null || "".equals(s.toString())) {
					letterListView.setVisibility(View.VISIBLE);
					personList.setVisibility(View.VISIBLE);
					resultList.setVisibility(View.GONE);
					tv_noresult.setVisibility(View.GONE);
				} else {
					city_result.clear();
					letterListView.setVisibility(View.GONE);
					personList.setVisibility(View.GONE);
					getResultCityList(s.toString());
					if (city_result.size() <= 0) {
						tv_noresult.setVisibility(View.VISIBLE);
						resultList.setVisibility(View.GONE);
					} else {
						tv_noresult.setVisibility(View.GONE);
						resultList.setVisibility(View.VISIBLE);
						resultListAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		letterListView = (MyLetterListView) findViewById(R.id.llist_letter);
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		isNeedFresh = true;
		personList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if (position >= 3) {
					closeSuccess(allCity_lists.get(position).name);
				}
			}
		});
		locateProcess = 1;
		personList.setAdapter(adapter);
		personList.setOnScrollListener(this);
		resultListAdapter = new ResultListAdapter(this, city_result);
		resultList.setAdapter(resultListAdapter);
		resultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				closeSuccess(city_result.get(position).name);
			}
		});
//		initOverlay();
		cityInit();
		hotCityInit();
		hisCityInit();
		setAdapter(allCity_lists, city_hot, city_history);

		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		InitLocation();
		mLocationClient.start();
	}



	private void InitLocation() {

		//mGeofenceClient = new GeofenceClient(this);
		LocationClientOption option = new LocationClientOption();
		option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09||");
		option.setScanSpan(10000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
//		// 设置定位参数
//		LocationClientOption option = new LocationClientOption();
//		option.setCoorType("bd09ll"); // 设置坐标类型
//		option.setScanSpan(10000); // 10分钟扫描1次
//		// 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。
//		option.setAddrType("all");
//		// 设置是否返回POI的电话和地址等详细信息。默认值为false，即不返回POI的电话和地址信息。
////		option.setPoiExtraInfo(true);
//		// 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
//		option.setProdName("通过GPS定位我当前的位置");
//		// 禁用启用缓存定位数据
//		option.disableCache(true);
//		// 设置最多可返回的POI个数，默认值为3。由于POI查询比较耗费流量，设置最多返回的POI个数，以便节省流量。
////		option.setPoiNumber(3);
//		// 设置定位方式的优先级。
//		// 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
//		option.setPriority(LocationClientOption.GpsFirst);
//		mLocationClient.setLocOption(option);
	}

	private void cityInit() {
		CITY city = new CITY().init("定位", "0"); // 当前定位城市
		allCity_lists.add(city);
		city = new CITY().init("最近", "1"); // 最近访问的城市
		allCity_lists.add(city);
		city = new CITY().init("热门", "2"); // 热门城市
		allCity_lists.add(city);
		//city = new CITY().init("全部", "3"); // 全部城市
		//allCity_lists.add(city);
		city_lists = getCityList();
		allCity_lists.addAll(city_lists);
	}

	/**
	 * 热门城市
	 */
	public void hotCityInit() {
		CITY city = new CITY().init("北京", "2");
		city_hot.add(city);
		city = new CITY().init("上海", "2");
		city_hot.add(city);
		city = new CITY().init("广州", "2");
		city_hot.add(city);
		city = new CITY().init("深圳", "2");
		city_hot.add(city);
		city = new CITY().init("杭州", "2");
		city_hot.add(city);
		city = new CITY().init("南京", "2");
		city_hot.add(city);
		city = new CITY().init("天津", "2");
		city_hot.add(city);
		city = new CITY().init("武汉", "2");
		city_hot.add(city);
		city = new CITY().init("重庆", "2");
		city_hot.add(city);
	}

	public void InsertCity(String name) {
//		SQLiteDatabase db = helper.getReadableDatabase();
//		Cursor cursor = db.rawQuery("select * from recentcity where name = '"
//				+ name + "'", null);
//		if (cursor.getCount() > 0) { //
//			db.delete("recentcity", "name = ?", new String[] { name });
//		}
//		db.execSQL("insert into recentcity(name, date) values('" + name + "', "
//				+ System.currentTimeMillis() + ")");
//		db.close();
		RECENTCITY recent = new RECENTCITY().init(name, System.currentTimeMillis());
		recent.save();
	}

	private void hisCityInit() {
//		SQLiteDatabase db = helper.getReadableDatabase();
//		Cursor cursor = db.rawQuery(
//				"select * from recentcity order by date desc limit 0, 3", null);
//		while (cursor.moveToNext()) {
//			city_history.add(cursor.getString(1));
//		}
//		cursor.close();
//		db.close();
		List<RECENTCITY> historyes = new Select().from(RECENTCITY.class).orderBy("date desc").limit("0, 3").execute();
		if (historyes != null)
		{
			for (RECENTCITY history : historyes)
			{
				city_history.add(history.name);
			}
		}
	}

	private ArrayList<CITY> getCityList() {
		ArrayList<CITY> list = new ArrayList<CITY>();

//		DBHelper dbHelper = new DBHelper(this);
//		try {
//			dbHelper.createDataBase();
//			SQLiteDatabase db = dbHelper.getWritableDatabase();
//			Cursor cursor = db.rawQuery("select * from city", null);
//			City city;
//			while (cursor.moveToNext()) {
//				city = new City(cursor.getString(1), cursor.getString(2));
//				list.add(city);
//			}
//			cursor.close();
//			db.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		List<CITY> cities = new Select().from(CITY.class).execute();
		if (cities != null)
			list.addAll(cities);

		Collections.sort(list, comparator);
		return list;
	}

	private void getResultCityList(String keyword) {
//		DBHelper dbHelper = new DBHelper(this);
//		try {
//			dbHelper.createDataBase();
//			SQLiteDatabase db = dbHelper.getWritableDatabase();
//			Cursor cursor = db.rawQuery(
//					"select * from city where name like \"%" + keyword
//							+ "%\" or pinyin like \"%" + keyword + "%\"", null);
//			City city;
//			Log.e("info", "length = " + cursor.getCount());
//			while (cursor.moveToNext()) {
//				city = new City(cursor.getString(1), cursor.getString(2));
//				city_result.add(city);
//			}
//			cursor.close();
//			db.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		List<CITY> cities = new Select().from(CITY.class).where("name like \"%" + keyword
				+ "%\" or pinyin like \"%" + keyword + "%\"").execute();
		if (cities != null)
			city_result.addAll(cities);

		Collections.sort(city_result, comparator);
	}

	/**
	 * a-z排序
	 */
	@SuppressWarnings("rawtypes")
	Comparator comparator = new Comparator<CITY>() {
		@Override
		public int compare(CITY lhs, CITY rhs) {
			String a = lhs.pinyin.substring(0, 1);
			String b = rhs.pinyin.substring(0, 1);
			int flag = a.compareTo(b);
			if (flag == 0) {
				return a.compareTo(b);
			} else {
				return flag;
			}
		}
	};

	private void setAdapter(List<CITY> list, List<CITY> hotList,
							List<String> hisCity) {
		adapter = new ListAdapter(this, list, hotList, hisCity);
		personList.setAdapter(adapter);
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
//			//Receive Location
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("time : ");
//			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
//			if (location.getLocType() == BDLocation.TypeGpsLocation){
//				sb.append("\nspeed : ");
//				sb.append(location.getSpeed());
//				sb.append("\nsatellite : ");
//				sb.append(location.getSatelliteNumber());
//				sb.append("\ndirection : ");
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//				sb.append(location.getDirection());
//			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//				//运营商信息
//				sb.append("\noperationers : ");
//				sb.append(location.getOperators());
//
//			}
//			//logMsg(sb.toString());
//			Log.i("BaiduLocationApiDem", sb.toString());
//
//			if (location.getLatitude() > 10)
//				GLOBAL_DATA.getInstance(getApplicationContext()).currLat = location.getLatitude();
//			if (location.getLongitude() > 10)
//				GLOBAL_DATA.getInstance(getApplicationContext()).currLon = location.getLongitude();
//			if (location.getDistrict() != null && !"".equals(location.getDistrict()))
//			{
//				GLOBAL_DATA.getInstance(getApplicationContext()).currAddr = location.getDistrict();
//				GLOBAL_DATA.getInstance(getApplicationContext()).currFullAddr = location.getCountry()+ location.getAddrStr();
//				GLOBAL_DATA.getInstance(getApplicationContext()).saveData(getApplicationContext());
//
//
//				option.setScanSpan(1000 * 60 * 10);
//				mLocationClient.setLocOption(option);
//
//				//if (drawerView != null)
//				//{
//				//	drawerView.updateCurrentCity();
//				//}
//			}
//		}


			Log.e("info", "city = " + location.getCity());
			if (!isNeedFresh) {
				return;
			}
			isNeedFresh = false;
			if (location.getCity() == null) {
				locateProcess = 3; // 定位失败
				personList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				return;
			}
			currentCity = location.getCity().substring(0,
					location.getCity().length() - 1);

			GLOBAL_DATA.getInstance(C00_CityActivity.this).currCity = currentCity;

			locateProcess = 2; // 定位成功
			personList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	private class ResultListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private ArrayList<CITY> results = new ArrayList<CITY>();

		public ResultListAdapter(Context context, ArrayList<CITY> results) {
			inflater = LayoutInflater.from(context);
			this.results = results;
		}

		@Override
		public int getCount() {
			return results.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.c00_city_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.name.setText(results.get(position).name);
			return convertView;
		}

		class ViewHolder {
			TextView name;
		}
	}

	void closeSuccess(String city)
	{
		InsertCity(city);
		Intent intent = new Intent();
		intent.putExtra("city", city);
		setResult(Activity.RESULT_OK, intent);

		AppUtils.setCurrCity(this, city);
		finish();
	}

	public class ListAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<CITY> list;
		private List<CITY> hotList;
		private List<String> hisCity;
		final int VIEW_TYPE = 5;

		public ListAdapter(Context context, List<CITY> list,
						   List<CITY> hotList, List<String> hisCity) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			this.context = context;
			this.hotList = hotList;
			this.hisCity = hisCity;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentStr = getAlpha(list.get(i).pinyin);
				// 上一个汉语拼音首字母，如果不存在为" "
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
						.pinyin) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).pinyin);
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
		}

		@Override
		public int getViewTypeCount() {
			return VIEW_TYPE;
		}

		@Override
		public int getItemViewType(int position) {
			return position < 3 ? position : 4;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		ViewHolder holder;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final TextView text_city;
			final View layout_city;
			int viewType = getItemViewType(position);
			if (viewType == 0) { // 定位
				convertView = inflater.inflate(R.layout.c00_city_frist_list_item, null);
				TextView text_state = (TextView) convertView
						.findViewById(R.id.text_state);
				layout_city = convertView.findViewById(R.id.layout_city);
				text_city = (TextView) convertView.findViewById(R.id.text_city);
				layout_city.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (locateProcess == 2) {
							closeSuccess(text_city.getText().toString());
						} else if (locateProcess == 3) {
							locateProcess = 1;
							personList.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							mLocationClient.stop();
							isNeedFresh = true;
							InitLocation();
							currentCity = "";
							mLocationClient.start();
						}
					}
				});
				ProgressBar pbLocate = (ProgressBar) convertView
						.findViewById(R.id.pbLocate);
				if (locateProcess == 1) { // 正在定位
					text_state.setText("正在定位");
					layout_city.setVisibility(View.GONE);
					pbLocate.setVisibility(View.VISIBLE);
				} else if (locateProcess == 2) { // 定位成功
					text_state.setText("定位城市");
					layout_city.setVisibility(View.VISIBLE);
					text_city.setText(currentCity);
					mLocationClient.stop();
					pbLocate.setVisibility(View.GONE);
				} else if (locateProcess == 3) {
					text_state.setText("未定位到城市,请选择");
					layout_city.setVisibility(View.VISIBLE);
					text_city.setText("重新选择");
					pbLocate.setVisibility(View.GONE);
				}
			} else if (viewType == 1) { // 最近访问城市
				convertView = inflater.inflate(R.layout.c00_city_recent_city, null);
				GridView rencentCity = (GridView) convertView.findViewById(R.id.recent_city);
				rencentCity.setAdapter(new HitCityAdapter(context, this.hisCity));
				rencentCity.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						closeSuccess(city_history.get(position));
					}
				});
				TextView recentHint = (TextView) convertView
						.findViewById(R.id.recentHint);
				recentHint.setText("常用城市");
			} else if (viewType == 2) {
				convertView = inflater.inflate(R.layout.c00_city_recent_city, null);
				GridView hotCity = (GridView) convertView
						.findViewById(R.id.recent_city);
				hotCity.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						closeSuccess(city_hot.get(position).name);
					}
				});
				hotCity.setAdapter(new HotCityAdapter(context, this.hotList));
				TextView hotHint = (TextView) convertView
						.findViewById(R.id.recentHint);
				hotHint.setText("热门城市");
			} else if (viewType == 3) {
				convertView = inflater.inflate(R.layout.c00_city_total_item, null);
			} else {
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.c00_city_list_item, null);
					holder = new ViewHolder();
					holder.alpha = (TextView) convertView
							.findViewById(R.id.alpha);
					holder.name = (TextView) convertView
							.findViewById(R.id.name);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				if (position >= 1) {
					holder.name.setText(list.get(position).name);
					String currentStr = getAlpha(list.get(position).pinyin);
					String previewStr = (position - 1) >= 0 ? getAlpha(list
							.get(position - 1).pinyin) : " ";
					if (!previewStr.equals(currentStr)) {
						holder.alpha.setVisibility(View.VISIBLE);
						holder.alpha.setText(currentStr);
					} else {
						holder.alpha.setVisibility(View.GONE);
					}
				}
			}
			return convertView;
		}

		private class ViewHolder {
			TextView alpha; // 首字母标题
			TextView name; // 城市名字
		}
	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}

	class HotCityAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<CITY> hotCitys;

		public HotCityAdapter(Context context, List<CITY> hotCitys) {
			this.context = context;
			inflater = LayoutInflater.from(this.context);
			this.hotCitys = hotCitys;
		}

		@Override
		public int getCount() {
			return hotCitys.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.c00_city_item_city, null);
			TextView city = (TextView) convertView.findViewById(R.id.city);
			city.setText(hotCitys.get(position).name);
			return convertView;
		}
	}

	class HitCityAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<String> hotCitys;

		public HitCityAdapter(Context context, List<String> hotCitys) {
			this.context = context;
			inflater = LayoutInflater.from(this.context);
			this.hotCitys = hotCitys;
		}

		@Override
		public int getCount() {
			return hotCitys.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.c00_city_item_city, null);
			TextView city = (TextView) convertView.findViewById(R.id.city);
			city.setText(hotCitys.get(position));
			return convertView;
		}
	}

	private boolean mReady;

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		mReady = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.c00_city_overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private boolean isScroll = false;

	private class LetterListViewListener implements
			MyLetterListView.OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			isScroll = false;
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				personList.setSelection(position);
//				overlay.setText(s);
//				overlay.setVisibility(View.VISIBLE);
//				handler.removeCallbacks(overlayThread);
//				// 延迟一秒后执行，让overlay为不可见
//				handler.postDelayed(overlayThread, 1000);
			}
		}
	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}

	// 获得汉语拼音首字母
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else if (str.equals("0")) {
			return "定位";
		} else if (str.equals("1")) {
			return "最近";
		} else if (str.equals("2")) {
			return "热门";
//		} else if (str.equals("3")) {
//			return "全部";
		} else {
			return "#";
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_TOUCH_SCROLL
				|| scrollState == SCROLL_STATE_FLING) {
			isScroll = true;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		if (!isScroll) {
			return;
		}

		/*
		if (mReady) {
			String text;
			String name = allCity_lists.get(firstVisibleItem).name;
			String pinyin = allCity_lists.get(firstVisibleItem).pinyin;
			if (firstVisibleItem < 4) {
				text = name;
			} else {
				text = PingYinUtil.converterToFirstSpell(pinyin)
						.substring(0, 1).toUpperCase();
			}
			overlay.setText(text);
			overlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(overlayThread);
			// 延迟一秒后执行，让overlay为不可见
			handler.postDelayed(overlayThread, 1000);
		}
		*/
	}
}