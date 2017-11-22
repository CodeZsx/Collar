package com.codez.collar.bean;

import android.databinding.BaseObservable;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class GeoBean extends BaseObservable{
    /** 经度坐标 */
    public String longitude;
    /** 维度坐标 */
    public String latitude;
    /** 所在城市的城市代码 */
    public String city;
    /** 所在省份的省份代码 */
    public String province;
    /** 所在城市的城市名称 */
    public String city_name;
    /** 所在省份的省份名称 */
    public String province_name;
    /** 所在的实际地址，可以为空 */
    public String address;
    /** 地址的汉语拼音，不是所有情况都会返回该字段 */
    public String pinyin;
    /** 更多信息，不是所有情况都会返回该字段 */
    public String more;
}
