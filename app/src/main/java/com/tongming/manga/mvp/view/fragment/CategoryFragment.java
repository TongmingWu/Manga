package com.tongming.manga.mvp.view.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.cusview.CusListView;
import com.tongming.manga.mvp.base.BaseFragment;
import com.tongming.manga.mvp.bean.ComicCard;
import com.tongming.manga.mvp.bean.Search;
import com.tongming.manga.mvp.bean.SearchRecord;
import com.tongming.manga.mvp.presenter.SearchPresenterImp;
import com.tongming.manga.mvp.view.activity.ComicDetailActivity;
import com.tongming.manga.mvp.view.activity.ISearchView;
import com.tongming.manga.mvp.view.activity.SearchActivity;
import com.tongming.manga.mvp.view.adapter.CategoryAdapter;
import com.tongming.manga.mvp.view.adapter.SearchAdapter;
import com.tongming.manga.util.CommonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Tongming on 2016/8/9.
 */
public class CategoryFragment extends BaseFragment implements ISearchView {
    @BindView(R.id.et_search)
    AutoCompleteTextView etSearch;
    @BindView(R.id.gv_special_category)
    GridView gvSpecialCategory;
    @BindView(R.id.gv_normal_category)
    GridView gvNormalCategory;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.lv_hint)
    CusListView lvHint;
    @BindView(R.id.sl_content)
    ScrollView slContent;
    boolean isSearch;
    private SearchAdapter adapter;
    private List<SearchRecord> result = new ArrayList<>();
    private boolean isQueryRecord;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initView() {
//        initGridView();
        initNewGV();
        slContent.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                lvHint.setVisibility(View.GONE);
                CommonUtil.hideSoftInput(getActivity());
                etSearch.setFocusable(false);
            }
        });
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etSearch.setFocusable(true);
                etSearch.setFocusableInTouchMode(true);
                etSearch.requestFocus();
                String word = etSearch.getText().toString().trim();
                editHint(word);
                return false;
            }
        });
        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEND);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String word = s.toString().trim();
                editHint(word);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                String word = s.toString().trim();
//                editHint(word);
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
        lvHint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ComicDetailActivity.class);
                SearchRecord card = result.get(position);
                intent.putExtra("url", card.getComic_url());
                intent.putExtra("name", card.getComic_name());
                ((SearchPresenterImp) presenter).recordSearch(card.getComic_name(), card.getComic_url());
                startActivity(intent);
                etSearch.setText(card.getComic_name());
                lvHint.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 实时更新关键词的搜索结果
     */
    private void editHint(String word) {
        //搜索结果最多显示5个
        if (presenter == null) {
            presenter = new SearchPresenterImp(this);
        }
        if (!TextUtils.isEmpty(word)) {
            //进行搜索
            Logger.d("搜索:" + word);
            presenter.clearSubscription();
            ((SearchPresenterImp) presenter).doSearch(word);
        } else {
            //显示历史记录
            if (!isQueryRecord) {
                Logger.d("显示历史记录");
                isQueryRecord = true;
                ((SearchPresenterImp) presenter).querySearchRecord();
            }
        }
    }

    private void doSearch() {
        if (!TextUtils.isEmpty(etSearch.getText().toString().trim())) {
            if (!isSearch) {
                int page = 0;
                String word = etSearch.getText().toString();
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("page", page);
                intent.putExtra("word", word);
                startActivity(intent);
                lvHint.setVisibility(View.GONE);
                isSearch = true;
            }
        } else {
            Toast.makeText(getActivity(), "内容不能为空xD", Toast.LENGTH_SHORT).show();
        }
    }

    private void initNewGV() {
        SparseArray<String> array = new SparseArray<>();
        List<Integer> picList = new ArrayList<>();
        String[] category = new String[]{
                "最近更新", "排行榜", "完结", "连载中", "韩国", "港台", "欧美", "国漫",
                "日本", "青年", "少女", "少年", "搞笑", "轻小说", "节操", "奇幻", "职场", "武侠",
                "治愈", "萌系", "后宫", "耽美", "伪娘", "百合", "生活", "恐怖", "悬疑", "校园", "魔法",
                "竞技", "爱情", "科幻", "格斗", "欢乐", "冒险"

        };
        for (int i = 35; i > 0; i--) {
            array.put(i, category[i - 1]);
        }
        gvSpecialCategory.setAdapter(new CategoryAdapter(picList, array, getContext()));
        gvNormalCategory.setVisibility(View.GONE);
    }

    private void initGridView() {
        List<Integer> picList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<Integer> typeList = new ArrayList<>();
        int[] types = new int[]{
                9, 7, 10, 8, 6, 2, 3, 1, 5
        };
        String[] names = new String[]{
                //从10-1,其中没有4
                "最近更新", "热门连载", "已完结", "排行榜", "国漫", "日漫", "韩漫", "港漫", "欧美"
        };
        for (int type : types) {
            typeList.add(type);
        }
        Collections.addAll(nameList, names);
        gvSpecialCategory.setAdapter(new CategoryAdapter(picList, nameList, typeList, getActivity()));

        List<Integer> normalPicList = new ArrayList<>();
        List<String> normalNameList = new ArrayList<>();
        List<Integer> normalTypeList = new ArrayList<>();
        names = new String[]{
                "魔幻", "格斗", "竞技", "少女", "热血", "萌系", "搞笑",
                "励志", "恐怖", "悬疑", "科幻", "游戏", "武侠", "耽美",
                "青年", "同人", "美女", "校园", "轻小说", "四格", "美食",
                "百合", "猎奇", "后宫", "伪娘", "节操", "机战", "福利",
                "穿越", "恋爱", "治愈", "生活"
        };
        types = new int[]{
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 14, 15, 16, 17,
                18, 19, 20, 21, 30, 31, 35, 39, 40, 41, 42, 43, 82,
                83, 84, 90, 93
        };
        for (int type : types) {
            normalTypeList.add(type);
        }
        Collections.addAll(normalNameList, names);
        gvNormalCategory.setAdapter(new CategoryAdapter(normalPicList, normalNameList, normalTypeList, getActivity()));
    }

    @Override
    public void onStop() {
        super.onStop();
        isSearch = false;
    }

    @Override
    public void onSuccess(Search search) {
        parserResult(search.getResult(), 0);
    }

    @Override
    public void onQuery(List<SearchRecord> recordList) {
        isQueryRecord = false;
        parserResult(recordList, 1);
    }

    private void parserResult(List<?> list, int type) {
        if (list.size() > 0) {
            if (type == 0) {
                List<ComicCard> cardList = (List<ComicCard>) list;
                for (ComicCard card : cardList) {
                    SearchRecord record = new SearchRecord();
                    record.setComic_name(card.getComic_name());
                    record.setComic_url(card.getComic_url());
                    result.add(record);
                }
            } else {
                result = (List<SearchRecord>) list;
            }
            if (adapter == null) {
                adapter = new SearchAdapter(result, getContext());
                lvHint.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            lvHint.setVisibility(View.VISIBLE);
        } else {
            lvHint.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onFail() {

    }
}
