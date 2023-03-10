package com.dmy.farming.view.addressselector;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import com.dmy.farming.R;
import com.dmy.farming.view.addressselector.model.City;
import com.dmy.farming.view.addressselector.model.County;
import com.dmy.farming.view.addressselector.model.Province;
import com.dmy.farming.view.addressselector.model.Street;

public class AddressSelector /*implements AdapterView.OnItemClickListener*/ {
    private static final int INDEX_TAB_PROVINCE = 0;
    private static final int INDEX_TAB_CITY = 1;
    private static final int INDEX_TAB_COUNTY = 2;
    private static final int INDEX_TAB_STREET = 3;

    private static final int INDEX_INVALID = -1;

    private static final int WHAT_PROVINCES_PROVIDED = 0;
    private static final int WHAT_CITIES_PROVIDED = 1;
    private static final int WHAT_COUNTIES_PROVIDED = 2;
    private static final int WHAT_STREETS_PROVIDED = 3;

    @SuppressWarnings("unchecked")
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_PROVINCES_PROVIDED:
                    provinces = (List<Province>) msg.obj;
                    provinceAdapter.notifyDataSetChanged();
                    listView.setAdapter(provinceAdapter);
                    updateProvinceProgressVisibility();
                    break;

                case WHAT_CITIES_PROVIDED:
                    cities = (List<City>) msg.obj;
                    cityAdapter.notifyDataSetChanged();
                    if (Lists.notEmpty(cities)) {
                        // ???????????????????????????
                        listView_city.setAdapter(cityAdapter);
                        // ?????????????????????
                        tabIndex = INDEX_TAB_CITY;
                    } else {
                        // ????????????????????????
                        callbackInternal();
                    }
                    updateCityProgressVisibility();
                    break;

                case WHAT_COUNTIES_PROVIDED:
                    counties = (List<County>) msg.obj;
                    countyAdapter.notifyDataSetChanged();
                    if (Lists.notEmpty(counties)) {
                        listView_country.setAdapter(countyAdapter);
                        tabIndex = INDEX_TAB_COUNTY;
                    } else {
                        callbackInternal();
                    }
                    updateCountryProgressVisibility();
                    break;

//                case WHAT_STREETS_PROVIDED:
//                    streets = (List<Street>) msg.obj;
//                    streetAdapter.notifyDataSetChanged();
//                    if (Lists.notEmpty(streets)) {
//                        listView.setAdapter(streetAdapter);
//                        tabIndex = INDEX_TAB_STREET;
//                    } else {
//                        callbackInternal();
//                    }
//                    callbackInternal();
//                    break;
            }

            updateTabsVisibility();
//            updateProgressVisibility();
            updateIndicator();

            return true;
        }
    });

    private static AddressProvider DEFAULT_ADDRESS_PROVIDER;

    private final Context context;
    private OnAddressSelectedListener listener;
    private AddressProvider addressProvider;

    private View view;

    private View indicator;

    private TextView textViewProvince;
    private TextView textViewCity;
    private TextView textViewCounty;
//    private TextView textViewStreet;

    private ProgressBar progressBar,progressBar_city,progressBar_country;

    private ListView listView,listView_city,listView_country;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private CountyAdapter countyAdapter;
//    private StreetAdapter streetAdapter;

    private List<Province> provinces;
    private List<City> cities;
    private List<County> counties;
