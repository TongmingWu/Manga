package com.tongming.manga.mvp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.tongming.manga.R;
import com.tongming.manga.cusview.SpaceItemDecoration;
import com.tongming.manga.mvp.base.BaseFragment;
import com.tongming.manga.mvp.bean.Hot;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.presenter.HomePresenterImp;
import com.tongming.manga.mvp.view.activity.ComicDetailActivity;
import com.tongming.manga.mvp.view.activity.HomeActivity;
import com.tongming.manga.mvp.view.activity.IHomeView;
import com.tongming.manga.mvp.view.activity.SearchActivity;
import com.tongming.manga.mvp.view.adapter.RVComicAdapter;
import com.tongming.manga.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Tongming on 2016/8/9.
 */
public class HomeFragment extends BaseFragment implements IHomeView {
    @BindView(R.id.banner)
    ConvenientBanner convenientBanner;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    /*@BindView(R.id.gv_recommend)
    GridView gvRecommend;
    @BindView(R.id.gv_local)
    GridView gvLocal;
    @BindView(R.id.gv_release)
    GridView gvRelease;
    @BindView(R.id.gv_hot)
    GridView gvHot;*/
    @BindView(R.id.rv_hot)
    RecyclerView rvHot;
    @BindView(R.id.rv_recommend)
    RecyclerView rvRecommend;
    @BindView(R.id.rv_local)
    RecyclerView rvLoacl;
    @BindView(R.id.rv_release)
    RecyclerView rvRelease;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_line)
    TextView tvLine;
    Hot hot;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        if (CommonUtil.getDeviceHeight(getActivity()) - CommonUtil.getScreenHeight(getContext()) > 0) {
            tvLine.setVisibility(View.VISIBLE);
        }
        presenter = new HomePresenterImp(this);
        ((HomePresenterImp) presenter).getData();
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((HomePresenterImp) presenter).getData();
                //调用HomeActivity的getUser()方法
                if (getActivity().getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("isLogin", false)) {
                    ((HomeActivity) getActivity()).getUser(User.getInstance().getToken());
                }
            }
        });
        rvHot.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvHot.addItemDecoration(new SpaceItemDecoration(CommonUtil.dip2px(getContext(), 10), true));

        rvRecommend.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvRecommend.addItemDecoration(new SpaceItemDecoration(CommonUtil.dip2px(getContext(), 10), true));

        rvLoacl.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvLoacl.addItemDecoration(new SpaceItemDecoration(CommonUtil.dip2px(getContext(), 10), true));

        rvRelease.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvRelease.addItemDecoration(new SpaceItemDecoration(CommonUtil.dip2px(getContext(), 10), true));
    }

    @Override
    public void onLoad(final Hot hot) {
        this.hot = hot;
        if (hot.getBanner().size() == 0 || hot.getResult() == null) {
            return;
        }
        List<String> bannerList = new ArrayList<>();
        for (Hot.BannerBean bean : hot.getBanner()) {
            bannerList.add(bean.getImg());
        }
        initBanner(bannerList);

        RVComicAdapter hotAdapter = new RVComicAdapter(hot.getResult().getHot(), getContext(), RVComicAdapter.NORMAL_COMIC);
        rvHot.setAdapter(hotAdapter);
        hotAdapter.setOnItemClickListener(new RVComicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String comicUrl = hot.getResult().getHot().get(position).getComic_url();
                Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                intent.putExtra("url", comicUrl);
                intent.putExtra("name", hot.getResult().getHot().get(position).getComic_name());
                startActivity(intent);
            }
        });

        RVComicAdapter recommendAdapter = new RVComicAdapter(hot.getResult().getRecommend(), getContext(), RVComicAdapter.NORMAL_COMIC);
        rvRecommend.setAdapter(recommendAdapter);
        recommendAdapter.setOnItemClickListener(new RVComicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String comicUrl = hot.getResult().getRecommend().get(position).getComic_url();
                Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                intent.putExtra("url", comicUrl);
                intent.putExtra("name", hot.getResult().getRecommend().get(position).getComic_name());
                startActivity(intent);
            }
        });

        RVComicAdapter localAdapter = new RVComicAdapter(hot.getResult().getLocal(), getContext(), RVComicAdapter.NORMAL_COMIC);
        rvLoacl.setAdapter(localAdapter);
        localAdapter.setOnItemClickListener(new RVComicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String comicUrl = hot.getResult().getLocal().get(position).getComic_url();
                Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                intent.putExtra("url", comicUrl);
                intent.putExtra("name", hot.getResult().getLocal().get(position).getComic_name());
                startActivity(intent);
            }
        });

        RVComicAdapter releaseAdapter = new RVComicAdapter(hot.getResult().getRelease(), getContext(), RVComicAdapter.NORMAL_COMIC);
        rvRelease.setAdapter(releaseAdapter);
        releaseAdapter.setOnItemClickListener(new RVComicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String comicUrl = hot.getResult().getRelease().get(position).getComic_url();
                Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                intent.putExtra("url", comicUrl);
                intent.putExtra("name", hot.getResult().getRelease().get(position).getComic_name());
                startActivity(intent);
            }
        });
    }

    private void initBanner(List<String> bannerList) {
        convenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new Holder<String>() {
                    private ImageView imageView;

                    @Override
                    public View createView(Context context) {
                        imageView = new ImageView(context);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return imageView;
                    }

                    @Override
                    public void UpdateUI(Context context, int position, String data) {
                        GlideUrl url = new GlideUrl(data, new LazyHeaders.Builder()
                                .addHeader("Referer", "http://m.dmzj.com/")
                                .build());
                        Glide.with(context).load(url).into(imageView);
                    }
                };
            }
        }, bannerList)
                .setPageIndicator(new int[]{R.drawable.point_bg_normal, R.drawable.point_bg_enable})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        convenientBanner.startTurning(5000);
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String comicUrl = hot.getBanner().get(position).getComic_url();
                Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                intent.putExtra("url", comicUrl);
                intent.putExtra("name", hot.getBanner().get(position).getTitle());
                startActivity(intent);
            }
        });

    }

    @OnClick({R.id.rl_hot, R.id.rl_recommend, R.id.rl_local, R.id.rl_release})
    public void onClick(View view) {
        int select = 0;
        int page = 0;
        int type;
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("select", select)
                .putExtra("page", page);
        switch (view.getId()) {
            /*
                @param select = 0
                type , page = 1
            */
            case R.id.rl_hot:
                type = 32;
                intent.putExtra("type", type);
                intent.putExtra("name", "热门连载");
                break;
            case R.id.rl_recommend:
                type = 34;
                intent.putExtra("type", type);
                intent.putExtra("name", "排行榜");
                break;
            case R.id.rl_local:
                type = 28;
                intent.putExtra("type", type);
                intent.putExtra("name", "国漫");
                break;
            case R.id.rl_release:
                type = 35;
                intent.putExtra("type", type);
                intent.putExtra("name", "最近更新");
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onFail() {

    }

    @Override
    public void showRefresh() {
        llContent.setVisibility(View.INVISIBLE);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
            }
        });
        refresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        }, 1000 * 3);
    }

    @Override
    public void hideRefresh() {
        if (refresh.isRefreshing()) {
            refresh.post(new Runnable() {
                @Override
                public void run() {
                    refresh.setRefreshing(false);
                }
            });
        }
        if (llContent.getVisibility() == View.INVISIBLE) {
            llContent.setVisibility(View.VISIBLE);
        }
    }
}
