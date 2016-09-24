package com.tongming.manga.mvp.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tongming.manga.R;
import com.tongming.manga.mvp.bean.ComicInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tongming on 2016/8/11.
 */
public class ChapterAdapter extends BaseAdapter {

    private List<ComicInfo.ChapterListBean> chapterList;
    private Context mContext;
    private int historyPos = -1;
    private List<Integer> downloadPos = new ArrayList<>();
    private List<Integer> selectionPos = new ArrayList<>();

    /**
     * 根据 historyPos 重绘item背景
     *
     * @param chapterList
     * @param historyPos
     * @param mContext
     */
    public ChapterAdapter(List<ComicInfo.ChapterListBean> chapterList, int historyPos, Context mContext) {
        this.chapterList = chapterList;
        this.mContext = mContext;
        this.historyPos = historyPos;
    }

    /**
     * 根据 downloadPos 重绘item背景
     *
     * @param chapterList
     * @param downloadPos
     * @param mContext
     */
    public ChapterAdapter(List<ComicInfo.ChapterListBean> chapterList, List<Integer> downloadPos, Context mContext) {
        this.chapterList = chapterList;
        this.downloadPos = downloadPos;
        this.mContext = mContext;
    }

    /**
     * 根据 downloadPos和historyPos 重绘item背景
     */
    public ChapterAdapter(List<ComicInfo.ChapterListBean> chapterList, int historyPos, List<Integer> downloadPos, Context mContext) {
        this.chapterList = chapterList;
        this.mContext = mContext;
        this.historyPos = historyPos;
        this.downloadPos = downloadPos;
    }

    public void setHistoryPos(int historyPos) {
        this.historyPos = historyPos;
    }

    public void setDownloadPos(List<Integer> downloadPos) {
        this.downloadPos = downloadPos;
    }

    public List<Integer> getDownloadPos() {
        return downloadPos;
    }

    public List<Integer> getSelectionPos() {
        return selectionPos;
    }

    public int addSelectPos(int selectPos) {
        if (!isSelected(selectPos) && !isDownload(selectPos)) {
            selectionPos.add(selectPos);
        }
        return selectionPos.size();
    }

    public int selectAll() {
        selectionPos.clear();
        for (int i = 0; i < chapterList.size(); i++) {
            //判断是否已经在下载记录中
            if (!isDownload(i)) {
                selectionPos.add(i);
            }
        }
        return selectionPos.size();
    }

    public int unselectAll() {
        selectionPos.clear();
        return selectionPos.size();
    }

    private boolean isDownload(int position) {
        if (downloadPos.size() > 0) {
            for (int pos : downloadPos) {
                if (pos == position) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSelected(int selectPos) {
        if (selectionPos.size() > 0) {
            for (int i = 0; i < selectionPos.size(); i++) {
                if (selectPos == selectionPos.get(i)) {
                    selectionPos.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkSelect(int position) {
        for (int i = 0; i < selectionPos.size(); i++) {
            if (position == selectionPos.get(i)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDownload(int position) {
        for (int i = 0; i < downloadPos.size(); i++) {
            if (position == downloadPos.get(i)) {
                return true;
            }
        }
        return false;
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
        if (position == historyPos || (selectionPos.size() > 0 && checkSelect(position))) {
            holder.tvChapter.setBackgroundResource(R.drawable.chapter_select_bg);
            holder.tvChapter.setTextColor(Color.WHITE);
        } else {
            holder.tvChapter.setBackgroundResource(R.drawable.select_chapter);
            holder.tvChapter.setTextColor(Color.BLACK);
        }
        if (downloadPos.size() > 0 && checkDownload(position)) {
            if (historyPos == -1) {
                //SelectActivity页
                holder.tvChapter.setBackgroundResource(R.drawable.chapter_download_bg);
                holder.tvChapter.setTextColor(Color.LTGRAY);
            } else {
                //ComicDetailActivity页
                if (historyPos != position) {
                    holder.tvChapter.setBackgroundResource(R.drawable.chapter_download_border);
                    holder.tvChapter.setTextColor(mContext.getResources().getColor(R.color.downloadBorder, null));
                }
            }
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
