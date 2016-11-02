package com.tongming.manga.mvp.view.activity;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.cusview.SpaceItemDecoration;
import com.tongming.manga.mvp.base.SwipeBackActivity;
import com.tongming.manga.mvp.bean.Category;
import com.tongming.manga.mvp.bean.ComicCard;
import com.tongming.manga.mvp.bean.Search;
import com.tongming.manga.mvp.bean.SearchRecord;
import com.tongming.manga.mvp.presenter.SearchPresenterImp;
import com.tongming.manga.mvp.view.adapter.RVComicAdapter;
import com.tongming.manga.util.CommonUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Tongming on 2016/8/10.
 */
public class SearchActivity extends SwipeBackActivity implements ISearchView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    /*@BindView(R.id.rv_search)
    EasyRecyclerView rvSearch;*/
    @BindView(R.id.rl_progress)
    RelativeLayout rlProgress;
    @BindView(R.id.tv_load_more)
    TextView tvMore;
    @BindView(R.id.pb_load_more)
    ProgressBar pbMore;
    private String word;
    private int type;
    private List<ComicCard> comicCards;
    //    private ComicAdapter adapter;
    private RVComicAdapter adapter;
    private boolean isLoading;
    private GridLayoutManager manager;
    private Search search;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initToolbar(toolbar);
        presenter = new SearchPresenterImp(this);
        Intent intent = getIntent();
        word = intent.getStringExtra("word");
        int page = intent.getIntExtra("page", 1);
        if (word == null) {
            type = intent.getIntExtra("type", 1);
            String typeName = intent.getStringExtra("name");
            toolbar.setTitle(typeName);
            ((SearchPresenterImp) presenter).doSearch(type, page);
        } else {
            ((SearchPresenterImp) presenter).doSearch(word, page);
            toolbar.setTitle("搜索: " + word);
        }
    }

    @Override
    public void onSuccess(Search search) {
        isLoading = false;
        this.search = search;
        if (comicCards == null) {
            comicCards = search.getResult();
            initRecyclerView();
            hideProgress();
        } else {
            /*for (ComicCard comic : search.getResult()) {
                comicCards.add(comic);
                adapter.notifyItemInserted(comicCards.size() - 1);
            }*/
            int originalSize = comicCards.size();
            comicCards.addAll(search.getResult());
            adapter.notifyItemRangeChanged(originalSize, search.getResult().size());
            rlProgress.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {
        manager = new GridLayoutManager(this, 3);
        rvSearch.setLayoutManager(manager);
        rvSearch.addItemDecoration(new SpaceItemDecoration(CommonUtil.dip2px(this, 10), true));
        adapter = new RVComicAdapter(comicCards, this, RVComicAdapter.NORMAL_COMIC);
        rvSearch.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVComicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object object) {
                Intent intent = new Intent(SearchActivity.this, ComicDetailActivity.class);
                ComicCard card = comicCards.get(position);
                intent.putExtra("url", card.getComic_url())
                        .putExtra("name", card.getComic_name())
                        .putExtra("source", card.getComic_source())
                        .putExtra("cover", card.getCover());
                ImageView ivCover = (ImageView) view.findViewById(R.id.iv_cover);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SearchActivity.this, ivCover, getString(R.string.coverName));
                startActivity(intent, options.toBundle());
            }
        });
        rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int position = manager.findLastCompletelyVisibleItemPosition();
                        if (position == manager.getItemCount() - 1) {
                            if (search.isNext()) {
                                loadMore(search.getCurrent_page() + 1);
                            } else {
//                                Toast.makeText(SearchActivity.this, "没有咯- -", Toast.LENGTH_SHORT).show();
                                rlProgress.setVisibility(View.VISIBLE);
                                pbMore.setVisibility(View.GONE);
                                tvMore.setText("已没有更多- -");
                            }
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
    }

    private void loadMore(int requestPage) {
        rlProgress.setVisibility(View.VISIBLE);
        /*tvMore.setText("正在加载");
        tvMore.setTextColor(Color.BLACK);
        pbMore.setVisibility(View.VISIBLE);*/
        if (word == null) {
            ((SearchPresenterImp) presenter).doSearch(type, requestPage);
        } else {
            ((SearchPresenterImp) presenter).doSearch(word, requestPage);
        }
        isLoading = true;
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
    public void onQuery(List<SearchRecord> recordList) {

    }

    @Override
    public void onGetCategory(Category category) {

    }

    @Override
    public void onFail(Throwable throwable) {
        Logger.e(throwable.getMessage());
    }
}
