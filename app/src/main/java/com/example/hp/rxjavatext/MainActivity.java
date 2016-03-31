package com.example.hp.rxjavatext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hp.rxjavatext.LoadMangager.LoadManager;
import com.example.hp.rxjavatext.model.Goods;
import com.example.hp.rxjavatext.model.GoodsList;
import com.example.hp.rxjavatext.model.Image;

import java.util.ArrayList;


import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.RetrofitError;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private ListView myListView;

    private ProgressBar myprogress;

    private MyAdapter adapter;
    private ArrayList<String> dates;
    private String mType = "Android";
    private Realm mRealm;
    private ArrayList<Image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRealm=Realm.getInstance(this);

        myListView = (ListView) findViewById(R.id.myListView);
        myprogress = (ProgressBar) findViewById(R.id.myprogress);
        initDate();
        adapter = new MyAdapter(images,dates, this);
        myListView.setAdapter(adapter);

    }

    private void initDate() {
        dates = new ArrayList<>();
        images=new ArrayList<>();
        LoadManager.getInstance(this)
                .getGoodLists(mType,20,1)
                .cache()
                .subscribeOn(Schedulers.newThread())//指定观察这运行的线程
                .observeOn(AndroidSchedulers.mainThread())//指定被观察者运行的主线sun
                .subscribe(GoodListObserver);

    }

    private rx.Observer<GoodsList> GoodListObserver = new rx.Observer<GoodsList>() {


        @Override
        public void onCompleted() {
            myprogress.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof RetrofitError) {

                RetrofitError error=(RetrofitError) e;

                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    Toast.makeText(MainActivity.this, "好像您的网络出了点问题", Toast.LENGTH_LONG).show();
                }
                if(error.getKind()==RetrofitError.Kind.HTTP){
                    Toast.makeText(MainActivity.this, "好像服务器出了点问题", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "莫名错误", Toast.LENGTH_LONG).show();
                }

            }
        }
        @Override
        public void onNext(GoodsList goodsList) {
            if (goodsList != null && goodsList.getResults() != null) {

                for (int i = 0; i < goodsList.getResults().size(); i++) {

                    dates.add(goodsList.getResults().get(i).getWho());
                }

                mRealm.beginTransaction();
                for(Goods goods:goodsList.getResults()){
                    Image image=Image.queryImageById(mRealm,goods.getObjectId());
                    if(image==null){

                        image=mRealm.createObject(Image.class);
                        Image.GoodCopyToImage(goods,image);
                    }
                }
                mRealm.commitTransaction();
            }

            RealmResults<Image> realmResults=mRealm.where(Image.class).findAll();
            Log.d("CCC","存储的大小="+realmResults.size());

            images.addAll(realmResults);

            adapter.notifyDataSetChanged();
        }
    };

}
