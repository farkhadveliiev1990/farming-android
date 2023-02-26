package com.dmy.farming.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.AppConst;
import com.dmy.farming.api.data.GLOBAL_DATA;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.WEATHER;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.fragment.B00_HomeFragment;
import com.dmy.farming.fragment.TabsFragment;
import com.dmy.farming.service.LocationService;
import com.dmy.farming.utils.PushMsgProcHandler;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.BeeFrameworkApp;
import org.bee.activity.BaseActivity;
import org.bee.model.ActivityManagerModel;
import org.bee.model.BeeQuery;
import org.bee.model.BusinessResponse;
import org.bee.service.NetworkStateService;
import org.bee.view.MyDialog;
import org.bee.view.ToastView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements BusinessResponse {
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";

    public static final String ACTION_LOGIN = "bccsclient.action.LOGIN";
    public static final String ACTION_PUSHCLICK = "bccsclient.action.PUSHCLICK";
    public static final String ACTION_CHATPUSH = "bccsclient.action.PUSHCHAT";
    public static final String ACTION_CONFLICT = "bccsclient.action.CONFLICT";

    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String CUSTOM_CONTENT ="CustomContent";

    // 在百度开发者中心查询应用的API Key
    public static String API_KEY ;

    public TabsFragment tabs_fragment;
    CommonModel commonModel;
    public WEATHER localWEATHER;

    private static MainActivity mainActivity = null;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tabs_fragment = (TabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);

        Intent intent = new Intent(this, NetworkStateService.class);
//        intent.setAction("org.bee.service.NetworkStateService");
        startService(intent);

        getPersimmions();

        mainActivity = this;
        handleIntent(getIntent());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 如果要统计Push引起的用户使用应用情况，请实现本方法，且加上这一个语句
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onDestroy() {
        mainActivity = null;
        super.onDestroy();
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();

        if (ACTION_CHATPUSH.equals(action)) {
            runOnUiThread(new Runnable() {
                public void run() {
                    int tab_index = 2;
                    tabs_fragment.tabSelected(tab_index);
                }
            });
        }
        else if (ACTION_CONFLICT.equals(action)) {
            showConflictDialog();
        }
        else if (ACTION_PUSHCLICK.equals(action)) {
            String message = intent.getStringExtra(CUSTOM_CONTENT);
            PushMsgProcHandler.pushIntent(this, message);
        }
        else if (ACTION_LOGIN.equals(action))
        {
            //
        }
    }

    /**
     * 显示帐号在别处登录dialog
     */
    boolean isConflictDialogShow;
    MyDialog myDialog;
    public boolean isConflict = false;

    private void showConflictDialog() {
        isConflictDialogShow = true;
        SESSION.getInstance().updateValue(this, "", "", "", "",-1,"");
        if (!isFinishing()) {
            // clear up global variables
            try {
                if (myDialog != null) myDialog.dismiss();
                myDialog = new MyDialog(this, "", "同一账号已在其他设备登录");
                myDialog.negative.setVisibility(View.GONE);
                myDialog.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                        myDialog = null;

                        Intent responseIntent;
                        responseIntent = new Intent(MainActivity.ACTION_LOGIN);
                        responseIntent.setClass(MainActivity.this, MainActivity.class);
                        responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(responseIntent);
                    }
                });
                myDialog.show();
                isConflict = true;
            } catch (Exception e) {
                //EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    public static void showStatusBar(boolean isShow)
    {
        if (mainActivity != null)
            mainActivity.refreshStatusBar(isShow);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String username = SESSION.getInstance().nick;
        String phone = SESSION.getInstance().sid;
        String password = SESSION.getInstance().password;

        updateDeviceInfo();
        refreshUnreadCnt();
    }

    void updateDeviceInfo() {
        if (!AppUtils.getUploadDev(this) && AppUtils.isLogin(this) && !TextUtils.isEmpty(AppUtils.getChannelId(this))) {
            if (commonModel == null) {
                commonModel = new CommonModel(this);
                commonModel.addResponseListener(this);
            }
//            commonModel.updateChaanel();
        }
    }

    private boolean isExit = false;
    //退出操作
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isExit==false){
                isExit=true;
                Resources resource = (Resources) getBaseContext().getResources();
                String exit=resource.getString(R.string.again_exit);
                ToastView toast = new ToastView(getApplicationContext(), exit);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                handler.sendEmptyMessageDelayed(MSG_EXIT, 3000);
                if(BeeQuery.environment() == BeeQuery.ENVIROMENT_DEVELOPMENT)
                {
                    BeeFrameworkApp.getInstance().showBug(this);
                }

                return true;
            } else {
                Intent intent = new Intent();
                intent.setAction("org.bee.service.NetworkStateService");
                stopService(intent);

                try{
                    //XMPPService.getInstance().stopSelf();
                }catch(Exception e){

                }

                finish();
                ToastView.cancel();
                return false;
            }
        }
        return false;
    }

    private final static int MSG_EXIT = 1;
    private final static int MSG_REFRESH = 2;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_EXIT) {
                isExit = false;
            } else {
                tabs_fragment.refreshUnreadCnt();
            }
        }
    };

    private LocationService locationService;

    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务

        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        {
            initLocation();

            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
                    AppConst.AppKey);

            Resources resource = this.getResources();
            String pkgName = this.getPackageName();

            // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
            // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：a1，
            // 与下方代码中 PushManager.setNotificationBuilder(this, a1, cBuilder)中的第二个参数对应
            CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                    resource.getIdentifier(
                            "notification_custom_builder", "layout", pkgName),
                    resource.getIdentifier("notification_icon", "id", pkgName),
                    resource.getIdentifier("notification_title", "id", pkgName),
                    resource.getIdentifier("notification_text", "id", pkgName));
            cBuilder.setNotificationFlags(Notification.FLAG_ONLY_ALERT_ONCE);
            cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
            cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
            cBuilder.setLayoutDrawable(resource.getIdentifier(
                    "simple_notification_icon", "drawable", pkgName));
            cBuilder.setNotificationSound(Uri.withAppendedPath(
                    MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
            // 推送高级设置，通知栏样式设置为下面的ID
            PushManager.setNotificationBuilder(this, 1, cBuilder);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if(EcmobileManager.getUmengKey(this)!=null){
//            MobclickAgent.onPause(this);
//        }
    }

    ////////////////////// BAIDU /////////////////////

    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    LocationClientOption mOption;
    void initLocation() {

        // -----------location config ------------
        locationService = ((FarmingApp) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);

        mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        //mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        //mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        //mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        //mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        //mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        //mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        //mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用

        //注册监听
        locationService.setLocationOption(mOption);//locationService.getDefaultLocationClientOption());
        locationService.start();
    }
    /*****
     * @see
     * //定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }

                if (location.getLatitude() > 10 && location.getLongitude() > 10)
                {
                    GLOBAL_DATA gData = GLOBAL_DATA.getInstance(getApplicationContext());
                    gData.currLat = location.getLatitude(); // 41.734111
                    gData.currLon = location.getLongitude(); // 123.401398

                    if (!TextUtils.isEmpty(location.getProvince()))
                        gData.currProvince = location.getProvince().replace("省", ""); //辽宁
                    if (!TextUtils.isEmpty(location.getCity()))
                        gData.currCity = location.getCity().replace("市", ""); // 沈阳
                    if (!TextUtils.isEmpty(location.getDistrict()))
                        gData.currDistrict = location.getDistrict().replace("区", ""); // 东陵
                    if (!TextUtils.isEmpty(location.getStreet()))
                        gData.currStreet = location.getStreet(); // 长白南路
                    if (!TextUtils.isEmpty(location.getAddrStr()))
                        gData.currFullAddr = location.getAddrStr(); //中国辽宁省沈阳市东陵区长白南路

                    //Log.e("map", gData.currFullAddr);

                    if (!TextUtils.isEmpty(gData.currCity) && TextUtils.isEmpty(AppUtils.getCurrCity(MainActivity.this))) {
                        AppUtils.setCurrCity(MainActivity.this, gData.currCity);

                        android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentByTag("tab_one");
                        if (f != null && f instanceof B00_HomeFragment) {
                         //   ((B00_HomeFragment)f).setChangeCity(false);
                        }
                    }

                    gData.saveData(getApplicationContext());

                    if (!TextUtils.isEmpty(location.getStreet())){
                        mOption.setScanSpan(1000 * 60 * 10);
                        locationService.setLocationOption(mOption);
                    }
                }
            }
        }
    };


    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
//        if (url.endsWith(ApiInterface.USER_UPDATE_CHANNEL)) {
//            //
//        }
    }


    /**
     * 刷新页面
     */
    public void refreshUnreadCnt() {
        if(!handler.hasMessages(MSG_REFRESH)){
            handler.sendEmptyMessageDelayed(MSG_REFRESH, 300);
        } else {
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
