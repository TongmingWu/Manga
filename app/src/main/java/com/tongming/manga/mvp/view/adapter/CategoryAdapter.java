package com.tongming.manga.mvp.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongming.manga.R;
import com.tongming.manga.cusview.GlideGircleTransform;
import com.tongming.manga.mvp.bean.Category;
import com.tongming.manga.mvp.view.activity.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tongming on 2016/8/10.
 */
public class CategoryAdapter extends BaseAdapter {

    private List<Integer> picList;
    private List<String> nameList;
    private List<Integer> typeList;
    private Context mContext;
    private SparseArray<String> array;

    private List<Category.CategoryBean> beanList;

    public CategoryAdapter(List<Integer> picList, List<String> nameList, List<Integer> typeList, Context mContext) {
        this.picList = picList;
        this.nameList = nameList;
        this.typeList = typeList;
        this.mContext = mContext;
    }

    public CategoryAdapter(List<Integer> picList, SparseArray<String> array, Context mContext) {
        this.mContext = mContext;
        this.array = array;
    }

    public CategoryAdapter(List<Category.CategoryBean> beanList, Context mContext) {
        this.beanList = beanList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int position) {
        return beanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_type, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Category.CategoryBean bean = beanList.get(position);
        holder.tvCategory.setText(bean.getTitle());
        if (!bean.getCover().isEmpty()) {
            Glide.with(mContext)
                    .load(bean.getCover())
                    .placeholder(R.drawable.default_avatar)
                    .transform(new GlideGircleTransform(mContext))
                    .into(holder.ivCategory);
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = bean.getCid();
                int page = 1;
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra("type", type)
                        .putExtra("page", page)
                        .putExtra("name", bean.getTitle());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        View itemView;
        @BindView(R.id.item_root)
        LinearLayout root;
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.iv_category)
        ImageView ivCategory;


        public ViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            /*Drawable drawable = itemView.getContext().getDrawable(R.drawable.default_avatar);
            drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() / 1.3f), (int) (drawable.getIntrinsicHeight() / 1.3f));
            tvCategory.setCompoundDrawables(null, drawable, null, null);
            tvCategory.setCompoundDrawablePadding(10);*/
        }
    }
}
