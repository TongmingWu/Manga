package com.tongming.manga.mvp.modle;

import rx.Subscription;

/**
 * Created by Tongming on 2016/9/3.
 */
public interface ICacheModel {
    Subscription clearCache();

    Subscription calculateSize();
}
