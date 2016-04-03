package com.fupinyou.shanci.shanciapp;

import java.util.List;

/**
 * Created by fupinyou on 2016/4/3.
 */
public class YouDaoSearchData {
    /**
     * phonetic : fān yì
     * explains : ["translate","interpret"]
     */

    private BasicBean basic;
    /**
     * translation : ["translation"]
     * basic : {"phonetic":"fān yì","explains":["translate","interpret"]}
     * query : 翻译
     * errorCode : 0
     * web : [{"value":["Translation","translate","Translator"],"key":"翻译"},{"value":["machine translation","機械翻訳","기계 번역"],"key":"机器翻译"},{"value":["Mémoire de traduction","Translation memory","翻訳メモリ"],"key":"翻译记忆"}]
     */

    private String query;
    private int errorCode;
    private List<String> translation;
    /**
     * value : ["Translation","translate","Translator"]
     * key : 翻译
     */

    private List<WebBean> web;

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public List<WebBean> getWeb() {
        return web;
    }

    public void setWeb(List<WebBean> web) {
        this.web = web;
    }

    public static class BasicBean {
        private String phonetic;
        private List<String> explains;

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }
    }

    public static class WebBean {
        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }
}
