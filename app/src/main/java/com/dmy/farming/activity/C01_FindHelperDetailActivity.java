package com.dmy.farming.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.ADVER;
import com.dmy.farming.api.data.FINDHELPLIST;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.chat.RENTLIST;
import com.dmy.farming.api.model.FindHelperDetailModel;
import com.dmy.farming.api.model.RentDetailModel;
import com.dmy.farming.api.model.SaleResponse;
import com.dmy.farming.protocol.PHOTO;
import com.dmy.farming.view.B01_Home_Banner;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class C01_FindHelperDetailActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

    String id,type;/*title,time,price,address_detail,content*/;
    MainActivity mActivity;
    FindHelperDetailModel findHelperDetailModel;
    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c01_sell_item_detail);

        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
		/*title = getIntent().getStringExtra("title");
		time = getIntent().getStringExtra("time");
		price = getIntent().getStringExtra("price");
		address_detail = getIntent().getStringExtra("address_detail");
		content = getIntent().getStringExtra("content");*/

        initView();
        findHelperDetailModel = new FindHelperDetailModel(this, id);
        findHelperDetailModel.addResponseListener(this);


    }

    XListView list_black;
    TextView phone, title, price, time, address_detail, content,name,farm;
    ImageView img;
    String textphone = "";
    View layout_contact;
    B01_Home_Banner mainBanner;
    String iscollection ="0";
    FrameLayout more;
    static  String  publishuser ;
    View null_pager;

    void initView() {
        View img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        null_pager = findViewById(R.id.null_pager);
        null_pager.setVisibility(View.VISIBLE);

        phone = (TextView) findViewById(R.id.phone);
        phone.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        time = (TextView) findViewById(R.id.time);
        address_detail = (TextView) findViewById(R.id.address_detail);
        content = (TextView) findViewById(R.id.content);
        img = (ImageView)findViewById(R.id.img);
        name = (TextView) findViewById(R.id.name);
        farm =(TextView)findViewById(R.id.farm);
        mainBanner = (B01_Home_Banner)findViewById(R.id.layout_banner);
    //    urls = new String[3];
       layout_contact = findViewById(R.id.layout_contact);
        if (type.equals("mine"))
            layout_contact.setVisibility(View.GONE);
        else
            layout_contact.setVisibility(View.VISIBLE);

        more =(FrameLayout)findViewById(R.id.more);
        more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow == null) {
                    showSelectDialog(v);
                }
            }
        });
		/*saleModel = new SaleModel(mActivity);
		saleModel.addResponseListener(this);*/

        //list_black = (XListView) findViewById(R.id.list_black);
        //list_black.setXListViewListener(this, 1);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.phone:
                call(textphone);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    String about_user="";
    PopupWindow popupWindow;
    ImageView collection;
    private void showSelectDialog(View v) {
        //??????????????????????????????
        View view = LayoutInflater.from(this).inflate(R.layout.d01_question_item, null);
        final View layout_collection = view.findViewById(R.id.layout_collection);
        collection = (ImageView) view.findViewById(R.id.collection);
        if(iscollection.equals("0")){
            collection.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
        }else{
            collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
        }
        if(checkLogined()){
            layout_collection.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iscollection.equals("0")) {
                        collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
                        findHelperDetailModel.collection(id);
                    } else {
                        findHelperDetailModel.cancelcollection(id);
                        collection.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
                    }
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });
        }
        if (publishuser.equals(SESSION.getInstance().sid)){
            final View report = view.findViewById(R.id.layout_report);
            View line2 = view.findViewById(R.id.line2);
            report.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            report.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //	requestData(true);
                    Intent intent = new Intent(C01_FindHelperDetailActivity.this,ReportActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("from","7");
                    intent.putExtra("iscomment","0");
//                    if(SESSION.getInstance().usertype ==1){
//                        about_user =SESSION.getInstance().uid;
//                    }else{
                        about_user =SESSION.getInstance().sid;
//                    }
                    intent.putExtra("about_user",about_user);
                    startActivity(intent);
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });
        }else{
            final View report = view.findViewById(R.id.layout_report);
            report.setVisibility(View.VISIBLE);
            report.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //	requestData(true);
                    Intent intent = new Intent(C01_FindHelperDetailActivity.this,ReportActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("from","7");
                    intent.putExtra("iscomment","0");
//                    if(SESSION.getInstance().usertype ==1){
//                        about_user =SESSION.getInstance().uid;
//                    }else{
                        about_user =SESSION.getInstance().sid;
//                    }
                    intent.putExtra("about_user",about_user);
                    startActivity(intent);
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });
        }

        final View share = view.findViewById(R.id.layout_share);
        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //	requestData(true);
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                //??????????????????true?????????touch??????????????????
                //????????? PoppWindow???onTouchEvent?????????????????????????????????????????????dismiss
            }
        });
        //???????????????????????????????????????popupWindow?????????????????????????????????????????????Back??????????????????
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow = null;
            }
        });

        popupWindow.showAsDropDown(v,50,10);

    }



    /**
	 * ??????????????????
	 * @param phone ????????????
	 */
	private void call(String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
	}

	void requestData() {
        findHelperDetailModel.getRoute();
//		groupListModel.getGroupList(request, false);
	}

    ArrayList<ADVER> adver_top = new ArrayList<>();

    String[] urls = new String[3];
    void initBanner()
    {
        for (int i = 0; i < urls.length; i++)
        {
            ADVER adver = new ADVER();
            PHOTO photo = new PHOTO();
            photo.url = urls[i];
            adver.adver_img = photo;
            adver.adver_id = (i + 5000) + "";
            adver.target = (i + 5000) + "";
            adver_top.add(adver);
        }
        upadteBanner();
    }

    void upadteBanner()
    {
        if (adver_top != null && adver_top.size() > 0)
        {
            mainBanner.bindData(adver_top);

            if (adver_top.size() == 1)
                mainBanner.stopReply();
            else
                mainBanner.startReply();

        }

    }


    @Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        if (url.contains(ApiInterface.FINDHELPDETAIL)) {
            if (findHelperDetailModel.lastStatus.error_code == 200) {
                if (findHelperDetailModel.data.deleted == 1){
                    errorMsg("??????????????????");
                }else {
                    updateData();
                }
            } else {
                errorMsg(findHelperDetailModel.lastStatus.error_desc);
            }
        } else if (url.contains(ApiInterface.COLLECTION)) {
            if (findHelperDetailModel.lastStatus.error_code == 200) {
                errorMsg("????????????");
                iscollection = "1";
            }
        }
        if (url.contains(ApiInterface.CANCELCOLLECTION)) {
            if (findHelperDetailModel.lastStatus.error_code == 200) {
                errorMsg("??????????????????");
                iscollection = "0";
            }

        }
    }

	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			if (requestCode == REQUEST_MONEY) {

			}
		}
	}

	void updateData() {

		if (id.equals(findHelperDetailModel.id)) {
            null_pager.setVisibility(View.GONE);
            FINDHELPLIST route = findHelperDetailModel.data;
			title.setText(route.title);
            price.setText(route.price);
            time.setText(route.publishTime);
            address_detail.setText(route.addressDetails);
            content.setText(route.content);
            textphone = route.link_phone;
            name.setText(route.link_name);
            farm.setText(route.helpType);
            publishuser = route.user_phone;
            iscollection =route.iscollection;
            urls = route.imgUrl.split(",");
            initBanner();
          //  imageLoader.displayImage(urls[0], img, FarmingApp.options);
          //  img.set
		}else {
            null_pager.setVisibility(View.VISIBLE);
        }

	}


    @Override
    public void onRefresh(int id) {
        requestData();
    }

    @Override
    public void onLoadMore(int id) {

    }
}