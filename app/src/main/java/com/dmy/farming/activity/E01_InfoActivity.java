package com.dmy.farming.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.api.model.UserInfoModel;
import com.dmy.farming.api.userUpdateRequest;
/*import com.dmy.farming.https.AsyncHttpUtil;*/
import com.dmy.farming.utils.FileUtils;
import com.external.activeandroid.query.Select;
import com.external.androidquery.callback.AjaxStatus;
/*import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;*/
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.bee.view.MyDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class E01_InfoActivity extends BaseActivity implements BusinessResponse, View.OnClickListener {

    UserInfoModel userInfoModel;
    CommonModel commonModel;

    userUpdateRequest request;
    USER userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e01_info);

        initView();

        userInfoModel = new UserInfoModel(this);
        userInfoModel.addResponseListener(this);
        commonModel = new CommonModel(this);
        commonModel.addResponseListener(this);

        request = new userUpdateRequest();
        updateData();
    }

    ImageView img_head;
    View button_send;

    TextView text_value_1, text_value_2;

    void initView()
    {
        View img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        button_send = findViewById(R.id.button_send);
        button_send.setOnClickListener(this);
        button_send.setEnabled(false);

        View layout_menu_0 = findViewById(R.id.layout_menu_0);
        layout_menu_0.setOnClickListener(this);
        View layout_menu_1 = findViewById(R.id.layout_menu_1);
        layout_menu_1.setOnClickListener(this);
        View layout_menu_2 = findViewById(R.id.layout_menu_2);
        layout_menu_2.setOnClickListener(this);

//        View layout_menu_6 = findViewById(R.id.layout_menu_6);
//        layout_menu_6.setOnClickListener(this);

        img_head = (ImageView) findViewById(R.id.img_head);

        text_value_1 = (TextView) findViewById(R.id.text_value_1);
        text_value_2 = (TextView) findViewById(R.id.text_value_2);
//        text_value_6 = (TextView) findViewById(R.id.text_value_6);

    }

    String phonenum = "";

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    void updateData() {
        if (AppUtils.isLogin(this))
            userInfo = userInfo(SESSION.getInstance().uid);

        if (userInfo != null) {
            imageLoader.displayImage(userInfo.avatar, img_head, FarmingApp.options_head);
            if (!"".equals(userInfo.nickname)) text_value_1.setText(userInfo.nickname);
            if (!"".equals(userInfo.username)) {
                phonenum = userInfo.username;
//                text_value_6.setText(AppUtils.getPhoneWithStar(phonenum));
            }
            request.gender = userInfo.gender + "";
            if (userInfo.gender == 0) {
                text_value_2.setText("男");
            }else if (userInfo.gender == 1){
                text_value_2.setText("女");
            }
        }
    }

    void updateImage() {
        imageLoader.displayImage(commonModel.fullPath, img_head, FarmingApp.options);
        request.avatar = commonModel.fullPath;
        setModified();
    }

    void setModified() {
        button_send.setEnabled(true);
    }

    public static USER userInfo(String uid) {
        return new Select().from(USER.class).where("USER_id = ?", uid).executeSingle();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_menu_0:
                if (checkLogined()) {
                    showSelectDialog();
                }
                break;
            case R.id.layout_menu_1:
                intent = new Intent(this, E02_EditFieldActivity.class);
                intent.putExtra("type", WAIT_EDIT_NICK);
                intent.putExtra("value", text_value_1.getText().toString());
                startActivityForResult(intent, WAIT_EDIT_NICK);
                break;
            case R.id.layout_menu_2:
                intent = new Intent(this, E02_EditGenderActivity.class);
                intent.putExtra("gender",request.gender);
                startActivityForResult(intent, WAIT_EDIT_GENDER);
                break;
            /*case R.id.layout_menu_20:
                request.gender = 1;
                dispSex(0);
                setModified();
                break;
            case R.id.layout_menu_21:
                request.gender = 0;
                dispSex(1);
                setModified();
                break;
            case R.id.layout_qrcode:
                intent = new Intent(this, D33_QrCodeActivity.class);
                intent.putExtra("target_type", 0);
                intent.putExtra("target_id", EjabberdConst.getMyID());
                startActivity(intent);
                break;
            case R.id.layout_menu_4:
                intent = new Intent(this, E02_EditFieldActivity.class);
                intent.putExtra("type", WAIT_EDIT_REALNAME);
                intent.putExtra("value", text_value_4.getText().toString());
                startActivityForResult(intent, WAIT_EDIT_REALNAME);
                break;
            case R.id.layout_menu_5:
                intent = new Intent(this, E02_EditFieldActivity.class);
                intent.putExtra("type", WAIT_EDIT_IDCARD);
                intent.putExtra("value", text_value_5.getText().toString());
                startActivityForResult(intent, WAIT_EDIT_IDCARD);
                break;*/
           /* case R.id.layout_menu_6:
                intent = new Intent(this, E02_ResetPhoneStartActivity.class);
                startActivityForResult(intent, WAIT_EDIT_PHONE);
                break;*/
            /*case R.id.layout_menu_7:
                intent = new Intent(this, E02_EditFieldActivity.class);
                intent.putExtra("type", WAIT_EDIT_ALIPAY);
                intent.putExtra("value", text_value_7.getText().toString());
                startActivityForResult(intent, WAIT_EDIT_ALIPAY);
                break;
            case R.id.layout_menu_8:
                intent = new Intent(this, E02_EditFieldActivity.class);
                intent.putExtra("type", WAIT_EDIT_TAG);
                intent.putExtra("value", text_value_8.getText().toString());
                startActivityForResult(intent, WAIT_EDIT_TAG);
                break;
            case R.id.layout_menu_9:
                intent = new Intent(this, E02_EditFieldActivity.class);
                intent.putExtra("type", WAIT_EDIT_JOB);
                intent.putExtra("value", text_value_9.getText().toString());
                startActivityForResult(intent, WAIT_EDIT_JOB);
                break;*/
            case R.id.button_send:
                saveModified();
                break;
        }
    }

    MyDialog mDialog;

    @Override
    public void finish() {
        if (button_send.isEnabled())
        {
            mDialog = new MyDialog(this, "", "是否放弃对资料的修改");
            mDialog.show();
            mDialog.positive.setText("放弃");
            mDialog.negative.setText("继续编辑");
            mDialog.negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mDialog.positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    E01_InfoActivity.super.finish();
                }
            });
        }
        else
        {
            super.finish();
        }
    }

    void saveModified()
    {
        userInfoModel.updateUser(request);
    }

    File file;
    String m_fileName = "";

    public final static int WAIT_EDIT_NICK = 1;
    public final static int WAIT_EDIT_REALNAME = 2;
    public final static int WAIT_EDIT_IDCARD = 3;
    public final static int WAIT_EDIT_GENDER = 4;
    public final static int WAIT_EDIT_ALIPAY = 5;
    public final static int WAIT_EDIT_TAG = 6;
    public final static int WAIT_EDIT_JOB = 7;

    static final int REQUEST_CODE_CAMERA = 8;
    static final int WAIT_IMAGE_CROP = 9;
    static final int WAIT_GET_CROP = 10;

    public final static int WAIT_EDIT_GROUP_DESC = 11;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (cameraFile == null && !TextUtils.isEmpty(beforePath))
                {
                    cameraFile = new File(beforePath);
                }

                if (cameraFile != null)
                {
                    if (cameraFile.exists())
                    {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getCropImageIntent(Uri.fromFile(cameraFile));
                    }
                }
            }
        }
        else if(data != null)
        {
            if (requestCode == WAIT_EDIT_NICK) {
                request.nickname = data.getStringExtra("value");
                text_value_1.setText(request.nickname);
                setModified();
            }
            else if (requestCode == WAIT_EDIT_GENDER) {
                request.gender = data.getStringExtra("value");
                if ("0".equals(request.gender))
                    text_value_2.setText("男");
                else if ("1".equals(request.gender))
                    text_value_2.setText("女");
                setModified();
            }
            /*else if (requestCode == WAIT_EDIT_IDCARD) {
                request.idcard = data.getStringExtra("value");
                text_value_5.setText(request.idcard);
                setModified();
            }*/
            /*else if (requestCode == WAIT_EDIT_PHONE) {
                phonenum = data.getStringExtra("value");
                text_value_6.setText(AppUtils.getPhoneWithStar(phonenum));
            }*/
           /* else if (requestCode == WAIT_EDIT_ALIPAY) {
                request.alipayNo = data.getStringExtra("value");
                text_value_7.setText(request.alipayNo);
                setModified();
            }
            else if (requestCode == WAIT_EDIT_TAG) {
                request.signature = data.getStringExtra("value");
                text_value_8.setText(request.signature);
                setModified();
            }
            else if (requestCode == WAIT_EDIT_JOB) {
                request.job = data.getStringExtra("value");
                text_value_9.setText(request.job);
                setModified();
            }*/
            else if (requestCode == WAIT_IMAGE_CROP)
            {
                if (data == null)
                    return;

                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                if (bitmap == null)
                    return;

                if (bitmap.getWidth() < 100 || bitmap.getHeight() < 100)
                {
                    errorMsg("头像太小，请重新选择");
                    return;
                }

                if (file == null) {
                    file = new File(DataCleanManager.getCacheDir(this)+"");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                }

                m_fileName = DataCleanManager.getCacheDir(this) + "/head" + System.currentTimeMillis() +".jpg";
                FileOutputStream b = null;
                try {
                    b = new FileOutputStream(m_fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    m_fileName = "";
                } finally {
                    try {
                        if (b != null)
                        {
                            b.flush();
                            b.close();

                            if (m_fileName != null && m_fileName.length() > 4)
                            {
                                uploadImage();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(requestCode == WAIT_GET_CROP)
            {
                if (data != null)
                    getCropImageIntent(data.getData());
            }
        }
    }

    void uploadImage() {
        if (!TextUtils.isEmpty(m_fileName)) {
            commonModel.uploadFile(m_fileName);
//            requestUploadUserImage(new File(m_fileName));
        }
    }

  /*  private void requestUploadUserImage(final File file) {

        RequestParams map = new RequestParams();
        try {
            map.put("fileUpload",file);
//			map.put("image",new File(m_fileName));
//			map.put("usermobile", UserManager.getInstance().getUser().getUsermobile());
//			map.put("imgname", file.getPath());

            AsyncHttpUtil.post("http://192.168.1.145:8055/NongYeWsInterface/WS/ImageService/taxuploadimg", map, new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(String s) {
                    JSONObject objRes = checkValidHttpResponse(E01_InfoActivity.this, s);
                    if (objRes != null) {

                        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                        img_head.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onFailure(Throwable throwable, String s) {
                    JSONObject objRes = checkValidHttpResponse(E01_InfoActivity.this, s);
                    if (objRes != null) {

                    }
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
*/
    public static JSONObject checkValidHttpResponse(final Context context, String receivedInfo)
    {
        try
        {
            if (checkNetworkAndMessage(context, receivedInfo)) {
                JSONObject jReceived =  new JSONObject(receivedInfo);
                if (jReceived != null) {
                    return jReceived;
                }
            }
        }
        catch(JSONException ex)
        {
            Log.e("", "MiscUtils::checkValidHttpResponse() -> " + ex.getMessage());
            return null;
        }
        return null;
    }

    public static boolean checkNetworkAndMessage(Context context, String receivedInfo)
    {
        boolean bValid = (!TextUtils.isEmpty(receivedInfo));
        if (!bValid) {
            if (isNetworkValid(context)) {
                //showMsg(context, R.string.no_connect_server);
            }
            else
                Toast.makeText(context, "" /*context.getResources().getString(R.string.network_invalid)*/, Toast.LENGTH_SHORT).show();
        }
        return bValid;
    }

    public static boolean isNetworkValid(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo[] networkInfos = connectivity.getAllNetworkInfo();
            if (networkInfos == null) {
                return false;
            }
            for (NetworkInfo networkInfo : networkInfos) {
                if (networkInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
            return false;
        } catch (Throwable e) {

            return false;
        }
    }

    //
    Dialog mMenuDialog;
    private void showSelectDialog() {
        // TODO Auto-generated method stub

        if (mMenuDialog == null)
        {
            LayoutInflater inflater = LayoutInflater.from(this);

            View view = inflater.inflate(R.layout.a00_select_menus, null);
            mMenuDialog = new Dialog(this, R.style.transparentFrameWindowStyle);
            mMenuDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Window window = mMenuDialog.getWindow();
            window.setWindowAnimations(R.style.main_menu_animstyle);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = AppUtils.getScHeight(this);
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mMenuDialog.onWindowAttributesChanged(wl);
            mMenuDialog.setCanceledOnTouchOutside(true);

            View layout_cancel = view.findViewById(R.id.layout_cancel);
            layout_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    mMenuDialog.dismiss();
                    mMenuDialog = null;
                }
            });

            View layout_camera, layout_local;
            layout_camera = view.findViewById(R.id.layout_camera);
            layout_camera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    mMenuDialog.dismiss();
                    mMenuDialog = null;
                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkCallPhonePermission = ContextCompat.checkSelfPermission(E01_InfoActivity.this, Manifest.permission.CAMERA);
                        if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(E01_InfoActivity.this,new String[]{Manifest.permission.CAMERA},222);
                            return;
                        }else{
                            openCamera(); //调用具体方法
                        }
                    } else {
                        openCamera(); //调用具体方法
                    }

                }
            });
            layout_local = view.findViewById(R.id.layout_local);
            layout_local.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    mMenuDialog.dismiss();
                    mMenuDialog = null;
                    selectPhotoes();
                }

            });
        }

        mMenuDialog.show();
    }

    //
    private void selectPhotoes() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //19以后这个api不可用，demo这里简单处理成图库选择图片
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, WAIT_GET_CROP);
    }

    private void setIntentParams(Intent intent)
    {
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
    }

    String beforePath = "";
    File cameraFile;
    Uri photoUri;

    private void openCamera()
    {
        File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/upload");
        if (!pictureFileDir.exists()) {
            pictureFileDir.mkdirs();
        }
        cameraFile = new File(pictureFileDir, "upload.jpeg");
        if (!cameraFile.exists()) {
            try {
                cameraFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        photoUri = Uri.fromFile(cameraFile);

        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

        startActivityForResult(intentFromCapture, REQUEST_CODE_CAMERA);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void getCropImageIntent(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        String url = FileUtils.getUriPath(this, uri);

        //sdk>=24
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

            Uri imageUri = FileProvider.getUriForFile(this, "com.dmy.fileprovider", new File(url));//通过FileProvider创建一个content类型的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra("noFaceDetection", true);//去除默认的人脸识别，否则和剪裁匡重叠
            intent.setDataAndType(imageUri, "image/*");

            //19=<sdk<24
        }else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT&&android.os.Build.VERSION.SDK_INT<Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");

            //sdk<19
        }else {
            intent.setDataAndType(uri, "image/*");
        }

//        intent.setDataAndType(uri, "image/*");
        setIntentParams(intent);
        startActivityForResult(intent, WAIT_IMAGE_CROP);
    }

    /*保存界面状态，如果activity意外被系统killed，返回时可以恢复状态值*/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        if (cameraFile != null)
        {
            savedInstanceState.putString("cameraFile", cameraFile.getAbsolutePath());
        }
        super.onSaveInstanceState(savedInstanceState); //实现父类方法 放在最后 防止拍照后无法返回当前activity
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
    {
        if (url.endsWith(ApiInterface.UPLOAD_FILE)) {
            if (commonModel.lastStatus.error_code == 200) {
                updateImage();
            } else {
                errorMsg(commonModel.lastStatus.error_desc);
            }
        }
        else if (url.endsWith(ApiInterface.USER_UPDATE))
        {
            if (userInfoModel.lastStatus.error_code == 200) {
                errorMsg("保存成功~");
                button_send.setEnabled(false);
                finish();
            }
            else
            {
                errorMsg(userInfoModel.lastStatus.error_desc);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 222:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    openCamera();
                } else {
                    // Permission Denied
                    errorMsg("请开启相机权限");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commonModel.removeResponseListener(this);
        userInfoModel.removeResponseListener(this);
    }
}
