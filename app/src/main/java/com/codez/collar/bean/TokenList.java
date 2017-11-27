package com.codez.collar.bean;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by codez on 2017/11/19.
 * Description:
 */

public class TokenList {
    private ArrayList<Token> tokenList = new ArrayList<>();
    private String currUid;

    public static TokenList parse(String jsonString) {
        Gson gson = new Gson();
        TokenList tokenList = gson.fromJson(jsonString, TokenList.class);
        if (tokenList == null) {

        }
        return tokenList;
    }

    public ArrayList<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }
    public void addTokenList(Token token){
        this.tokenList.add(token);
    }

    public void removeTokenList(int index) {
        this.tokenList.remove(index);
    }

    public String getCurrUid() {
        return currUid;
    }

    public void setCurrUid(String currUid) {
        this.currUid = currUid;
    }

    @Override
    public String toString() {
        return "TokenList{" +
                "tokenList=" + tokenList +
                ", currUid='" + currUid + '\'' +
                '}';
    }
}
