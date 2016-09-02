package com.tongming.manga.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tongming.manga.R;
import com.tongming.manga.mvp.base.BaseActivity;
import com.tongming.manga.mvp.bean.ComicCard;
import com.tongming.manga.mvp.bean.Search;
import com.tongming.manga.mvp.presenter.SearchPresenterImp;
import com.tongming.manga.mvp.view.adapter.ComicAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Tongming on 2016/8/10.
 */
public class SearchActivity extends BaseActivity implements ISearchView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.gv_search)
    GridView gvSearch;
    /*@BindView(R.id.tv_load_more)
    TextView tvMore;
    @BindView(R.id.pb_load_more)
    ProgressBar pbMore;*/
    private String word;
    private int select;
    private int type;
    private List<ComicCard> comicCards;
    private ComicAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        initToolbar(toolbar);
        presenter = new SearchPresenterImp(this);
        Intent intent = getIntent();
        word = intent.getStringExtra("word");
        int page = intent.getIntExtra("page", 1);
        if (word == null) {
            select = intent.getIntExtra("select", 0);
            type = intent.getIntExtra("type", 1);
            String typeName = intent.getStringExtra("name");
            toolbar.setTitle(typeName);
            ((SearchPresenterImp)presenter).getComicType(select, type, page);
        } else {
            ((SearchPresenterImp)presenter).doSearch(word, page);
            toolbar.setTitle("搜索: " + word);
        }

    }

    @Override
    public void onSuccess(final Search search) {
        if (comicCards == null) {
            comicCards = search.getResult();
            adapter = new ComicAdapter(comicCards, this, ComicAdapter.NORAML_COMIC);
            gvSearch.setAdapter(adapter);
        } else {
            comicCards.addAll(search.getResult());
            adapter.notifyDataSetChanged();
            /*if (pbMore.getVisibility() == View.VISIBLE) {
                pbMore.setVisibility(View.GONE);
                tvMore.setText("暂无更多");
                tvMore.setTextColor(Color.DKGRAY);
            }*/
        }
        gvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ComicDetailActivity.class);
                intent.putExtra("url", comicCards.get(position).getComic_url());
                intent.putExtra("name", comicCards.get(position).getComic_name());
                startActivity(intent);
            }
        });
        //GridView滑动到底部的监听事件
        gvSearch.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastPosition = 0, lastPositonY = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {     //view静止的时候
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {   //滑动到底部
                        View v = view.getChildAt(view.getChildCount() - 1);
                        int[] location = new int[2];
                        v.getLocationOnScreen(location);
                        int y = location[1];    //获取当前view的y值
                        if (view.getLastVisiblePosition() != lastPosition && y != lastPositonY) {
                            //第一次滑动至底部
                            lastPosition = view.getLastVisiblePosition();
                            lastPositonY = y;
                            if (search.isNext()) {
                                loadMore(search.getCurrent_page() + 1); //加载更多
                            } else {
                                Toast.makeText(SearchActivity.this, "没有咯- -", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        } else if (view.getLastVisiblePosition() == lastPosition && y == lastPositonY) {
                            //第二次滑动至底部

                        }
                    }
                    //为滑动至底部或者第二次滑动至底部都重置
                    lastPosition = 0;
                    lastPositonY = 0;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void loadMore(int requestPage) {
        /*tvMore.setText("正在加载");
        tvMore.setTextColor(Color.BLACK);
        pbMore.setVisibility(View.VISIBLE);*/
        if (word == null) {
            ((SearchPresenterImp)presenter).getComicType(select, type, requestPage);
        } else {
            ((SearchPresenterImp)presenter).doSearch(word, requestPage);
        }
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        if (progress.getVisibility() == View.VISIBLE) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFail() {

    }
}
