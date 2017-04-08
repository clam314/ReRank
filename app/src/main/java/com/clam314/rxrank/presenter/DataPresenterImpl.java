package com.clam314.rxrank.presenter;

import android.content.Context;

import com.clam314.rxrank.GlobalConfig;
import com.clam314.rxrank.entity.CategoryGroup;
import com.clam314.rxrank.entity.ImageCache;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.http.Category;
import com.clam314.rxrank.model.HttpModelPresenter;
import com.clam314.rxrank.model.HttpModelPresenterImpl;
import com.clam314.rxrank.model.PreferenceModePresenter;
import com.clam314.rxrank.model.PreferenceModePresenterImpl;
import com.clam314.rxrank.util.DeBugLog;
import com.clam314.rxrank.util.FileUtil;
import com.clam314.rxrank.util.StringUtil;

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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
    public void loadHomeImage(final Context context,Observer<ImageCache> observer) {
        DeBugLog.logWarning("imageCache","——loadHomeImage");
        Observable<ImageCache> observable = preferenceModePresenter.getHomeImageCache(context);
        observable.subscribeOn(Schedulers.io())
                .map(new Function<ImageCache,ImageCache>() {
                    @Override
                    public ImageCache apply(@NonNull ImageCache cache) throws Exception {
                        DeBugLog.logWarning("imageCache","——loadHomeImage -cache: refreshTime:"+cache.getRefreshTime()+" path:"+cache.getSavePath());
                        if(System.currentTimeMillis()-cache.getRefreshTime() > GlobalConfig.TIME_OUT_HOME_IMAGE_CACHE){
                            loadHomeImageCache(context, FileUtil.getAppExternalStorageDirectory());
                        }
                        return cache;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadHomeImageCache(final Context context,final String savePath) {
        DeBugLog.logWarning("imageCache","——loadHomeImageCache");
        Observable<List<Item>> observable = httpModelPresenter.loadCategoryRandom(Category.welfare,1);
        observable.subscribeOn(Schedulers.newThread())
                .flatMap(new Function<List<Item>, ObservableSource<Object[]>>() {
                    @Override
                    public ObservableSource<Object[]> apply(@NonNull List<Item> items) throws Exception {
                        final String url = items.get(0).getUrl();
                        return httpModelPresenter.downloadFile(url).map(new Function<ResponseBody, Object[]>() {
                            @Override
                            public Object[] apply(@NonNull ResponseBody responseBody) throws Exception {
                                DeBugLog.logWarning("imageCache","——loadHomeImageCache get responseBody");
                                return new Object[]{responseBody,FileUtil.getFileSuffix(url)};
                            }
                        });
                    }
                })
                .subscribe(new Consumer<Object[]>() {
                    @Override
                    public void accept(@NonNull Object[] result) throws Exception {
                        ResponseBody body = (ResponseBody) result[0];
                        String fileName = "image_cache_" + System.currentTimeMillis() + result[1];
                        if (writeResponseBodyToDisk(savePath, body, fileName)) {
                            long refreshTime = System.currentTimeMillis();
                            ImageCache cache = new ImageCache();
                            cache.setSavePath(savePath + File.separator + fileName);
                            cache.setRefreshTime(refreshTime);
                            preferenceModePresenter.caveHomeImageCache(context, cache);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        //不订阅Throwable的Consumer断网时会崩溃
                    }
                });
    }

    private boolean writeResponseBodyToDisk(String savePath, ResponseBody responseBody,String fileName){
        DeBugLog.logWarning("imageCache","——writeResponseBodyToDisk savePath:"+savePath+" fileName:"+fileName);
        try {
            File saveFile = new File(savePath,fileName);
            if(!saveFile.exists()){
                if(saveFile.createNewFile())DeBugLog.logDebug("imageCache","create the new imageCache file");
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
