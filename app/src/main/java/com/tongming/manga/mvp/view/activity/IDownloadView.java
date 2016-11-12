package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.server.DownloadInfo;

import java.util.List;

/**
 * Author: Tongming
 * Date: 2016/9/8
 */

public interface IDownloadView {
    void onQueryDownloadInfo(List<DownloadInfo> infoList);

    void onQueryDownloadInfo(ComicPage page);

    void onDeleteDownloadInfo(int state);

    void onFail(Throwable throwable);
}

