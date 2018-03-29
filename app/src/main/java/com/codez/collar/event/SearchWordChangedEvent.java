package com.codez.collar.event;

/**
 * Created by codez on 2018/3/25.
 * Description:
 */

public class SearchWordChangedEvent {
    private String searchWord;

    public SearchWordChangedEvent(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }
}
