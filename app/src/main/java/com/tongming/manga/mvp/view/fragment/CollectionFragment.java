package com.tongming.manga.mvp.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.cusview.SpaceItemDecoration;
import com.tongming.manga.mvp.base.BaseFragment;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.presenter.CollectPresenterImp;
import com.tongming.manga.mvp.view.activity.ComicDetailActivity;
import com.tongming.manga.mvp.view.activity.ICollectView;
import com.tongming.manga.mvp.view.adapter.RVComicAdapter;
import com.tongming.manga.util.CommonUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Tongming on 2016/8/9.
 */
public class CollectionFragment extends BaseFragment implements ICollectView {

    //    @BindView(R.id.gv_collect)
//    GridView gvCollect;
    @BindView(R.id.rv_collection)
    RecyclerView rvCollection;
    private boolean isLogin;
    private ProgressDialog progressDialog;
    private List<CollectedComic> comics;
    private RVComicAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collection;
    }

    @Override
    protected void initView() {
        presenter = new CollectPresenterImp(this);
        ((CollectPresenterImp) presenter).queryAllCollect(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        //更新数据库操作
        if (presenter == null) {
            presenter = new CollectPresenterImp(this);
        }
        isLogin = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("isLogin", false);
        if (!isLogin) {
            ((CollectPresenterImp) presenter).queryAllCollect(getContext());
        } else {
            if (User.getInstance() != null) {
                List<CollectedComic> comics = User.getInstance().getInfo().getUser().getCollection();
                if (comics != null && comics.size() > 0) {
//                    initGridView(comics);
                    initRecyclerView(comics);
                }
            }
        }
    }

    public void refresh() {
        if (presenter == null) {
            presenter = new CollectPresenterImp(this);
        }
        if (isLogin) {
            UserInfo info = User.getInstance().getInfo();
            initRecyclerView(info.getUser().getCollection());
        } else {
            ((CollectPresenterImp) presenter).queryAllCollect(getContext());
        }
//        Logger.d("刷新成功");
    }

    @Override
    public void onQueryAllCollect(List<CollectedComic> comics) {
        initRecyclerView(comics);
    }

    @Override
    public void onDeleteCollectOnNet(UserInfo info) {
        if (info != null) {
            User.getInstance().saveUser(info);
            initRecyclerView(info.getUser().getCollection());
            hideDialog();
            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
        }
    }


    private void initRecyclerView(List<CollectedComic> comicList) {
        if (this.comics == null) {
            this.comics = comicList;
            rvCollection.setLayoutManager(new GridLayoutManager(getContext(), 3));
            rvCollection.addItemDecoration(new SpaceItemDecoration(CommonUtil.dip2px(getContext(), 10), true));
            if (adapter == null) {
                adapter = new RVComicAdapter(comics, getContext(), RVComicAdapter.COLLECTION_COMIC);
                rvCollection.setAdapter(adapter);
                adapter.setOnItemClickListener(new RVComicAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                        CollectedComic comic = comics.get(position);
                        String name = comic.getName();
                        intent.putExtra("url", comic.getUrl())
                                .putExtra("name", name.endsWith("漫画") ? name.replace("漫画", "") : name)
                                .putExtra("source", comic.getComic_source())
                                .putExtra("cover", comic.getCover());
                        if (Build.VERSION.SDK_INT >= 20) {
                            ImageView ivCover = (ImageView) view.findViewById(R.id.iv_cover);
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), ivCover, getString(R.string.coverName));
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }
                    }
                });
                adapter.setOnItemLongClickListener(new RVComicAdapter.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(View view, final int position) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("注意")
                                .setMessage("确定要取消收藏" + comics.get(position).getName() + "吗?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!isLogin) {
                                            ((CollectPresenterImp) presenter).deleteCollectByName(getContext(), comics.get(position).getName());
                                        } else {
                                            //在线更新数据
                                            ((CollectPresenterImp) presenter).deleteCollectOnNet(comics.get(position).getName());
                                        }
                                        if (progressDialog == null) {
                                            progressDialog = ProgressDialog.show(getActivity(), null, "正在删除...");
                                        }
                                    }
                                }).setNegativeButton("取消", null)
                                .show();
                        return true;
                    }
                });
            }
        } else {
            this.comics.clear();
            this.comics.addAll(comicList);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /*private void initGridView(final List<CollectedComic> comics) {
        gvCollect.setAdapter(new ComicAdapter(comics, getActivity(), ComicAdapter.COLLECTION_COMIC));
        gvCollect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                CollectedComic comic = comics.get(position);
                String name = comic.getName();
                intent.putExtra("url", comic.getUrl()).putExtra("name", name.endsWith("漫画") ? name.replace("漫画", "") : name);
                startActivity(intent);
            }
        });
        gvCollect.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                *//*View dialog = View.inflate(getContext(), R.layout.dialog_card, null);
                TextView tvRemove = (TextView) dialog.findViewById(R.id.tv_remove_collection);*//*
                new AlertDialog.Builder(getActivity())
                        .setTitle("注意")
                        .setMessage("确定要取消收藏" + comics.get(position).getName() + "吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isLogin) {
                                    ((CollectPresenterImp) presenter).deleteCollectByName(getContext(), comics.get(position).getName());
                                } else {
                                    //在线更新数据
                                    ((CollectPresenterImp) presenter).deleteCollectOnNet(comics.get(position).getName());
                                }
                                if (progressDialog == null) {
                                    progressDialog = ProgressDialog.show(getActivity(), null, "正在删除...");
                                }
                            }
                        }).setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });
    }*/

    @Override
    public void onDeleteCollectByName(int state) {
        if (state > 0) {
            ((CollectPresenterImp) presenter).queryAllCollect(getContext());
            Logger.d("删除指定收藏完毕");
            hideDialog();
        }
    }

    @Override
    public void onDeleteAllCollect(int state) {

    }

    private void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onFail(Throwable throwable) {
        Logger.e(throwable.getMessage());
    }
}
