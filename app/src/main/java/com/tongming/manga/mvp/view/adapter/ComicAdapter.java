package com.tongming.manga.mvp.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongming.manga.R;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.ComicCard;
import com.tongming.manga.mvp.bean.HistoryComic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tongming on 2016/8/10.
 */
public class ComicAdapter extends BaseAdapter {

    public static final int NORAML_COMIC = 0;
    public static final int HISTORY_COMIC = 1;
    public static final int COLLECTION_COMIC = 2;
    private List<ComicCard> comicList;
    private List<HistoryComic> historyComics;
    private List<CollectedComic> collectedComics;
    private List<?> list;
    private Context mContext;

    public ComicAdapter(List<?> list, Context mContext, int type) {
        this.list = list;
        switch (type) {
            case 0:
                comicList = (List<ComicCard>) this.list;
                break;
            case 1:
                historyComics = (List<HistoryComic>) this.list;
                break;
            case 2:
                collectedComics = (List<CollectedComic>) this.list;
                break;
        }
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        if (list.size() > 6 && list.size() < 10 && comicList != null) {
            return 6;
        }
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
            Glide.with(mContext)
                    .load(bean.getCover())
                    .crossFade()
                    .into(holder.cover);
            holder.comicName.setText(bean.getComic_name());
            holder.chapterName.setText(bean.getNewest_chapter());
        } else if (historyComics != null) {
            HistoryComic comic = historyComics.get(position);
            Glide.with(mContext)
                    .load(comic.getCover())
                    .crossFade()
                    .into(holder.cover);
            holder.comicName.setText(comic.getName());
            holder.chapterName.setText(comic.getHistoryName());
        } else {
            CollectedComic comic = collectedComics.get(position);
            Glide.with(mContext)
                    .load(comic.getCover())
                    .crossFade()
                    .into(holder.cover);
            holder.comicName.setText(comic.getName());
            holder.chapterName.setText(comic.getAuthor());
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

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