//    private List<Street> streets;

    private int provinceIndex = INDEX_INVALID;
    private int cityIndex = INDEX_INVALID;
    private int countyIndex = INDEX_INVALID;
    private int streetIndex = INDEX_INVALID;

    private int tabIndex = INDEX_TAB_PROVINCE;

    public AddressSelector(Context context) {
        this.context = context;

        DEFAULT_ADDRESS_PROVIDER = new DefaultAddressProvider(context);
        addressProvider = DEFAULT_ADDRESS_PROVIDER;

        initViews();
        initAdapters();
        retrieveProvinces();
    }

    private void initAdapters() {
        provinceAdapter = new ProvinceAdapter();
        cityAdapter = new CityAdapter();
        countyAdapter = new CountyAdapter();
//        streetAdapter = new StreetAdapter();
    }

    private void initViews() {
        view = LayoutInflater.from(context).inflate(R.layout.address_selector, null);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.progressBar_city = (ProgressBar) view.findViewById(R.id.progressBar_c);
        this.progressBar_country = (ProgressBar) view.findViewById(R.id.progressBar_country);

        this.listView = (ListView) view.findViewById(R.id.listView);
        this.listView_city = (ListView) view.findViewById(R.id.listView_c);
        this.listView_country = (ListView) view.findViewById(R.id.listView_country);
        this.indicator = view.findViewById(R.id.indicator);

        this.textViewProvince = (TextView) view.findViewById(R.id.textViewProvince);
        this.textViewCity = (TextView) view.findViewById(R.id.textViewCity);
        this.textViewCounty = (TextView) view.findViewById(R.id.textViewCounty);
//        this.textViewStreet = (TextView) view.findViewById(R.id.textViewStreet);

        this.textViewProvince.setOnClickListener(new OnProvinceTabClickListener());
        this.textViewCity.setOnClickListener(new OnCityTabClickListener());
        this.textViewCounty.setOnClickListener(new OnCountyTabClickListener());
//        this.textViewStreet.setOnClickListener(new OnStreetTabClickListener());

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Province province = provinceAdapter.getItem(position);

                // ???????????????????????????????????????
//                textViewProvince.setText(province.name);
//                textViewCity.setText("??????");
//                textViewCounty.setText("??????");
//                textViewStreet.setText("?????????");

                // ??????????????????
                cities = null;
                counties = null;
//                streets = null;
                cityAdapter.notifyDataSetChanged();
                countyAdapter.notifyDataSetChanged();
//                streetAdapter.notifyDataSetChanged();

                // ??????????????????
                provinceIndex = position;
                cityIndex = INDEX_INVALID;
                countyIndex = INDEX_INVALID;
                streetIndex = INDEX_INVALID;

                // ??????????????????
                provinceAdapter.notifyDataSetChanged();

                retrieveCitiesWith(province.id);

                updateTabsVisibility();
                updateIndicator();
            }
        });
        this.listView_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = cityAdapter.getItem(position);

//                textViewCity.setText(city.name);
//                textViewCounty.setText("?????????");
//                textViewStreet.setText("?????????");

                counties = null;
//                streets = null;
                countyAdapter.notifyDataSetChanged();
//                streetAdapter.notifyDataSetChanged();

                cityIndex = position;
                countyIndex = INDEX_INVALID;
                streetIndex = INDEX_INVALID;

                cityAdapter.notifyDataSetChanged();

                retrieveCountiesWith(city.id);

                updateTabsVisibility();
                updateIndicator();
            }
        });
        this.listView_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                County county = countyAdapter.getItem(position);

//                textViewCounty.setText(county.name);
//                textViewStreet.setText("?????????");

//                streets = null;
//                streetAdapter.notifyDataSetChanged();

                countyIndex = position;
                streetIndex = INDEX_INVALID;

                countyAdapter.notifyDataSetChanged();

