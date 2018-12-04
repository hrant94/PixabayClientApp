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
import android.widget.TextView;

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
    private TextView mEmptyStateTextView;
    private int mPageNo = 1;
    private int mCurrentItemsCount = 0;
    private int mTotalItemsCount = 0;
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
        mPresenter.getImageList(mPageNo, mCurrentItemsCount, mSearchString);
    }

    private void initViews() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
        ImageView searchCancel = findViewById(R.id.search_cancel);
        EditText searchEditText = findViewById(R.id.search_edit_text);
        mEmptyStateTextView = findViewById(R.id.empty_state_text);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mPageNo = 1;
            mPresenter.getImageList(mPageNo, mCurrentItemsCount, mSearchString);
        });
        mSwipeRefreshLayout.setRefreshing(true);

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
                if (layoutManager.findLastCompletelyVisibleItemPosition() == mAdapter.getItemCount() - 1 && mCurrentItemsCount < mTotalItemsCount) {
                    mPageNo++;
                    mPresenter.getImageList(mPageNo, mCurrentItemsCount, mSearchString);
                }
            }
        });

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
                mRecyclerView.smoothScrollToPosition(0);
                mSwipeRefreshLayout.setRefreshing(true);
                mSearchString = searchEditText.getText().toString().trim().replace(" ", "+");
                mPageNo = 1;
                mPresenter.getImageList(mPageNo, mCurrentItemsCount, mSearchString);
                return true;
            }
            return false;
        });

        searchCancel.setOnClickListener(view -> searchEditText.setText(""));
    }

    @Override
    public void addToImagesList(List<PixabayImageModel> hits) {
        mAdapter.addImagesList(hits);
        mCurrentItemsCount = mAdapter.getItemCount();
    }

    @Override
    public void updateImagesList(List<PixabayImageModel> hits, int totalCount) {
        mTotalItemsCount = totalCount;
        mAdapter.updateImagesList(hits);
        if (mEmptyStateTextView.getVisibility() == View.VISIBLE)
            mEmptyStateTextView.setVisibility(View.GONE);
        if (mRecyclerView.getVisibility() == View.GONE)
            mRecyclerView.setVisibility(View.VISIBLE);
        mCurrentItemsCount = mAdapter.getItemCount();
    }

    @Override
    public void showEmptyState() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
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
