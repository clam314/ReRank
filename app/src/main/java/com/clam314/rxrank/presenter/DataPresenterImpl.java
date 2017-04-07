package com.clam314.rxrank.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.clam314.rxrank.entity.CategoryGroup;
import com.clam314.rxrank.entity.ImageCache;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.http.Category;
import com.clam314.rxrank.model.HttpModelPresenter;
import com.clam314.rxrank.model.HttpModelPresenterImpl;
import com.clam314.rxrank.model.PreferenceModePresenter;
import com.clam314.rxrank.model.PreferenceModePresenterImpl;
import com.clam314.rxrank.util.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by clam314 on 2017/4/1
 */

public class DataPresenterImpl implements DataPresenter {
    private HttpModelPresenter httpModelPresenter;
    private PreferenceModePresenter preferenceModePresenter;

    public DataPresenterImpl(){
        this.httpModelPresenter = new HttpModelPresenterImpl();
        this.preferenceModePresenter = new PreferenceModePresenterImpl();
    }

    @Override
    public void loadCategoryContents(Observer<List<Item>> observer, String category, int size, int pageNo) {
        Observable<List<Item>> observable = httpModelPresenter.loadCategory(category,size,pageNo);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadHistoryDay(Observer<List<String>> observer) {
        Observable<List<String>> observable = httpModelPresenter.loadHistoryDays();
        observable.subscribeOn(Schedulers.io())
                .map(new Function<List<String>, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull List<String> strings) throws Exception {
                        List<String> des = new ArrayList<>();
                        for (String s:strings){
                            //将后台返回格式为yyyy-MM-dd的日期转换成yyyy/MM/dd的格式
                            des.add(StringUtil.dateFormat(s));
                        }
                        return des;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadDayContents(Observer<CategoryGroup> observer, String day) {
        Observable<CategoryGroup> observable = httpModelPresenter.loadDay(day);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadCategoryRandomContents(Observer<List<Item>> observer, String category, int size) {
        Observable<List<Item>> observable = httpModelPresenter.loadCategoryRandom(category,size);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadHomeImage(Context context,Observer<ImageCache> observer) {
        Observable<ImageCache> observable = preferenceModePresenter.getHomeImageCache(context);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadHomeImageCache(final Context context,final String savePath) {
        Observable<List<Item>> observable = httpModelPresenter.loadCategoryRandom(Category.welfare,1);
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<List<Item>, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(@NonNull List<Item> items) throws Exception {
                        return httpModelPresenter.downloadFile(items.get(0).getUrl());
                    }
                })
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@NonNull ResponseBody responseBody) throws Exception {
                        if(writeResponseBodyToDisk(savePath,responseBody)){
                            long refreshTime = System.currentTimeMillis();
                            ImageCache cache = new ImageCache();
                            cache.setSavePath(savePath);
                            cache.setRefreshTime(refreshTime);
                            preferenceModePresenter.caveHomeImageCache(context,cache);
                        }
                    }
                });
    }

    private boolean writeResponseBodyToDisk(String savePath, ResponseBody responseBody){
        try {
            File saveFile = new File(savePath);
            if(!saveFile.exists()){
                saveFile.createNewFile();
            }
            InputStream inputStream = responseBody.byteStream();
            OutputStream outputStream = new FileOutputStream(saveFile);
            byte[] fileReader = new byte[2048];
            while (true){
                int read = inputStream.read(fileReader);
                if(read == -1){
                    break;
                }
                outputStream.write(fileReader,0,read);
            }
            outputStream.flush();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
