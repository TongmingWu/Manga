package com.tongming.manga.mvp.bean;

import java.util.List;

/**
 * Author: Tongming
 * Date: 2016/10/12
 */

public class Category {

    /**
     * category : [{"title":"最近更新","cid":9,"cover":""},{"title":"热门连载","cid":7,"cover":""},{"title":"已完结","cid":10,"cover":""},{"title":"排行榜","cid":8,"cover":""},{"title":"国漫","cid":6,"cover":""},{"title":"日漫","cid":2,"cover":""},{"title":"韩漫","cid":3,"cover":""},{"title":"港漫","cid":1,"cover":""},{"title":"欧美","cid":5,"cover":""},{"title":"魔幻","cid":1,"cover":""},{"title":"格斗","cid":2,"cover":""},{"title":"竞技","cid":3,"cover":""},{"title":"少女","cid":4,"cover":""},{"title":"热血","cid":5,"cover":""},{"title":"萌系","cid":6,"cover":""},{"title":"搞笑","cid":7,"cover":""},{"title":"励志","cid":8,"cover":""},{"title":"恐怖","cid":9,"cover":""},{"title":"悬疑","cid":10,"cover":""},{"title":"科幻","cid":11,"cover":""},{"title":"游戏","cid":14,"cover":""},{"title":"武侠","cid":15,"cover":""},{"title":"耽美","cid":16,"cover":""},{"title":"青年","cid":17,"cover":""},{"title":"同人","cid":18,"cover":""},{"title":"美女","cid":19,"cover":""},{"title":"校园","cid":20,"cover":""},{"title":"轻小说","cid":21,"cover":""},{"title":"四格","cid":30,"cover":""},{"title":"美食","cid":31,"cover":""},{"title":"百合","cid":35,"cover":""},{"title":"猎奇","cid":39,"cover":""},{"title":"后宫","cid":40,"cover":""},{"title":"伪娘","cid":41,"cover":""},{"title":"节操","cid":42,"cover":""},{"title":"机战","cid":43,"cover":""},{"title":"福利","cid":82,"cover":""},{"title":"穿越","cid":83,"cover":""},{"title":"恋爱","cid":84,"cover":""},{"title":"治愈","cid":90,"cover":""},{"title":"生活","cid":93,"cover":""}]
     * message : 获取成功
     * code : 200
     */

    private String message;
    private int code;
    /**
     * title : 最近更新
     * cid : 9
     * cover :
     */

    private List<CategoryBean> category;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public static class CategoryBean {
        private String title;
        private int cid;
        private String cover;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }
}
