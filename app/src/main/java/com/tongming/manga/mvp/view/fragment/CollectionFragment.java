package com.tongming.manga.mvp.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.mvp.base.BaseFragment;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.presenter.CollectPresenterImp;
import com.tongming.manga.mvp.view.activity.ComicDetailActivity;
import com.tongming.manga.mvp.view.activity.ICollectView;
import com.tongming.manga.mvp.view.adapter.ComicAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Tongming on 2016/8/9.
 */
public class CollectionFragment extends BaseFragment implements ICollectView {

    @BindView(R.id.gv_collect)
    GridView gvCollect;
    private boolean isLogin;
    private ProgressDialog progressDialog;

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
        isLogin = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("isLogin", false);
        if (!isLogin) {
            if (presenter == null) {
                presenter = new CollectPresenterImp(this);
            }
            ((CollectPresenterImp) presenter).queryAllCollect(getContext());
        } else {
            List<CollectedComic> comics = User.getInstance().getInfo().getUser().getCollection();
            if (comics.size() > 0) {
                initGridView(comics);
                Logger.d(comics.size());
            }
        }
    }

    public void refresh() {
        if (isLogin) {
            UserInfo info = User.getInstance().getInfo();
            initGridView(info.getUser().getCollection());
        }else {
            ((CollectPresenterImp) presenter).queryAllCollect(getContext());
        }
        Logger.d("刷新成功");
    }

    @Override
    public void onQueryAllCollect(List<CollectedComic> comics) {
        initGridView(comics);
    }

    @Override
    public void onDeleteCollectOnNet(UserInfo info) {
        if (info != null) {
            User.getInstance().saveUser(info);
            initGridView(info.getUser().getCollection());
            hideDialog();
            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
        }
    }

    private void initGridView(final List<CollectedComic> comics) {
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
                /*View dialog = View.inflate(getContext(), R.layout.dialog_card, null);
                TextView tvRemove = (TextView) dialog.findViewById(R.id.tv_remove_collection);*/
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
