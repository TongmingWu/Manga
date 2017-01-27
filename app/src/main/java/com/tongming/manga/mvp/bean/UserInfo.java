package com.tongming.manga.mvp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tongming on 2016/8/30.
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -21455356667888L;
    /**
     * message : 获取用户信息成功
     * code : 200
     * user : {"phone":"18819203492","name":"M18819203492","collection":[{"area":"日本","category":"搞笑","last_time":1472521664,"name":"蜡笔小新","cover":"","status":0,"url":"","author":"未知"}],"logon_date":1472434440,"uid":"1","sex":"male","avatar":null}
     */

    private String message;
    private int code;
    /**
     * phone : 18819203492
     * name : M18819203492
     * collection : [{"area":"日本","category":"搞笑","last_time":1472521664,"name":"蜡笔小新","cover":"","status":0,"url":"","author":"未知"}]
     * logon_date : 1472434440
     * uid : 1
     * sex : male
     * avatar : null
     */

    private UserBean user;

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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean implements Serializable {
        private String phone;
        private String name;
        private int logon_date;
        private int uid;
        private String sex;
        private String avatar;
        private String personality;
        /**
         * area : 日本
         * category : 搞笑
         * last_time : 1472521664
         * name : 蜡笔小新
         * cover :
         * status : 0
         * url :
         * author : 未知
         */

        private List<CollectedComic> collection;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLogon_date() {
            return logon_date;
        }

        public void setLogon_date(int logon_date) {
            this.logon_date = logon_date;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setPersonality(String personality) {
            this.personality = personality;
        }

        public String getPersonality() {

            return personality;
        }

        public List<CollectedComic> getCollection() {
            return collection;
        }

        public void setCollection(List<CollectedComic> collection) {
            this.collection = collection;
        }

        @Override
        public String toString() {
            return "UserBean{" +
                    "phone='" + phone + '\'' +
                    ", name='" + name + '\'' +
                    ", logon_date=" + logon_date +
                    ", uid=" + uid +
                    ", sex='" + sex + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", personality='" + personality + '\'' +
                    ", collection=" + collection +
                    '}';
        }
    }
}
