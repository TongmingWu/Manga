package com.tongming.manga.mvp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.tongming.manga.R;
import com.tongming.manga.mvp.base.BaseFragment;
import com.tongming.manga.mvp.bean.Hot;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.presenter.HomePresenterImp;
import com.tongming.manga.mvp.view.activity.ComicDetailActivity;
import com.tongming.manga.mvp.view.activity.HomeActivity;
import com.tongming.manga.mvp.view.activity.IHomeView;
import com.tongming.manga.mvp.view.activity.SearchActivity;
import com.tongming.manga.mvp.view.adapter.ComicAdapter;

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
    @BindView(R.id.gv_hot)
    GridView gvHot;
    @BindView(R.id.gv_update)
    GridView gvUpdate;
    @BindView(R.id.gv_newest)
    GridView gvNewest;
    @BindView(R.id.rl_hot)
    RelativeLayout rlHot;
    @BindView(R.id.rl_update)
    RelativeLayout rlUpdate;
    @BindView(R.id.rl_newest)
    RelativeLayout rlNewest;
    @BindView(R.id.ll_content)
    LinearLayout llContent;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
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
    }

    @Override
    public void onLoad(final Hot hot) {
        if (hot.getBanner().size() == 0 || hot.getResult().getHot().size() == 0) {
            return;
        }
        List<String> bannerList = new ArrayList<>();
        for (Hot.BannerBean bean : hot.getBanner()) {
            bannerList.add(bean.getImg());
        }
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
                        Glide.with(context).load(data).into(imageView);
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
        // GridView
        gvHot.setAdapter(new ComicAdapter(hot.getResult().getHot(), getContext(), ComicAdapter.NORAML_COMIC));
        gvHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String comicUrl = hot.getResult().getHot().get(position).getComic_url();
                Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                intent.putExtra("url", comicUrl);
                intent.putExtra("name", hot.getResult().getHot().get(position).getComic_name());
                startActivity(intent);
            }
        });
        gvUpdate.setAdapter(new ComicAdapter(hot.getResult().getUpdate(), getContext(), ComicAdapter.NORAML_COMIC));
        gvUpdate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String comicUrl = hot.getResult().getUpdate().get(position).getComic_url();
                Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                intent.putExtra("url", comicUrl);
                intent.putExtra("name", hot.getResult().getUpdate().get(position).getComic_name());
                startActivity(intent);
            }
        });
        gvNewest.setAdapter(new ComicAdapter(hot.getResult().getRelease(), getContext(), ComicAdapter.NORAML_COMIC));
        gvNewest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String comicUrl = hot.getResult().getRelease().get(position).getComic_url();
                Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                intent.putExtra("url", comicUrl);
                intent.putExtra("name", hot.getResult().getRelease().get(position).getComic_name());
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.rl_hot, R.id.rl_update, R.id.rl_newest})
    public void onClick(View view) {
        int select = 0;
        int page = 1;
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
                type = 7;
                intent.putExtra("type", type);
                intent.putExtra("name", "热门连载");
                break;
            case R.id.rl_update:
                type = 8;
                intent.putExtra("type", type);
                intent.putExtra("name", "排行榜");
                break;
            case R.id.rl_newest:
                type = 9;
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
        }, 1000 * 5);
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
