package com.tongming.manga.mvp.view.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.tongming.manga.mvp.bean.Category;
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
import java.util.Collection;
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

    private static final int SEARCH_NET = 0;
    private static final int SEARCH_LOCAL = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initView() {
//        initGridView();
//        initNewGV();
        getCategory();
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
                intent.putExtra("source", card.getComic_source());
                SearchRecord record = new SearchRecord();
                record.setComic_source(card.getComic_source());
                record.setComic_name(card.getComic_name());
                record.setComic_url(card.getComic_url());
                ((SearchPresenterImp) presenter).recordSearch(record);
                startActivity(intent);
                etSearch.setText(card.getComic_name());
                lvHint.setVisibility(View.GONE);
            }
        });
    }

    public void getCategory() {
        if (presenter == null) {
            presenter = new SearchPresenterImp(this);
        }
        ((SearchPresenterImp) presenter).getCategory();
    }

    /**
     * 实时更新关键词的搜索结果
     *
     * @param word 关键词
     */
    private void editHint(String word) {
        //搜索结果最多显示5个
        if (presenter == null) {
            presenter = new SearchPresenterImp(this);
        }
        presenter.clearSubscription();
        if (!TextUtils.isEmpty(word)) {
            //进行搜索
            Logger.d("搜索:" + word);
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

    public void hideHint() {
        if (lvHint != null) {
            lvHint.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isSearch = false;
        hideHint();
    }

    @Override
    public void onGetCategory(Category category) {
        gvSpecialCategory.setAdapter(new CategoryAdapter(category.getCategory(), getActivity()));
    }

    @Override
    public void onSuccess(Search search) {
        parserResult(search.getResult(), SEARCH_NET);
    }

    @Override
    public void onQuery(List<SearchRecord> recordList) {
        isQueryRecord = false;
        parserResult(recordList, SEARCH_LOCAL);
    }

    private void parserResult(List<?> list, int type) {
        if (list.size() > 0) {
            if (result != null) {
                result.clear();
            }
            if (type == SEARCH_NET) {
                List<ComicCard> cardList = (List<ComicCard>) list;
                for (ComicCard card : cardList) {
                    SearchRecord record = new SearchRecord();
                    record.setComic_name(card.getComic_name());
                    record.setComic_url(card.getComic_url());
                    record.setComic_source(card.getComic_source());
                    result.add(record);
                }
            } else {
                result.addAll((Collection<? extends SearchRecord>) list);
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