//                retrieveStreetsWith(county.id);

                updateTabsVisibility();
                updateIndicator();

                callbackInternal();
            }
        });

        updateIndicator();
    }

    public View getView() {
        return view;
    }

    private void updateIndicator() {
        view.post(new Runnable() {
            @Override
            public void run() {
                switch (tabIndex) {
                    case INDEX_TAB_PROVINCE:
                        buildIndicatorAnimatorTowards(textViewProvince).start();
                        break;
                    case INDEX_TAB_CITY:
                        buildIndicatorAnimatorTowards(textViewCity).start();
                        break;
                    case INDEX_TAB_COUNTY:
                        buildIndicatorAnimatorTowards(textViewCounty).start();
                        break;
//                    case INDEX_TAB_STREET:
//                        buildIndicatorAnimatorTowards(textViewStreet).start();
//                        break;
                }
            }
        });
    }

    private AnimatorSet buildIndicatorAnimatorTowards(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(indicator, "X", indicator.getX(), tab.getX()+ 70);

        final ViewGroup.LayoutParams params = indicator.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(params.width, /*tab.getMeasuredWidth()*/ 100);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (int) animation.getAnimatedValue();
                indicator.setLayoutParams(params);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(xAnimator, widthAnimator);

        return set;
    }

    private class OnProvinceTabClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_PROVINCE;
            listView.setAdapter(provinceAdapter);

            if (provinceIndex != INDEX_INVALID) {
                listView.setSelection(provinceIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private class OnCityTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_CITY;
            listView_city.setAdapter(cityAdapter);

            if (cityIndex != INDEX_INVALID) {
                listView_city.setSelection(cityIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private class OnCountyTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_COUNTY;
            listView_country.setAdapter(countyAdapter);

            if (countyIndex != INDEX_INVALID) {
                listView_country.setSelection(countyIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private class OnStreetTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_STREET;
//            listView.setAdapter(streetAdapter);

            if (streetIndex != INDEX_INVALID) {
                listView.setSelection(streetIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private void updateTabsVisibility() {
//        textViewProvince.setVisibility(Lists.notEmpty(provinces) ? View.VISIBLE : View.GONE);
//        textViewCity.setVisibility(Lists.notEmpty(cities) ? View.VISIBLE : View.GONE);
//        textViewCounty.setVisibility(Lists.notEmpty(counties) ? View.VISIBLE : View.GONE);
//        textViewStreet.setVisibility(Lists.notEmpty(streets) ? View.VISIBLE : View.GONE);

        textViewProvince.setEnabled(tabIndex != INDEX_TAB_PROVINCE);
        textViewCity.setEnabled(tabIndex != INDEX_TAB_CITY);
        textViewCounty.setEnabled(tabIndex != INDEX_TAB_COUNTY);
//        textViewStreet.setEnabled(tabIndex != INDEX_TAB_STREET);
    }

   /* @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (tabIndex) {
            case INDEX_TAB_PROVINCE:
                Province province = provinceAdapter.getItem(position);

                // ???????????????????????????????????????
                textViewProvince.setText(province.name);
                textViewCity.setText("?????????");
                textViewCounty.setText("?????????");
//                textViewStreet.setText("?????????");

                // ??????????????????
                cities = null;
                counties = null;
//                streets = null;
                cityAdapter.notifyDataSetChanged();
                countyAdapter.notifyDataSetChanged();
//                streetAdapter.notifyDataSetChanged();

                // ??????????????????
                this.provinceIndex = position;
                this.cityIndex = INDEX_INVALID;
                this.countyIndex = INDEX_INVALID;
                this.streetIndex = INDEX_INVALID;

                // ??????????????????
                provinceAdapter.notifyDataSetChanged();

                retrieveCitiesWith(province.id);

                break;

            case INDEX_TAB_CITY:
                City city = cityAdapter.getItem(position);

                textViewCity.setText(city.name);
                textViewCounty.setText("?????????");
//                textViewStreet.setText("?????????");

                counties = null;
//                streets = null;
                countyAdapter.notifyDataSetChanged();
//                streetAdapter.notifyDataSetChanged();

                this.cityIndex = position;
                this.countyIndex = INDEX_INVALID;
                this.streetIndex = INDEX_INVALID;

                cityAdapter.notifyDataSetChanged();

                retrieveCountiesWith(city.id);

                break;

            case INDEX_TAB_COUNTY:
                County county = countyAdapter.getItem(position);

                textViewCounty.setText(county.name);
//                textViewStreet.setText("?????????");

//                streets = null;
//                streetAdapter.notifyDataSetChanged();

                this.countyIndex = position;
                this.streetIndex = INDEX_INVALID;

                countyAdapter.notifyDataSetChanged();

                retrieveStreetsWith(county.id);

                break;

//            case INDEX_TAB_STREET:
//                Street street = streetAdapter.getItem(position);
//
//                textViewStreet.setText(street.name);
//
//                this.streetIndex = position;
//
//                streetAdapter.notifyDataSetChanged();
//
//                callbackInternal();
//
//                break;
        }

        updateTabsVisibility();
        updateIndicator();
    }*/

    private void callbackInternal() {
        if (listener != null) {
            Province province = provinces == null || provinceIndex == INDEX_INVALID ? null : provinces.get(provinceIndex);
            City city = cities == null || cityIndex == INDEX_INVALID ? null : cities.get(cityIndex);
            County county = counties == null || countyIndex == INDEX_INVALID ? null : counties.get(countyIndex);
//            Street street = streets == null || streetIndex == INDEX_INVALID ? null : streets.get(streetIndex);

            listener.onAddressSelected(province, city, county/*, street*/);
        }
    }

    private void updateProvinceProgressVisibility() {
        ListAdapter adapter = listView.getAdapter();
        int itemCount = adapter.getCount();
        progressBar.setVisibility(itemCount > 0 ? View.GONE : View.VISIBLE);

        progressBar_city.setVisibility(View.GONE);
        progressBar_country.setVisibility(View.GONE);

    }

    private void updateCityProgressVisibility() {
        ListAdapter cityAdapter = listView_city.getAdapter();
        if (cityAdapter != null) {
            int cityItemCount = cityAdapter.getCount();
            progressBar_city.setVisibility(cityItemCount > 0 ? View.GONE : View.VISIBLE);
        }else {
            progressBar_city.setVisibility(View.GONE);
        }

        progressBar_country.setVisibility(View.GONE);
    }

    private void updateCountryProgressVisibility() {
        ListAdapter countryAdapter = listView_country.getAdapter();
        if (countryAdapter != null) {
            int countryItemCount = countryAdapter.getCount();
            progressBar_country.setVisibility(countryItemCount > 0 ? View.GONE : View.VISIBLE);
        }else {
            progressBar_country.setVisibility(View.GONE);
        }
    }

    private void retrieveProvinces() {
        progressBar.setVisibility(View.VISIBLE);
        addressProvider.provideProvinces(new AddressProvider.AddressReceiver<Province>() {
            @Override
            public void send(List<Province> data) {
                handler.sendMessage(Message.obtain(handler, WHAT_PROVINCES_PROVIDED, data));
            }
        });
    }

    private void retrieveCitiesWith(int provinceId) {
        progressBar_city.setVisibility(View.VISIBLE);
        addressProvider.provideCitiesWith(provinceId, new AddressProvider.AddressReceiver<City>() {
            @Override
            public void send(List<City> data) {
                handler.sendMessage(Message.obtain(handler, WHAT_CITIES_PROVIDED, data));
            }
        });
    }

    private void retrieveCountiesWith(int cityId) {
        progressBar_country.setVisibility(View.VISIBLE);
        addressProvider.provideCountiesWith(cityId, new AddressProvider.AddressReceiver<County>() {
            @Override
            public void send(List<County> data) {
                handler.sendMessage(Message.obtain(handler, WHAT_COUNTIES_PROVIDED, data));
            }
        });
    }

//    private void retrieveStreetsWith(int countyId) {
//        progressBar.setVisibility(View.VISIBLE);
//        addressProvider.provideStreetsWith(countyId, new AddressProvider.AddressReceiver<Street>() {
//            @Override
//            public void send(List<Street> data) {
//                handler.sendMessage(Message.obtain(handler, WHAT_STREETS_PROVIDED, data));
//            }
//        });
//    }

    private class ProvinceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return provinces == null ? 0 : provinces.size();
        }

        @Override
        public Province getItem(int position) {
            return provinces.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Province item = getItem(position);
            holder.textView.setText(item.name);

            boolean checked = provinceIndex != INDEX_INVALID && provinces.get(provinceIndex).id == item.id;
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

    private class CityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cities == null ? 0 : cities.size();
        }

        @Override
        public City getItem(int position) {
            return cities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            City item = getItem(position);
            holder.textView.setText(item.name);

            boolean checked = cityIndex != INDEX_INVALID && cities.get(cityIndex).id == item.id;
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

    private class CountyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return counties == null ? 0 : counties.size();
        }

        @Override
        public County getItem(int position) {
            return counties.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            County item = getItem(position);
            holder.textView.setText(item.name);

            boolean checked = countyIndex != INDEX_INVALID && counties.get(countyIndex).id == item.id;
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

//    private class StreetAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return streets == null ? 0 : streets.size();
//        }
//
//        @Override
//        public Street getItem(int position) {
//            return streets.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return getItem(position).id;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            Holder holder;
//
//            if (convertView == null) {
//                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);
//
//                holder = new Holder();
//                holder.textView = (TextView) convertView.findViewById(R.id.textView);
//                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);
//
//                convertView.setTag(holder);
//            } else {
//                holder = (Holder) convertView.getTag();
//            }
//
//            Street item = getItem(position);
//            holder.textView.setText(item.name);
//
//            boolean checked = streetIndex != INDEX_INVALID && streets.get(streetIndex).id == item.id;
//            holder.textView.setEnabled(!checked);
//            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);
//
//            return convertView;
//        }
//
//        class Holder {
//            TextView textView;
//            ImageView imageViewCheckMark;
//        }
//    }

    public OnAddressSelectedListener getOnAddressSelectedListener() {
        return listener;
    }

    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        this.listener = listener;
    }

    public void setAddressProvider(AddressProvider addressProvider) {
        this.addressProvider = addressProvider;
        if (addressProvider == null) {
            this.addressProvider = DEFAULT_ADDRESS_PROVIDER;
        }

        retrieveProvinces();
    }

}
