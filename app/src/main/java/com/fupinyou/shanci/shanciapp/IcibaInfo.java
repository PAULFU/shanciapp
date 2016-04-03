package com.fupinyou.shanci.shanciapp;

import java.util.List;

/**
 * Created by fupinyou on 2016/4/3.
 */
public class IcibaInfo {
    /**
     * sid : 1933
     * tts : http://news.iciba.com/admin/tts/2016-04-03-day.mp3
     * content : The more we do, the more we can do. The busier we are, the more leisure we have.
     * note : 事越做越会做，人越忙越有空。
     * love : 5190
     * translation : 词霸小编：#来自一位不愿意透露ID的小伙伴~这句投稿来自一位不愿意透露ID的小伙伴。这句经典的话来自，英国批判家、散文家 威廉.哈兹里特。看到这句话让小编想起来鲁迅先生的一句话：“时间就像海绵里的水，挤挤总是有的。”脑子越用越灵活，越忙学的东西越多效率越来越高。希望大家都能有充实美好的一天又一天！
     * picture : http://cdn.iciba.com/news/word/2016-04-03.jpg
     * picture2 : http://cdn.iciba.com/news/word/big_2016-04-03b.jpg
     * caption : 词霸每日一句
     * dateline : 2016-04-03
     * s_pv : 9098
     * sp_pv : 364
     * tags : [{"id":"10","name":"正能量"}]
     * fenxiang_img : http://cdn.iciba.com/web/news/longweibo/imag/2016-04-03.jpg
     */

    private String sid;
    private String tts;
    private String content;
    private String note;
    private String love;
    private String translation;
    private String picture;
    private String picture2;
    private String caption;
    private String dateline;
    private String s_pv;
    private String sp_pv;
    private String fenxiang_img;
    /**
     * id : 10
     * name : 正能量
     */

    private List<TagsBean> tags;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getS_pv() {
        return s_pv;
    }

    public void setS_pv(String s_pv) {
        this.s_pv = s_pv;
    }

    public String getSp_pv() {
        return sp_pv;
    }

    public void setSp_pv(String sp_pv) {
        this.sp_pv = sp_pv;
    }

    public String getFenxiang_img() {
        return fenxiang_img;
    }

    public void setFenxiang_img(String fenxiang_img) {
        this.fenxiang_img = fenxiang_img;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
