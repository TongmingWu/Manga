package com.tongming.manga.mvp.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.mvp.bean.ComicInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tongming on 2016/8/11.
 */
public class ChapterAdapter extends BaseAdapter {

    private List<ComicInfo.ChapterListBean> chapterList;
    private Context mContext;
    private int changePos;

    public ChapterAdapter(List<ComicInfo.ChapterListBean> chapterList, int changePos, Context mContext) {
        this.chapterList = chapterList;
        this.mContext = mContext;
        this.changePos = changePos;
    }

    @Override
    public int getCount() {
        return chapterList.size();
    }

    @Override
    public Object getItem(int position) {
        return chapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_chapter, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == changePos) {
            holder.tvChapter.setBackgroundResource(R.drawable.chatper_select);
            Logger.d("改变颜色");
        }
        String title = chapterList.get(position).getChapter_title();
        holder.tvChapter.setText(title);
        return convertView;
    }

    class ViewHolder {
        private View itemView;
        @BindView(R.id.tv_item_chapter_name)
        TextView tvChapter;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
