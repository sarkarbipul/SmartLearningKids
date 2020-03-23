package com.logicaltriangle.skl.model;

import java.util.List;

public class WordResult {
    private boolean status;
    private List<Word> wordList;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Word> getWordList() {
        return wordList;
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
    }
}
