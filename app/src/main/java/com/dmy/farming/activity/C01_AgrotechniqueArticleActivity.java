package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_ArticleAdapter;
import com.dmy.farming.adapter.E01_MyFollowListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.CROPCYCLE;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.farmarticleRequest;
import com.dmy.farming.api.model.CropCycleModel;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.model.FarmArticleModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import static com.dmy.farming.R.id.center;
import static com.dmy.farming.R.id.textView;

public class C01_AgrotechniqueArticleActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_activity_agrotechnique_article);
        followModel = new DictionaryModel(this);
        followModel.addResponseListener(this);
        farmArticleModel = new FarmArticleModel(this);
        farmArticleModel.addResponseListener(this);
        cropCycleModel = new CropCycleModel(this);
        cropCycleModel.addResponseListener(this);

        request  = new farmarticleRequest();

        followModel.followType(AppConst.info_from,true);
        initView();
        initView1();

	}

	XListView listView;
    FarmArticleModel farmArticleModel;
    C01_ArticleAdapter articleAdapter;
    DictionaryModel followModel;
    CropCycleModel cropCycleModel;
    private LayoutInflater mInflater;
    private LinearLayout mGallery,mGallery1;
    private String[] mImgIds;
    private String[] codesubname;
    private String[] diccode;
    private String[] cyclecode;
    View null_pager;
    farmarticleRequest request;
    ImageView img_search;

    void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

        null_pager = findViewById(R.id.null_pager);
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);
        mGallery1 = (LinearLayout) findViewById(R.id.subcrop);

        img_search = (ImageView)findViewById(R.id.img_search) ;
        img_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(C01_AgrotechniqueArticleActivity.this,B01_SearchActivity.class);
                startActivity(intent);
            }
        });
		findViewById(R.id.img_add).setOnClickListener(this);

		listView = (XListView)findViewById(R.id.list_black);
        listView.setXListViewListener(this, 1);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(false);

	}

    String  userid = "1";
    int m = 0;
    String  codetype;
    private void initView1()// 填充数据
    {
        mGallery.removeAllViews();
        if(mImgIds != null) {
                for (int i = 0; i < mImgIds.length; i++) {
                        final TextView textView = new TextView(this);
                        textView.setText(mImgIds[i]);
                        textView.setId(i);
                        textView.setTextSize(18);
                        WindowManager manager =getWindowManager();
                        DisplayMetrics outMetrics = new DisplayMetrics();
                        manager.getDefaultDisplay().getMetrics(outMetrics);
                        int width = outMetrics.widthPixels;
                        textView.setWidth(width/5);
                        textView.setGravity(Gravity.CENTER);
                        textView.setPadding(10,10,10,10);
                        textView.setTextColor(getResources().getColor(R.color.text_grey));
                      //  textView.setLayoutParams(params);
                        if(i==0){
                            textView.setBackground(getResources().getDrawable(R.drawable.watermellon));
                            textView.setTextColor(getResources().getColor(R.color.green));
                        }
                        mGallery.addView(textView);
                        textView.setTag(i);
                        textView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                       v.setBackground(getResources().getDrawable(R.drawable.watermellon));
                                       textView.setTextColor(getResources().getColor(R.color.green));
                                        m=(Integer) textView.getTag();
                                       selectTab(m);
                                   //    crop_type = mImgIds[m];
                                       mGallery1.removeAllViews();
                                        crop_type = diccode[m];
                                       cropCycleModel.cropcycleType(AppConst.info_from,crop_type,true);
                            }
                        });
                }


        }
    }
    private void selectTab(int position) {
    // TODO Auto-generated method stub
            for (int i = 0; i < mGallery.getChildCount(); i++) {
                //TextView childAt = (TextView) mGallery.getChildAt(position);
                TextView child = (TextView) mGallery.getChildAt(i);
                if (position == i) {
                    child.setTextColor(getResources().getColor(R.color.green));
                } else {
                    child.setTextColor(getResources().getColor(R.color.text_grey));
                    child.setBackgroundResource(0);
                }

            }

        }
    private void selectTab1(int position) {
        // TODO Auto-generated method stub
        for (int i = 0; i < mGallery1.getChildCount(); i++) {
            //TextView childAt = (TextView) mGallery.getChildAt(position);
            TextView child = (TextView) mGallery1.getChildAt(i);
            if (position == i) {
                child.setTextColor(getResources().getColor(R.color.green));
            } else {
                child.setTextColor(getResources().getColor(R.color.text_grey));
                child.setBackgroundResource(0);
            }

        }

    }



    private void initView2()// 填充数据
    {
        mGallery1.removeAllViews();
        if(codesubname != null) {
            for (int i = 0; i < codesubname.length; i++) {
                final TextView textView = new TextView(this);
                textView.setText(codesubname[i]);
                textView.setId(i);
                textView.setTextSize(16);
                WindowManager manager =getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(outMetrics);
                int width = outMetrics.widthPixels;
                textView.setWidth(width/5);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(10,5,10,5);
                textView.setTextColor(getResources().getColor(R.color.text_grey));
                if(i==0){
                        textView.setTextColor(getResources().getColor(R.color.green));
                }
                mGallery1.addView(textView);
                textView.setTag(i);
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                           /* case 0:
                                final ViewGroup.LayoutParams lp =  v.getLayoutParams();
                                lp.width = lp.WRAP_CONTENT;
                                lp.height=lp.WRAP_CONTENT;
                                int left1, top1, right1, bottom1;
                                left1 = top1 = right1 = bottom1 = 10;
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(left1, top1, right1, bottom1);
                                v.setLayoutParams(lp);
                                textView.setTextColor(getResources().getColor(R.color.green));
                                m=(Integer) textView.getTag();
                                selectTab(m);
                                break;*/
                            textView.setTextColor(getResources().getColor(R.color.green));
                            m=(Integer) textView.getTag();
                            selectTab1(m);
                            code_type = cyclecode[m];
                            articleAdapter = null;
                            requestData(true);
                    }
                });
            }


        }
    }
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
		/*	case R.id.layout_article_item:
				intent = new Intent(C01_AgrotechniqueArticleActivity.this,C02_AgrotechniqueArticleDetailActivity.class);
				startActivity(intent);
				break;*/
			case R.id.img_add:
                if(checkLogined()) {
                intent = new Intent(C01_AgrotechniqueArticleActivity.this, ChooseCropActivity.class);
                ArrayList<String> attentname = new ArrayList<>();
                ArrayList<String> attentcode = new ArrayList<>();
                if (ad != null) {
                    for (DICTIONARY dictionary : ad) {
                        attentname.add(dictionary.name);
                        attentcode.add(dictionary.aboutCode);
                    }
                }
                intent.putStringArrayListExtra("attentname", attentname);
                intent.putStringArrayListExtra("attentcode", attentcode);
                startActivityForResult(intent,REQUEST_MONEY);

            }
				break;

		}
	}

	@Override
	protected void onResume() {

        super.onResume();

	}

    String  crop_type = "";
    String  cycle_type = "";
    String code_type="";
    public void requestData(boolean bShow) {
        //request.city = AppUtils.getCurrCity(mActivity);

        request.info_from = AppConst.info_from;
        request.crop_type = crop_type;
        request.cycle_type = code_type;
        request.page = 1;
        request.sort = 0;
        request.provice = AppUtils.getCurrProvince(this);
        request.city = AppUtils.getCurrCity(this);
        farmArticleModel.getFarmArticle(request,bShow);

    }


    private ArrayList<DICTIONARY> ad;
    private void updatecroyData() {
        if (followModel.data.follow_type.size() > 0) {
            null_pager.setVisibility(View.GONE);
            mGallery1.setVisibility(View.VISIBLE);

            ad =  followModel.data.follow_type;
            diccode = new String[followModel.data.follow_type.size()];
            if(followModel.data.follow_type.size()>6){
                mImgIds = new String[6];
                for(int i = 0 ;i<6;i++){
                    mImgIds[i] = ad.get(i).name;
                    diccode[i] = ad.get(i).aboutCode;
                }
            }else{
                mImgIds = new String[followModel.data.follow_type.size()];
                for(int i = 0 ;i<followModel.data.follow_type.size();i++){
                    mImgIds[i] = ad.get(i).name;
                    diccode[i] = ad.get(i).aboutCode;
                }
            }
            codetype =  ad.get(0).name;
           // code_type = ad.get(0).aboutCode;
            crop_type = ad.get(0).aboutCode;
            initView1();
            cropCycleModel.cropcycleType(AppConst.info_from,crop_type,true);
        } else {
            mGallery1.setVisibility(View.GONE);
            null_pager.setVisibility(View.VISIBLE);

            Intent intent = new Intent(C01_AgrotechniqueArticleActivity.this,ChooseCropActivity.class);
            ArrayList<String> attentname = new ArrayList<>();
            ArrayList<String> attentcode = new ArrayList<>();
            intent.putStringArrayListExtra("attentname",attentname);
            intent.putStringArrayListExtra("attentcode",attentcode);
            startActivity(intent);
        }
    }

    private ArrayList<CROPCYCLE> ad1;
    private void updatecropctcleData() {
        if (cropCycleModel.data.crop_cycle.size() > 0) {
            ad1 =  cropCycleModel.data.crop_cycle;
            codesubname = new String[cropCycleModel.data.crop_cycle.size()];
            cyclecode = new String[cropCycleModel.data.crop_cycle.size()];
            for(int i = 0 ;i<ad1.size();i++){
                codesubname[i] = ad1.get(i).dicname;
                cyclecode[i] = ad1.get(i).cycleType;
            }
            code_type = ad1.get(0).cycleType;
            initView2();
            articleAdapter = null;
            request.info_from = AppConst.info_from;
            request.crop_type = crop_type;
            request.cycle_type = code_type;
            request.page = 1;
            request.sort = 0;
            request.provice = AppUtils.getCurrProvince(this);
            request.city = AppUtils.getCurrCity(this);
            farmArticleModel.getFarmArticle(request,true);
        } else {
        }
    }

    private void updateData() {
        if (farmArticleModel.data.farmarticles.size() > 0) {
            null_pager.setVisibility(View.GONE);
            if (articleAdapter == null) {
                articleAdapter = new C01_ArticleAdapter(this, farmArticleModel.data.farmarticles);
                listView.setAdapter(articleAdapter);
            } else {
               // farmArticleModel.getFarmArticle(info_from,crop_type,code_type,true);
                articleAdapter.notifyDataSetChanged();
            }
            if (0 == farmArticleModel.paginated.more) {
                listView.setPullLoadEnable(false);
            } else {
                listView.setPullLoadEnable(true);
            }
        } else {
            null_pager.setVisibility(View.VISIBLE);
            articleAdapter = null;
            //gridView.setAdapter(null);
        }
    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
            throws JSONException {
        if (url.contains(ApiInterface.FOLLOWTYPE)) {
            updatecroyData();
        }
        if(url.contains(ApiInterface.cropcycle)){
            updatecropctcleData();
        }
        if(url.contains(ApiInterface.ARTICLE)){
            updateData();
        }
    }


    final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_MONEY) {
                followModel.followType(AppConst.info_from, true);
            }
        }
	}


	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {

	}
}
