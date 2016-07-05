package com.aidlservice.demo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

//import com.utils.cutom.aidl.Book;

import com.utils.cutom.aidl.Book;
import com.utils.cutom.aidl.IBookManager;
import com.utils.cutom.aidl.IOnNewBookAddListenner;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by cys on 2016/6/30.
 */
public class BookManagerService extends Service{
    private AtomicBoolean mIsServiceDestroy= new AtomicBoolean();
    private CopyOnWriteArrayList<Book> mBookList=new CopyOnWriteArrayList<>();
    //这个会出现解绑失败
    //private CopyOnWriteArrayList<IOnNewBookAddListenner> mListenner =new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookAddListenner> mListenner =new RemoteCallbackList<>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    private Binder mBinder = new IBookManager.Stub(){
        @Override
        public List<Book> getBookList() throws RemoteException {
            SystemClock.sleep(4000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void rigisterListenner(IOnNewBookAddListenner listenner) throws RemoteException {

              mListenner.register(listenner);
        }

        @Override
        public void unrigisterListenner(IOnNewBookAddListenner listenner) throws RemoteException {
         // if (mListenner.contains(listenner)){
            //   Log.e("BookManagerService--->", "解绑成功");
                mListenner.unregister(listenner);
            //}else {
               // Log.e("BookManagerService--->", "解绑失败");
            //}

        }
    };
    
   private MyThread myThread;
    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book("设计思想",120));
        mBookList.add(new Book("数学与算法结合",122));
        myThread =new MyThread();
        myThread.start();
      //  Log.e("BookManagerService--->","运行啦");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestroy.set(true);
    }

    private void onNewBookArrived(Book book){
        mBookList.add(book);
        int num=mListenner.beginBroadcast();
        for (int i = 0; i < num; i++) {
            try {
                mListenner.getBroadcastItem(i).onNewBookArrivedListenner(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mListenner.finishBroadcast();
    }
    private class MyThread extends  Thread{
        @Override
        public void run() {
            super.run();
            while (!mIsServiceDestroy.get()){
                try {
                   // Log.e("BookManagerService--->","进入线程");
                    Thread.sleep(5000);
                    int bookId=mBookList.size()+1;
                    Book book=new Book("书本"+bookId,bookId);
                    onNewBookArrived(book);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
