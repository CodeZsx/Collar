package com.codez.collar.net;

import com.codez.collar.bean.UpgradeInfoBean;
import com.codez.collar.utils.Constants;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/**
 * Created by codez on 2018/4/2.
 * Description:
 */

public interface UpgradeService {

    @Headers({"Domain-Name: " + Constants.GITHUB_DOMAIN_NAME})
    @GET("upgrade.json")
    Observable<UpgradeInfoBean> getUpgradeInfo();

}
