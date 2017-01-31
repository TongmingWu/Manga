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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter == null) {
            presenter = new CollectPresenterImp(this);
        }
        isLogin = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("isLogin", false);
        if (!isLogin) {
            ((CollectPresenterImp) presenter).queryAllCollect();
        } else {
            if (User.getInstance() != null) {
                List<CollectedComic> comics = User.getInstance().getInfo().getUser().getCollection();
                if (comics != null && comics.size() > 0) {
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
            ((CollectPresenterImp) presenter).queryAllCollect();
        }
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
        if (comics == null && rvCollection != null) {
            comics = comicList;
            rvCollection.setLayoutManager(new GridLayoutManager(getContext(), 3));
            rvCollection.addItemDecoration(new SpaceItemDecoration(CommonUtil.dip2px(getContext(), 10), true));
            if (adapter == null) {
                adapter = new RVComicAdapter(comics, getContext(), RVComicAdapter.COLLECTION_COMIC);
                rvCollection.setAdapter(adapter);
                adapter.setOnItemClickListener(new RVComicAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, Object object) {
                        Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                        CollectedComic comic = (CollectedComic) object;
                        String name = comic.getComic_name();
                        intent.putExtra("url", comic.getComic_url())
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
                    public boolean onItemLongClick(View view, int position, Object object) {
                        final CollectedComic comic = (CollectedComic) object;
                        new AlertDialog.Builder(getActivity())
                                .setTitle("注意")
                                .setMessage("确定要取消收藏" + comic.getComic_name() + "吗?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!isLogin) {
                                            ((CollectPresenterImp) presenter).deleteCollectByName(comic.getComic_name());
                                        } else {
                                            //在线更新数据
                                            ((CollectPresenterImp) presenter).deleteCollectOnNet(comic.getComic_name());
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
            if (comics.size() != comicList.size()) {
                if (comics.size() > comicList.size()) {
                    int position = 0;
                    while (position < comicList.size()) {
                        if (!comics.get(position).getComic_name().equals(comicList.get(position).getComic_name())) {
                            break;
                        }
                        position++;
                    }
                    comics.remove(position);
                    adapter.notifyItemRemoved(position);
                } else {
                    comics.clear();
                    comics.addAll(comicList);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onDeleteCollectByName(int state) {
        if (state > 0) {
            ((CollectPresenterImp) presenter).queryAllCollect();
            Logger.d("删除指定收藏完毕");
            hideDialog();
        }
    }

    @Override
    public void onDeleteAllCollect(int state) {

    }

    private void showNothing() {
        //未收藏任何漫画

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.closeDB();
        }
    }
}
