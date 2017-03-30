package util;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by clam314 on 2017/3/30.
 */

public class RxBus {

    private final Subject<Object> mBus;

    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
    }

    private static class Holder{
        private final static RxBus instance = new RxBus();
    }

    public static RxBus getDefault(){
        return Holder.instance;
    }

    public void post(Object o){
        mBus.onNext(o);
    }

    public boolean hasObservable(){
        return mBus.hasObservers();
    }

    public <T>Observable<T> tObservable(Class<T> type){
        return mBus.ofType(type);
    }
}
