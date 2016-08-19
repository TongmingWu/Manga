package com.tongming.manga.mvp.bean;

import java.util.List;

/**
 * Created by Tongming on 2016/8/9.
 */
public class Hot {
    private ResultBean result;
    /**
     * result : {"hot":[{"cover":"http://tkres.tuku.cc/cimg/2013/11469.jpg","comic_url":"/comic/11469/","newest_chapter":"第153话 漫长的一天","newest_chapter_url":"/comic/11469/n-1467606783-78273/","comic_name":"穿越西元3000后"},{"cover":"http://tkres.tuku.cc/images/upload/20140924/14115407934497.jpg","comic_url":"/comic/15170/","newest_chapter":"第66话 新秀拟战赛（3）","newest_chapter_url":"/comic/15170/n-1467697863-92937/","comic_name":"风起苍岚"},{"cover":"http://tkres.tuku.cc/cimg/2013/11489.jpg","comic_url":"/comic/11489/","newest_chapter":"第84话 魔神之子","newest_chapter_url":"/comic/11489/n-1469189881-41261/","comic_name":"神印王座"},{"cover":"http://tkres.tuku.cc/images/upload/20140916/14108544133743.jpg","comic_url":"/comic/15057/","newest_chapter":"第50话 金轮裂灵阵","newest_chapter_url":"/comic/15057/n-1467642123-48078/","comic_name":"大主宰"},{"cover":"http://tkres.tuku.cc/cimg/2013/11460.jpg","comic_url":"/comic/11460/","newest_chapter":"第459话 未来再见（大结局）","newest_chapter_url":"/comic/11460/n-1446693782-71677/","comic_name":"偷星九月天"},{"cover":"http://tkres.tuku.cc/cimg/2010/dldl.jpg","comic_url":"/comic/24/","newest_chapter":"第161话 四元素学院（上）","newest_chapter_url":"/comic/24/n-1468239841-10520/","comic_name":"斗罗大陆(漫画版)"},{"cover":"http://tkres.tuku.cc/images/upload/20140924/14115400545185.jpg","comic_url":"/comic/15169/","newest_chapter":"第119话 暂时的分别","newest_chapter_url":"/comic/15169/n-1467606902-43748/","comic_name":"寻找前世之旅"},{"cover":"http://tkres.tuku.cc/images/upload/20150801/14384422374739.jpg","comic_url":"/comic/17882/","newest_chapter":"第72话 破囚笼","newest_chapter_url":"/comic/17882/n-1464969002-98667/","comic_name":"斗罗大陆2绝世唐门"},{"cover":"http://tkres.tuku.cc/cimg/2013/11438.jpg","comic_url":"/comic/11438/","newest_chapter":"第167话 师徒恩情","newest_chapter_url":"/comic/11438/n-1469785022-76055/","comic_name":"斗破苍穹"}],"release":[{"cover":"http://tkres.tuku.cc/images/upload/20160715/14685468783851.JPG","comic_url":"/comic/21288/","newest_chapter":"第3话","newest_chapter_url":"/comic/21288/n-1470812282-55114/","comic_name":"今天也是晴天"},{"cover":"http://tkres.tuku.cc/images/upload/20160706/14678082888662.jpg","comic_url":"/comic/21239/","newest_chapter":"第21话","newest_chapter_url":"/comic/21239/n-1470812282-82214/","comic_name":"浪漫果味C-2"},{"cover":"http://tkres.tuku.cc/images/upload/20160521/14638223833036.jpg","comic_url":"/comic/20830/","newest_chapter":"第23话","newest_chapter_url":"/comic/20830/n-1470812282-42391/","comic_name":"武拳"},{"cover":"http://tkres.tuku.cc/images/upload/20150805/14387896175176.jpg","comic_url":"/comic/17938/","newest_chapter":"第32话","newest_chapter_url":"/comic/17938/n-1470812282-10167/","comic_name":"歪小子斯科特"},{"cover":"http://tkres.tuku.cc/images/upload/20140703/14043794975431.jpg","comic_url":"/comic/14270/","newest_chapter":"第6话","newest_chapter_url":"/comic/14270/n-1470812282-35920/","comic_name":"我就是无法说不要"},{"cover":"http://tkres.tuku.cc/images/upload/20160605/14651210675295.jpg","comic_url":"/comic/21039/","newest_chapter":"第8话","newest_chapter_url":"/comic/21039/n-1470812221-66907/","comic_name":"女子研"},{"cover":"http://tkres.tuku.cc/images/upload/20160528/14644217641398.jpg","comic_url":"/comic/20906/","newest_chapter":"第57话","newest_chapter_url":"/comic/20906/n-1470812221-61852/","comic_name":"祈灵"},{"cover":"","comic_url":"/comic/20841/","newest_chapter":"第23话","newest_chapter_url":"/comic/20841/n-1470812221-89162/","comic_name":"我的霸道萝莉"},{"cover":"http://tkres.tuku.cc/images/upload/20160121/14533823754265.jpg","comic_url":"/comic/19816/","newest_chapter":"第8话","newest_chapter_url":"/comic/19816/n-1470812221-80110/","comic_name":"喰姬"}],"hongkong":[{"cover":"http://tkres.tuku.cc/images/upload/20150201/14227939617693.jpg","comic_url":"/comic/16359/","newest_chapter":"第28话上","newest_chapter_url":"/comic/16359/n-1470549362-87984/","comic_name":"唐三葬"},{"cover":"http://tkres.tuku.cc/images/upload/20160729/14697758388557.jpg","comic_url":"/comic/21352/","newest_chapter":"第1话","newest_chapter_url":"/comic/21352/n-1469775844-61581/","comic_name":"梦游仙境"},{"cover":"http://tkres.tuku.cc/cimg/2012/xtjzh.jpg","comic_url":"/comic/8975/","newest_chapter":"第229话","newest_chapter_url":"/comic/8975/n-1469772815-74438/","comic_name":"新铁将纵横"},{"cover":"http://tkres.tuku.cc/cimg/2012/xinlong.jpg","comic_url":"/comic/32/","newest_chapter":"第843话","newest_chapter_url":"/comic/32/n-1469771626-17935/","comic_name":"新著龙虎门"},{"cover":"http://tkres.tuku.cc/cimg/guhuozai.jpg","comic_url":"/comic/14/","newest_chapter":"第1939话","newest_chapter_url":"/comic/14/n-1469771049-40747/","comic_name":"古惑仔"},{"cover":"http://tkres.tuku.cc/cimg/2012/huowuyaoyang.jpg","comic_url":"/comic/10527/","newest_chapter":"第870话","newest_chapter_url":"/comic/10527/n-1469770662-11467/","comic_name":"火武耀扬"},{"cover":"http://tkres.tuku.cc/cimg/2012/chunqiuz.jpg","comic_url":"/comic/40/","newest_chapter":"第340话","newest_chapter_url":"/comic/40/n-1469769285-10533/","comic_name":"春秋战雄"},{"cover":"http://tkres.tuku.cc/images/upload/20150615/14343420603736.jpg","comic_url":"/comic/17504/","newest_chapter":"第55话","newest_chapter_url":"/comic/17504/n-1469768901-93522/","comic_name":"西游"},{"cover":"http://tkres.tuku.cc/images/upload/20160712/14682534234306.jpg","comic_url":"/comic/21266/","newest_chapter":"短篇","newest_chapter_url":"/comic/21266/n-1468253457-49773/","comic_name":"RINSACHIIIYONE..."}],"update":[{"cover":"http://tkres.tuku.cc/images/upload/20151223/14508569642744.jpg","comic_url":"/comic/19446/","newest_chapter":"第63话","newest_chapter_url":"/comic/19446/n-1470812162-30412/","comic_name":"颜值恋"},{"cover":"http://tkres.tuku.cc/images/upload/20160110/14523978868538.jpg","comic_url":"/comic/19690/","newest_chapter":"第106话","newest_chapter_url":"/comic/19690/n-1470812162-37056/","comic_name":"人皮衣裳"},{"cover":"http://tkres.tuku.cc/images/upload/20160529/14645208709577.jpg","comic_url":"/comic/20927/","newest_chapter":"第28话","newest_chapter_url":"/comic/20927/n-1470812103-12852/","comic_name":"王爷你好贱"},{"cover":"http://tkres.tuku.cc/images/upload/20160518/14635570775058.jpg","comic_url":"/comic/20787/","newest_chapter":"第99话","newest_chapter_url":"/comic/20787/n-1470810123-87311/","comic_name":"哥变成魔法少女了？！"},{"cover":"http://tkres.tuku.cc/images/upload/20160721/14690680534910.jpg","comic_url":"/comic/21336/","newest_chapter":"第11话","newest_chapter_url":"/comic/21336/n-1470810062-30532/","comic_name":"声色深处"},{"cover":"http://tkres.tuku.cc/images/upload/20151031/14462653439333.jpg","comic_url":"/comic/18954/","newest_chapter":"第55话","newest_chapter_url":"/comic/18954/n-1470810062-51376/","comic_name":"百炼成神"},{"cover":"http://tkres.tuku.cc/images/upload/20151226/14511194473104.jpg","comic_url":"/comic/19498/","newest_chapter":"第252话","newest_chapter_url":"/comic/19498/n-1470801122-55057/","comic_name":"致幻毁灭者"},{"cover":"http://tkres.tuku.cc/images/upload/20160104/14518776955318.jpg","comic_url":"/comic/19589/","newest_chapter":"第125话","newest_chapter_url":"/comic/19589/n-1470801062-87509/","comic_name":"陈官快递"},{"cover":"http://tkres.tuku.cc/images/upload/20160412/14604729245374.jpg","comic_url":"/comic/20404/","newest_chapter":"第42话","newest_chapter_url":"/comic/20404/n-1470795242-88938/","comic_name":"无罪之城"},{"cover":"http://tkres.tuku.cc/images/upload/20141125/14169048606491.jpg","comic_url":"/comic/15781/","newest_chapter":"第103话","newest_chapter_url":"/comic/15781/n-1470795241-47496/","comic_name":"我为苍生那些年"},{"cover":"http://tkres.tuku.cc/images/upload/20160502/14621989426909.jpg","comic_url":"/comic/20588/","newest_chapter":"第7话","newest_chapter_url":"/comic/20588/n-1470795182-97047/","comic_name":"咖啡遇上香草"},{"cover":"http://tkres.tuku.cc/images/upload/20150926/14432819801796.jpg","comic_url":"/comic/18530/","newest_chapter":"第203话","newest_chapter_url":"/comic/18530/n-1470795124-29745/","comic_name":"一人之下（异人）"}]}
     * code : 200
     * message : 操作成功
     * banner : [{"comic_url":"http://www.tuku.cc/comic/20000/","img":"http://tkres.tuku.cc/images/upload/20160410/14602915361532.jpg","title":"妖神记"},{"comic_url":"http://www.tuku.cc/comic/17504/n-1447317694-86588/","img":"http://tkres.tuku.cc/images/upload/20151113/14474259204722.jpg","title":"《西游》第22话 忘我"},{"comic_url":"http://www.tuku.cc/comic/40/n-1447318301-87132/","img":"http://tkres.tuku.cc/images/upload/20151113/14474258709516.jpg","title":"《春秋战雄》第304话 明鬼战罪神"}]
     */

    private int code;
    private String message;
    /**
     * comic_url : http://www.tuku.cc/comic/20000/
     * img : http://tkres.tuku.cc/images/upload/20160410/14602915361532.jpg
     * title : 妖神记
     */

    private List<BannerBean> banner;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public static class ResultBean {

        private List<ComicCard> hot;

        private List<ComicCard> release;

        private List<ComicCard> hongkong;

        private List<ComicCard> update;

        public List<ComicCard> getHot() {
            return hot;
        }

        public void setHot(List<ComicCard> hot) {
            this.hot = hot;
        }

        public List<ComicCard> getRelease() {
            return release;
        }

        public void setRelease(List<ComicCard> release) {
            this.release = release;
        }

        public List<ComicCard> getHongkong() {
            return hongkong;
        }

        public void setHongkong(List<ComicCard> hongkong) {
            this.hongkong = hongkong;
        }

        public List<ComicCard> getUpdate() {
            return update;
        }

        public void setUpdate(List<ComicCard> update) {
            this.update = update;
        }

    }

    public static class BannerBean {
        private String comic_url;
        private String img;
        private String title;

        public String getComic_url() {
            return comic_url;
        }

        public void setComic_url(String comic_url) {
            this.comic_url = comic_url;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
