package com.dmy.farming.view.addressselector;

import com.dmy.farming.view.addressselector.model.City;
import com.dmy.farming.view.addressselector.model.County;
import com.dmy.farming.view.addressselector.model.Province;

public interface OnAddressSelectedListener {
    void onAddressSelected(Province province, City city, County county/*, Street street*/);
}
