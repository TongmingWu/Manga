package com.tongming.manga.mvp.base;

import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.mvp.download.DownloadManager;
import com.tongming.manga.util.CommonUtil;

/**
 * Author: Tongming
 * Date: 2016/10/26
 */

public class BaseModel {
    protected DBManager manager;

    public void closeDB() {
        if (!CommonUtil.isServiceStarted(BaseApplication.getContext(), DownloadManager.class.getName())) {
            manager.closeDB();
        }
    }
}
