package com.hrant.pixabayclientapp.view.activities.mainActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.hrant.pixabayclientapp.PixabayClientApp;
import com.hrant.pixabayclientapp.R;
import com.hrant.pixabayclientapp.model.PixabayImageModel;
import com.hrant.pixabayclientapp.presenter.MainPresenter;
import com.hrant.pixabayclientapp.shared.helpers.SpacesItemDecoration;
import com.hrant.pixabayclientapp.view.activities.BaseActivity;
import com.hrant.pixabayclientapp.view.adapters.ImagesAdapter;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements IMainView {
    @Inject
    MainPresenter mPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ImagesAdapter mAdapter;
    private int mPageNo = 1;
    private int mPagesCount;
    private int mPageItemsCount = 45;
    private String mSearchString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);
        PixabayClientApp.getInstance().getMainComponent().inject(this);
        mPresenter.onViewCreated(this);
        initViews();
        mPresenter.getImageList(mPageNo, mPageItemsCount, mSearchString);
    }

    private void initViews() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mPageNo = 1;
            mPresenter.getImageList(mPageNo, mPageItemsCount, mSearchString);
        });
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView = findViewById(R.id.recycler_view);
        int spacingSize = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(3, spacingSize, false));
        mAdapter = new ImagesAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case ImagesAdapter.IMAGE_ITEM:
                        return 1;
                    case ImagesAdapter.LOADING:
                        return 3;

                    default:
                        return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.findLastCompletelyVisibleItemPosition() == mAdapter.getItemCount() - 1 && mPageNo < mPagesCount) {
                    mPageNo++;
                    mPresenter.getImageList(mPageNo, mPageItemsCount, mSearchString);
                }
            }
        });

        ImageView searchCancel = findViewById(R.id.search_cancel);
        EditText searchEditText = findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0)
                    searchCancel.setVisibility(View.VISIBLE);
                else
                    searchCancel.setVisibility(View.GONE);
            }
        });
        searchEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mSwipeRefreshLayout.setRefreshing(true);
                mSearchString = searchEditText.getText().toString().trim().replace(" ", "+");
                mPageNo = 1;
                mPresenter.getImageList(mPageNo, mPageItemsCount, mSearchString);
                return true;
            }
            return false;
        });
        searchCancel.setOnClickListener(view -> searchEditText.setText(""));
    }

    @Override
    public void addToImagesList(List<PixabayImageModel> hits) {
        mAdapter.addImagesList(hits);
    }

    @Override
    public void updateImagesList(List<PixabayImageModel> hits, int pagesCount) {
        mPagesCount = pagesCount;
        mAdapter.updateImagesList(hits);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PixabayClientApp.getInstance().releaseMainComponent();
        mPresenter.cancelRequests();
    }
}
