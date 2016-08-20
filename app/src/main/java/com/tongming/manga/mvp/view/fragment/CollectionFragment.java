package com.tongming.manga.mvp.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.mvp.base.BaseFragment;
import com.tongming.manga.mvp.bean.CollectedComic;
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
        ((CollectPresenterImp) presenter).queryAllCollect(getContext());
    }

    @Override
    public void onQueryAllCollect(final List<CollectedComic> comics) {
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
                                ((CollectPresenterImp) presenter).deleteCollectByName(getContext(), comics.get(position).getName());
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
        }
    }

    @Override
    public void onDeleteAllCollect(int state) {

    }
}
