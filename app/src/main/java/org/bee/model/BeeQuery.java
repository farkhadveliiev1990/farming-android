package org.bee.model;

import android.content.Context;

import com.dmy.farming.AppConst;
import com.dmy.farming.api.ApiInterface;
import com.external.androidquery.AQuery;
import com.external.androidquery.callback.AjaxCallback;

import java.util.Map;


public class BeeQuery<T> extends AQuery {
	public BeeQuery(Context context) {
		super(context);
		 
	}

	public static final int ENVIRONMENT_PRODUCTION = 1;
	public static final int ENVIROMENT_DEVELOPMENT = 2;
	public static final int ENVIROMENT_MOCKSERVER = 3;
	
	public static int environment() 
	{
		return ENVIRONMENT_PRODUCTION;
	}

	/*
	public static String serviceUrl()
	{
		if (ENVIRONMENT_PRODUCTION == BeeQuery.environment())
		{
			return AppConst.SERVER_PRODUCTION;
		}
		else
		{
            return AppConst.SERVER_DEVELOPMENT;
		}
	}
	*/

    public static  String wapCallBackUrl(){
        if (ENVIRONMENT_PRODUCTION == BeeQuery.environment())
        {
            return AppConst.WAP_PAY_CALLBCK_PRODUCTION;
        }
        else
        {
            return AppConst.WAP_PAY_CALLBCK_DEVELOPMENT;
        }
    }
	public <K> AQuery ajax(AjaxCallback<K> callback){

		if (BeeQuery.environment() == BeeQuery.ENVIROMENT_MOCKSERVER)
		{
			MockServer.ajax(callback);
			return null;
		}
        else
        {
            String url = callback.getUrl();
            String absoluteUrl = getAbsoluteUrl(url);

            callback.url(absoluteUrl);

        }

        if(BeeQuery.environment() == BeeQuery.ENVIROMENT_DEVELOPMENT)
        {
            DebugMessageModel.addMessage((BeeCallback)callback);
        }

		return (BeeQuery)super.ajax(callback);
	}

    public <K> AQuery ajaxAbsolute(AjaxCallback<K> callback){

        return (BeeQuery)super.ajax(callback);
    }

	public <K> AQuery ajax(String url, Map<String, ?> params, Class<K> type, BeeCallback<K> callback){
						
		callback.type(type).url(url).params(params);
		
		if (BeeQuery.environment() == BeeQuery.ENVIROMENT_MOCKSERVER)
		{
			MockServer.ajax(callback);
			return null;
		}
        else
        {
            String absoluteUrl = getAbsoluteUrl(url);
            callback.url(absoluteUrl);
        }
		return ajax(callback);
	}

    private static String getAbsoluteUrl(String relativeUrl) {
//		if (relativeUrl.contains(ApiInterface.MONEY_RECHARGE)
//				|| relativeUrl.contains(ApiInterface.MONEY_LIST)
//				|| relativeUrl.contains(ApiInterface.MONEY_SUM)) { //if (relativeUrl.startsWith("/chat/") || relativeUrl.startsWith("/group/"))
//			return AppConst.SERVER_MONEY + "/mobile" + relativeUrl;
//		} else {
        if (relativeUrl.contains("http"))
            return relativeUrl;
        else
            return AppConst.SERVER_MAIN + relativeUrl;
//		}
    }
}