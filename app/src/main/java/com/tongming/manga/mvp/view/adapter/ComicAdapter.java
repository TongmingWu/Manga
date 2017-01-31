package com.tongming.manga.mvp.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongming.manga.R;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.ComicCard;
import com.tongming.manga.mvp.bean.DownloadComic;
import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.download.DownloadTaskQueue;
import com.tongming.manga.server.DownloadInfo;
import com.tongming.manga.util.HeaderGlide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tongming on 2016/8/10.
 */
public class ComicAdapter extends BaseAdapter {

    public static final int NORMAL_COMIC = 0;
    public static final int HISTORY_COMIC = 1;
    public static final int COLLECTION_COMIC = 2;
    public static final int DOWNLOAD_COMIC = 3;
    private List<ComicCard> comicList;
    private List<HistoryComic> historyComics;
    private List<CollectedComic> collectedComics;
    private List<DownloadComic> downloadComicList;
    private List<?> list;
    private Context mContext;

    public ComicAdapter(List<?> list, Context mContext, int type) {
        this.list = list;
        switch (type) {
            case NORMAL_COMIC:
                comicList = (List<ComicCard>) this.list;
                break;
            case HISTORY_COMIC:
                historyComics = (List<HistoryComic>) this.list;
                break;
            case COLLECTION_COMIC:
                collectedComics = (List<CollectedComic>) this.list;
                break;
            case DOWNLOAD_COMIC:
                downloadComicList = (List<DownloadComic>) this.list;
        }
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        /*if (list.size() > 6 && list.size() < 12 && comicList != null) {
            return 6;
        }*/
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setQueueStatus(String cid, int status) {
        if (downloadComicList != null) {
            for (DownloadComic comic : downloadComicList) {
                if (cid.equals(comic.getComic_id())) {
                    comic.setStatus(status);
                    break;
                }
            }
        }
    }

    public void setDownloadInfo(String cid, DownloadInfo info) {
        if (downloadComicList != null) {
            for (DownloadComic comic : downloadComicList) {
                if (cid.equals(comic.getComic_id())) {
                    comic.setCurrentName(info.getChapter_name());
                    comic.setCurrentPosition(info.getPosition());
                    comic.setCurrentTotal(info.getTotal());
                    break;
                }
            }
        }
    }

    public void removeQueue(String cid) {
        if (downloadComicList != null) {
            for (DownloadComic comic : downloadComicList) {
                if (cid.equals(comic.getComic_id())) {
                    downloadComicList.remove(comic);
                    break;
                }
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_comic, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (comicList != null) {
            ComicCard bean = comicList.get(position);
            String cover = bean.getCover();
            if (!TextUtils.isEmpty(cover)) {
                HeaderGlide.loadImage(mContext, bean.getCover(), holder.cover, bean.getComic_source());
            }
            holder.comicName.setText(bean.getComic_name());
            if ("".equals(bean.getNewest_chapter())) {
                /*holder.chapterName.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.comicName.getLayoutParams();
                params.bottomMargin = CommonUtil.dip2px(mContext, 3);*/
                holder.chapterName.setText(bean.getComic_author().trim());
            } else {
                holder.chapterName.setText(bean.getNewest_chapter());
            }
        } else if (historyComics != null) {
            HistoryComic comic = historyComics.get(position);
            if (!TextUtils.isEmpty(comic.getCover())) {
                HeaderGlide.loadImage(mContext, comic.getCover(), holder.cover, comic.getComic_source());
            }
            holder.comicName.setText(comic.getName());
            holder.chapterName.setText(comic.getHistoryName());
        } else if (collectedComics != null) {
            CollectedComic comic = collectedComics.get(position);
            if (!TextUtils.isEmpty(comic.getCover())) {
                HeaderGlide.loadImage(mContext, comic.getCover(), holder.cover, comic.getComic_source());
            }
            holder.comicName.setText(comic.getComic_name());
            holder.chapterName.setText(comic.getComic_author());
        } else {
            DownloadComic comic = downloadComicList.get(position);
            if (!TextUtils.isEmpty(comic.getCover())) {
                HeaderGlide.loadImage(mContext, comic.getCover(), holder.cover, comic.getComic_source());
            }
            /*Glide.with(mContext)
                    .load(comic.getCover())
                    .into(holder.cover);*/
            holder.comicName.setText(comic.getName());
            holder.comicName.setTextSize(15);
            holder.comicName.setTextColor(mContext.getResources().getColor(R.color.normalText, null));
            switch (comic.getStatus()) {
                case DownloadTaskQueue.DOWNLOAD:
                    holder.rlDownload.setVisibility(View.VISIBLE);
//                    holder.chapterName.setText("下载中");
                    holder.chapterName.setText(comic.getCurrentName() + " " + comic.getCurrentPosition() + "/" + comic.getCurrentTotal());
                    break;
                case DownloadTaskQueue.WAIT:
                    holder.rlDownload.setVisibility(View.GONE);
                    holder.chapterName.setText("等待中");
                    break;
                case DownloadTaskQueue.PAUSE:
                    holder.rlDownload.setVisibility(View.GONE);
                    holder.chapterName.setText("暂停中");
                    break;
                case DownloadTaskQueue.COMPLETE:
                    holder.rlDownload.setVisibility(View.GONE);
                    holder.chapterName.setText("已完成(" + comic.getCount() + ")");
                    break;
            }
            holder.chapterName.setTextColor(Color.GRAY);
            holder.chapterName.setTextSize(12);
        }
        return convertView;
    }

    class ViewHolder {
        View itemView;
        @BindView(R.id.iv_cover)
        ImageView cover;
        @BindView(R.id.tv_comic_name)
        TextView comicName;
        @BindView(R.id.tv_chapter_name)
        TextView chapterName;
        @BindView(R.id.rl_download)
        RelativeLayout rlDownload;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
