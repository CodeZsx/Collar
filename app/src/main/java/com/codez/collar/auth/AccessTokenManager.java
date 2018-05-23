package com.codez.collar.auth;

import android.content.Context;

import com.codez.collar.bean.Token;
import com.codez.collar.bean.TokenList;
import com.codez.collar.utils.SDCardUtil;
import com.google.gson.Gson;

/**
 * Created by codez on 2017/11/19.
 * Description:
 */

public class AccessTokenManager {
    private static final String TOKENLIST_CACHE_DIR = SDCardUtil.getAppCachePath()+"/";
    private static final String TOKENLIST_CACHE_NAME = "tokenlistcache.txt";
    public void addToken(Context context, String accessToken, String expiresIn, String refreshToken, String uid) {
        Gson gson = new Gson();
        Token token = new Token(accessToken, expiresIn, refreshToken, uid);
        TokenList tokenList = TokenList.parse(SDCardUtil.get(context,
                TOKENLIST_CACHE_DIR, TOKENLIST_CACHE_NAME));
        if (tokenList == null || tokenList.getTokenList().size() == 0) {
            tokenList = new TokenList();
        }
        //重复登录的话，不进行重复token的添加
        for (int i = 0; i < tokenList.getTokenList().size(); i++) {
            if (tokenList.getTokenList().get(i).getUid().equals(uid)) {
                updateAccessToken(context, accessToken, expiresIn, refreshToken, uid);
                return;
            }
        }
        tokenList.addTokenList(token);
        tokenList.setCurrUid(uid);
        SDCardUtil.put(context,TOKENLIST_CACHE_DIR,
                TOKENLIST_CACHE_NAME,gson.toJson(tokenList));
        updateAccessToken(context, accessToken, expiresIn, refreshToken, uid);
    }

    public TokenList getTokenList(Context context) {
        return TokenList.parse(SDCardUtil.get(context,
                TOKENLIST_CACHE_DIR, TOKENLIST_CACHE_NAME));
    }

    public void deleteToken(Context context, String uid) {
        Gson gson = new Gson();
        TokenList tokenList = TokenList.parse(SDCardUtil.get(context,
                TOKENLIST_CACHE_DIR,TOKENLIST_CACHE_NAME));
        for (int i = 0; i< tokenList.getTokenList().size();i++) {
            if (tokenList.getTokenList().get(i).getUid().equals(uid)) {
                tokenList.removeTokenList(i);
            }
        }
        //当前账户list为空
        if (tokenList.getTokenList().size() == 0) {
            SDCardUtil.put(context,TOKENLIST_CACHE_DIR,
                    TOKENLIST_CACHE_NAME,gson.toJson(tokenList));
            AccessTokenKeeper.getInstance().clear();//移除当前使用账户
            return;
        }
        //当前登录账户为删除账户
        if (AccessTokenKeeper.getInstance().readAccessToken().getUid().equals(uid)) {
            AccessTokenKeeper.getInstance().clear();
            //当前登录账户被删除后，使用list中的第一个账户
            Token token = tokenList.getTokenList().get(0);
            tokenList.setCurrUid(tokenList.getTokenList().get(0).getUid());
            updateAccessToken(context, token.getToken(),
                    token.getExpiresIn(),
                    token.getRefreshToken(),
                    token.getUid());
        }
        //其他情况，即删除账户不为当前账户:无操作
        SDCardUtil.put(context,TOKENLIST_CACHE_DIR,
                TOKENLIST_CACHE_NAME,gson.toJson(tokenList));
    }

    public void switchToken(Context context, String uid) {
        TokenList tokenList = TokenList.parse(SDCardUtil.get(context,
                TOKENLIST_CACHE_DIR, TOKENLIST_CACHE_NAME));
        for (int i = 0; i < tokenList.getTokenList().size(); i++) {
            if (tokenList.getTokenList().get(i).getUid().equals(uid)) {
                Token token = tokenList.getTokenList().get(i);
                updateAccessToken(context, token.getToken(),
                        token.getExpiresIn(),
                        token.getRefreshToken(),
                        token.getUid());
            }
        }
    }

    public void switchToken(Context context, int positionInCache, OnTokenSwitchListener onTokenSwitchListener) {
        TokenList tokenList = TokenList.parse(SDCardUtil.get(context,
                TOKENLIST_CACHE_DIR, TOKENLIST_CACHE_NAME));
        if (tokenList.getTokenList().size() > 0) {
            Token token = tokenList.getTokenList().get(positionInCache);
            updateAccessToken(context, token.getToken(),
                    token.getExpiresIn(),
                    token.getRefreshToken(),
                    token.getUid());
            onTokenSwitchListener.onSuccess();
        }else {
            onTokenSwitchListener.OnError("切换失败，本地token缓存为空");
        }
    }

    public void updateAccessToken(Context context, String accessToken, String expiresIn, String refreshToken, String uid) {
        AccessTokenKeeper.getInstance().writeAccessToken(new Oauth2AccessToken(accessToken,
                Oauth2AccessToken.getExpiresInTime(expiresIn), refreshToken, uid));
    }
    public interface OnTokenSwitchListener{
        void onSuccess();

        void OnError(String error);
    }
}
