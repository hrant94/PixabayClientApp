package com.hrant.pixabayclientapp.view.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.hrant.pixabayclientapp.PixabayClientApp;
import com.hrant.pixabayclientapp.R;
import com.hrant.pixabayclientapp.model.PixabayImageModel;
import com.hrant.pixabayclientapp.presenter.MainPresenter;
import com.hrant.pixabayclientapp.shared.helpers.SpacesItemDecoration;
import com.hrant.pixabayclientapp.view.adapters.ImagesAdapter;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements IMainView {
    @Inject
    MainPresenter mPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ImagesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);
        PixabayClientApp.getInstance().getUserComponent().inject(this);
        mPresenter.onViewCreated(this);
        initViews();
        mPresenter.getImageList();
    }

    private void initViews() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mPresenter.getImageList();
        });
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        int spacingSize = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(3, spacingSize, false));
        mAdapter = new ImagesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setImagesList(List<PixabayImageModel> hits) {
        mAdapter.addImagesList(hits);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
