package com.aidlservice.demo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.utils.cutom.aidl.Book;
import com.utils.cutom.aidl.IBookManager;
import com.utils.cutom.aidl.IOnNewBookAddListenner;

import java.util.List;

public class OtherClientActivity extends Activity {
    private List<Book> bookList;
    private IBookManager iBookManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_client);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnect, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBookManager = IBookManager.Stub.asInterface(service);
            try {
                bookList = iBookManager.getBookList();
                iBookManager.rigisterListenner(onNewBookAddListenner);
                Log.e("OtherClientActivity", "测试" + bookList.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private IOnNewBookAddListenner onNewBookAddListenner = new IOnNewBookAddListenner.Stub()

    {

        @Override
        public void onNewBookArrivedListenner(Book book) throws RemoteException {
            Log.e("OtherClientActivity", "测试" +book.toString());
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnect);
        if (iBookManager!=null&&iBookManager.asBinder().isBinderAlive()){
            try {
                iBookManager.unrigisterListenner(onNewBookAddListenner);
                Log.e("OtherClientActivity", "解绑成功");
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e("OtherClientActivity", "解绑失败");
            }
        }
    }
}
