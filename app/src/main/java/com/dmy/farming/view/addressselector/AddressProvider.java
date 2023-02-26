package com.dmy.farming.view.addressselector;

import java.util.List;

import com.dmy.farming.view.addressselector.model.City;
import com.dmy.farming.view.addressselector.model.County;
import com.dmy.farming.view.addressselector.model.Province;
import com.dmy.farming.view.addressselector.model.Street;

public interface AddressProvider {
    void provideProvinces(AddressReceiver<Province> addressReceiver);
    void provideCitiesWith(int provinceId, AddressReceiver<City> addressReceiver);
    void provideCountiesWith(int cityId, AddressReceiver<County> addressReceiver);
    void provideStreetsWith(int countyId, AddressReceiver<Street> addressReceiver);

    interface AddressReceiver<T> {
        void send(List<T> data);
    }
}