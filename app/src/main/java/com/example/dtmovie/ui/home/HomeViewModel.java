package com.example.dtmovie.ui.home;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.example.dtmovie.base.BaseViewModel;
import com.example.dtmovie.data.model.Movie;
import com.example.dtmovie.data.responsitory.MovieReponsitory;
import com.example.dtmovie.data.source.remote.MovieRemoteData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseViewModel {
    public static final int PAGE_DEFAULT = 1;
    private CompositeDisposable mDisposable;
    private MovieReponsitory mReponsitory;
    private ObservableList<Movie> mMovieObservableList = new ObservableArrayList<>();
    private Context mContext;

    public void initViewModel(Context context) {
        mContext = context;
        mDisposable = new CompositeDisposable();
        mReponsitory = MovieReponsitory.getInstance(MovieRemoteData.getInstance(context));
        loadMovie();
    }

    private void loadMovie() {
        loadTopRate();
    }

    private void loadTopRate() {
        Disposable disposable = mReponsitory.getMoviesByCategory("now_playing", PAGE_DEFAULT)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieReponse -> {
                    mMovieObservableList.addAll(movieReponse.getMovies());
                }, throwable -> handeError(throwable.getMessage()));
        mDisposable.add(disposable);
    }

    private void handeError(String message) {
        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
    }
}
