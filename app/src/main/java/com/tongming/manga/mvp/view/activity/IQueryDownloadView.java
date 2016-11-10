package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.server.DownloadInfo;

import java.util.List;

/**
 * Author: Tongming
 * Date: 2016/9/8
 */

public interface IQueryDownloadView {
    void onQueryDownloadInfo(List<DownloadInfo> infoList);

    void onQueryDownloadInfo(ComicPage page);

    void onFail(Throwable throwable);
}
