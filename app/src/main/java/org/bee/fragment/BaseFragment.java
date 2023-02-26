package org.bee.fragment;

import android.support.v4.app.Fragment;

import org.bee.model.BusinessResponse;
import com.external.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseFragment extends Fragment implements BusinessResponse
{
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
            throws JSONException
    {

    }
}
