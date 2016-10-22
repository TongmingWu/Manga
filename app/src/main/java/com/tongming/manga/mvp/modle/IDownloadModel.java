package com.tongming.manga.mvp.modle;

import com.tongming.manga.server.DownloadInfo;

/**
 * Author: Tongming
 * Date: 2016/9/7
 */

public interface IDownloadModel {

    /**
     * ComicDetailActivity和SelectActivity页面使用
     */
    void queryDownloadInfo(String name, int status);

    void queryDownloadInfo(String cid);

    /**
     * 下载详情页面使用
     */
    void queryDownloadInfo(DownloadInfo info);

    /**
     * 下载管理页面使用
     */
    void queryAllDownloadInfo();

}
