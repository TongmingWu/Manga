package com.tongming.manga.mvp.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
 * Author: Tongming
 * Date: 2016/9/23
 */

public class RVComicAdapter extends RecyclerView.Adapter<RVComicAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    public static final int NORMAL_COMIC = 0;
    public static final int HISTORY_COMIC = 1;
    public static final int COLLECTION_COMIC = 2;
    public static final int DOWNLOAD_COMIC = 3;
    private List<ComicCard> comicList;
    private List<HistoryComic> historyComics;
    private List<CollectedComic> collectedComics;
    private List<DownloadComic> downloadComicList;
    private List<?> list;
    private int type;
    private Context mContext;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public RVComicAdapter(List<?> list, Context mContext, int type) {
        this.list = list;
        this.type = type;
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
    public RVComicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(mContext, R.layout.item_comic, null);
        inflate.setOnClickListener(this);
        inflate.setOnLongClickListener(this);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RVComicAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(R.id.position, position);
        holder.itemView.setTag(R.id.comic, list.get(position));
        switch (type) {
            case NORMAL_COMIC:
                ComicCard bean = comicList.get(position);
                String cover = bean.getCover();
                if (!TextUtils.isEmpty(cover)) {
                    HeaderGlide.loadImage(mContext, bean.getCover(), holder.cover, bean.getComic_source());
                }
                holder.comicName.setText(bean.getComic_name());
                if ("".equals(bean.getNewest_chapter())) {
                    holder.chapterName.setText(bean.getComic_author().trim());
                } else {
                    holder.chapterName.setText(bean.getNewest_chapter());
                }
                break;
            case HISTORY_COMIC:
                HistoryComic comic = historyComics.get(position);
                if (!TextUtils.isEmpty(comic.getCover())) {
                    HeaderGlide.loadImage(mContext, comic.getCover(), holder.cover, comic.getComic_source());
                }
                holder.comicName.setText(comic.getName());
                holder.chapterName.setText(comic.getHistoryName());
                break;
            case COLLECTION_COMIC:
                CollectedComic collectedComic = collectedComics.get(position);
                if (!TextUtils.isEmpty(collectedComic.getCover())) {
                    HeaderGlide.loadImage(mContext, collectedComic.getCover(), holder.cover, collectedComic.getComic_source());
                }
                holder.comicName.setText(collectedComic.getComic_name());
                holder.chapterName.setText(collectedComic.getComic_author());
                break;
            case DOWNLOAD_COMIC:
                DownloadComic downloadComic = downloadComicList.get(position);
                if (!TextUtils.isEmpty(downloadComic.getCover())) {
                    HeaderGlide.loadImage(mContext, downloadComic.getCover(), holder.cover, downloadComic.getComic_source());
                }
                holder.comicName.setText(downloadComic.getName());
                holder.comicName.setTextSize(15);
                holder.comicName.setTextColor(mContext.getResources().getColor(R.color.normalText, null));
                switch (downloadComic.getStatus()) {
                    case DownloadTaskQueue.DOWNLOAD:
                        holder.rlDownload.setVisibility(View.VISIBLE);
//                    holder.chapterName.setText("下载中");
                        holder.chapterName.setText(downloadComic.getCurrentName() + " " + downloadComic.getCurrentPosition() + "/" + downloadComic.getCurrentTotal());
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
                        holder.chapterName.setText("已完成(" + downloadComic.getCount() + ")");
                        break;
                }
                holder.chapterName.setTextColor(Color.GRAY);
                holder.chapterName.setTextSize(12);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, (Integer) v.getTag(R.id.position), v.getTag(R.id.comic));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return onItemLongClickListener != null &&
                onItemLongClickListener.onItemLongClick(v, (Integer) v.getTag(R.id.position), v.getTag(R.id.comic));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
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
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void updateItem(DownloadInfo info) {
            if (rlDownload.getVisibility() == View.GONE) {
                rlDownload.setVisibility(View.VISIBLE);
            }
            chapterName.setText(info.getChapter_name() + " " + info.getPosition() + "/" + info.getTotal());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Object object);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position, Object object);
    }
}
