package com.example.xiaoxiao.myapplication;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.xiaoxiao.myapplication.db.City;
import com.example.xiaoxiao.myapplication.db.Country;
import com.example.xiaoxiao.myapplication.db.Province;
import com.example.xiaoxiao.myapplication.utils.HttpUtil;
import com.example.xiaoxiao.myapplication.utils.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xiaoxiao on 2018/1/16.
 */

public class ChooseAreaFragment extends Fragment {

    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter ;
    private List<String> dataList =new ArrayList<>();
    private int currentLevel =0;
    private static final int LEVEL_PROVINCE =0;
    private static final int LEVEL_CITY =1;
    private static final int LEVEL_COUNTRY =2;
    private Province selectedProvince;
    private City selectedCity;
    private Country selectedCountry;
    private List<Province> provinceList ;
    private List<Country> countryList;
    private List<City> cityList;
    private ProgressDialog progressDialog;
    private String weatherId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.choose_area,container);
        titleText =(TextView)view.findViewById(R.id.title_text);
        backButton =(Button)view.findViewById(R.id.back_button);
        listView =(ListView)view.findViewById(R.id.list_view);
        adapter =new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("choose/xx","currentLevel: "+currentLevel);
                if(currentLevel ==LEVEL_PROVINCE){
                    selectedProvince =provinceList.get(position);
                    selectedProvince =provinceList.get(position);
                    queryCities();
                }else if(currentLevel ==LEVEL_CITY){
                    selectedCity =cityList.get(position);
                    queryCountries();
                }else if(currentLevel ==LEVEL_COUNTRY){
                    weatherId =countryList.get(position).getWeatherId();
                    if(getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        Log.d("choose/xx","weatherId: "+weatherId);
                        intent.putExtra("weather_id",weatherId);// countryList.get(position).getWeatherId());
                        startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity() instanceof WeatherActivity){
                        WeatherActivity weatherActivity =(WeatherActivity)getActivity();
                        weatherActivity.drawerLayout.closeDrawer(Gravity.LEFT);
                        weatherActivity.swipeRefreshLayout.setRefreshing(true);
                        weatherActivity.requestWeatner(weatherId);
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel ==LEVEL_COUNTRY){
                    queryCities();
                }else if(currentLevel ==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    public void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province :provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel =LEVEL_PROVINCE;
        }else{
            String address ="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    public void queryFromServer(String address, final String type){
        showProgressDialog();
        HttpUtil.sendRequestWithOkhttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
             getActivity().runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     Log.d("choose/xxx","response: ");
                     closeProgressDialog();
                 }
             });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              String responseText =response.body().string();
                Log.d("choose/xxx","response: "+responseText);
              boolean result =false;
              if("province".equals(type)){
                  result =Utility.handleProvinceResponse(responseText);
              }else if("city".equals(type)){
                  result =Utility.handleCityResponse(responseText,selectedProvince.getId());
              }else if("country".equals(type)){
                  result =Utility.handleCountryResponse(responseText,selectedCity.getId());
              }
                Log.d("choose/xxx","result: "+result + " type: "+type);
              if(result){
                  closeProgressDialog();
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          if("province".equals(type)){
                              queryProvinces();
                          }else if("city".equals(type)){
                              queryCities();
                          }else if("country".equals(type)){
                              queryCountries();
                          }
                      }
                  });
              }
            }
        });
    }
    public void closeProgressDialog(){
        if(progressDialog !=null){
            progressDialog.dismiss();
        }
    }
    public void showProgressDialog(){
        progressDialog =new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载中。。。");
        progressDialog.setCanceledOnTouchOutside(false);
    }
    public void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList =DataSupport.where("provinceid= ?",String.valueOf(selectedProvince.getId())).find(City.class);
        Log.d("choose/xx","cityList: "+cityList.size());
        if(cityList.size()>0){
            dataList.clear();
            for(City city :cityList){
                Log.d("choose:xx","name: "+city.getCityName());
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel =LEVEL_CITY;
        }else{
            Log.d("choose/xx","provinceCode: "+selectedProvince.getProvinceCode());
            queryFromServer("http://guolin.tech/api/china/"+selectedProvince.getProvinceCode(),"city");
        }
    }
    public void queryCountries(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countryList =DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(Country.class);
        if(countryList.size()>0){
            dataList.clear();
            for(Country country :countryList){
                dataList.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel =LEVEL_COUNTRY;
        }else {
            queryFromServer("http://guolin.tech/api/china/"+selectedProvince.getProvinceCode()+"/"+selectedCity.getCityCode(),"country");
        }
    }
}
