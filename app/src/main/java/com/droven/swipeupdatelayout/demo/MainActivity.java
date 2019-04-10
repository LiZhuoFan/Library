package com.droven.swipeupdatelayout.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droven.swipeupdatelayout.SwipeUpdateLayout;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private SwipeUpdateLayout swipeUpdateLayout;
    private MainAdapter adapter = new MainAdapter();
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeUpdateLayout = findViewById(R.id.swipe_update_layout);
        swipeUpdateLayout.setOnRefreshListener(new SwipeUpdateLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                complete(2, 3);
            }
        });
        swipeUpdateLayout.setOnLoadMoreListener(new SwipeUpdateLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                complete(3, 3);
            }
        });
        swipeUpdateLayout.setOnReloadListener(new SwipeUpdateLayout.OnReloadListener() {
            @Override
            public void onReload() {
                complete(4, 2);
            }
        });
        complete(1, 2);
    }

    private void complete(final int type, int second) {
        Observable.timer(second, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(Object o) {
                        switch (type) {
                            case 1:
                                swipeUpdateLayout.loadFail("网错错误");
                                break;
                            case 2:
                                adapter.setCount(0);
                                break;
                            case 3:
                                adapter.loadMore();
                                break;
                            case 4:
                                adapter.setCount(20);
                                swipeUpdateLayout.setAdapter(adapter);
                                break;
                        }
                        swipeUpdateLayout.loadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private static class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

        private int mCount = 0;

        void loadMore() {
            mCount += 20;
            notifyDataSetChanged();
        }

        void setCount(int count) {
            mCount = count;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            textView.setPadding(0, 24, 0, 24);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tv.setText(MessageFormat.format("Position:{0}", position));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv;

            ViewHolder(@NonNull TextView tv) {
                super(tv);
                this.tv = tv;
            }
        }

    }

}
