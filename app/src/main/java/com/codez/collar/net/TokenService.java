package com.codez.collar.net;


import com.codez.collar.bean.TokenResultBean;

import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public interface TokenService {

    @POST
    Observable<TokenResultBean> getAccessToken(@Url String url);

}
