# 🍯 Sugar

![](https://user-gold-cdn.xitu.io/2019/5/27/16af991f7c7f0d50?w=1280&h=640&f=png&s=32575)

[![](https://jitpack.io/v/wobiancao/sugar.svg)](https://jitpack.io/#wobiancao/sugar)
[ ![bintray](https://api.bintray.com/packages/a420245103/maven/sugar/images/download.svg) ](https://bintray.com/a420245103/maven/sugar/_latestVersion)

需求：新项目只需5分钟接入，之后直接开撸，不用关心网络、图片、模式、稳定等问题，支持mvp一个activity对应多个presenter。
适用自己的才是最好的！

最近公司事情多，所以to do会稍后进行☺️ 有想要加的模块或计划，大家可以提到issues，我会关注的，并且会回复，希望把这个弄好
-------


更新日志
-------
- 2019-06-24 更新 1.0.1.5 anko ui注解初始化+自定义ConverterFactory
- 2019-06-17 更新 kotlin-mvp temeple
- 2019-06-14 更新 kotlin接入
- 2019-06-05 更新 不务正业之[`使用MotionLayout实现高德地图bottomSheets效果`](https://github.com/wobiancao/sugar/blob/master/demo/README.md)
- 2019-05-31 更新 1.0.1.3升级到androidx
- 2019-05-29 最新，新鲜temeple出炉，请拉到最后看效果

- ...

To do
--------
- anko temeple
- anko ✔️
- kotlin-mvp temeple ✔️
- kotlin 全面支持 ✔️
- 图片加载库更换策略，或者为了方便使用直接用一套写好的放入
- 常用控件（刷新，标题等等）
- more...

最新修改
-------

`1.0.1.5`
-------
`主要新增anko支持,通过注解解耦使用AnkoUi,不需要手动初始化，直接使用ankoUi`
- 顶部TYPE注解 
```
@AnkoInject(ui = xxAnkoUi::class) 
```
- 变量FIELD注解
```
    @AnkoVariable
    internal var ui : xxAnkoUi? = null
```
然后可直接使用`ui`这个变量，如下:

1. `GankAnkoActivity`
```
@AnkoInject(ui = GankActivityUi::class)
@Route(path = RouterPageContant.KT_ANKO)
@CreatePresenter(presenter = [GankPresenter::class])
class GankAnkoActivity : BaseAnkoActivity(), GankContract.IView {

    @PresenterVariable
    internal var mPresenter: GankPresenter? = null
    @AnkoVariable
    internal var ui : GankActivityUi? = null

    @SuppressLint("NewApi")
    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(ui?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "KtGankActivity"
    }

    override fun loadData() {
        mPresenter?.getFuliDataRepository("20", "1")
    }

    override fun bindData(data: MutableList<GirlsData>?) {
        val jsonStr = JSON.toJSONString(data)
        ui?.textInfo?.text = jsonStr
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
```
2. `GankAnkoFragment`
```
@AnkoInject(ui = GankFragmentUi::class)
@CreatePresenter(presenter = [TestGankPresenter::class])
class GankAnkoFragment: BaseAnkoFragment(),  GankContract.IView {

    @PresenterVariable
    internal var mPresenter: TestGankPresenter? = null
    @AnkoVariable
    internal var ui : GankFragmentUi? = null

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(true, 0.2f)
                .init()
    }



    override fun loadData() {
        mPresenter?.getFuliDataRepository("10", "1")
    }

    override fun bindData(data: MutableList<GirlsData>?) {
        val jsonStr = JSON.toJSONString(data)
        ui?.textInfo?.text = jsonStr
    }
}
```

`1.0.1.4`
-------

1. `SugarHandleSubscriber`  用于接口请求时候仅处理`onNext()`,因为在`SugarRepository`里面的`customObservable(Observable observable)`函数里面统一处理了错误异常抓取，一般情况下不需要再处理`OnError`当然可以重载函数的时候做处理
```
 .doOnError(throwable -> {
                    LogUtils.i("doOnError------" + throwable);
                    if (mIView != null) {
                        mIView.showLoadFailed();
                    }
                    if (rxErrorHandler != null){
                       //统一异常抓取
                       rxErrorHandler.getHandlerFactory().handleError((Throwable) throwable);
                    }

                });
```
2. 使用`SugarHandleSubscriber`
- java :
```
@Override
    public void getFuliDataRepository(String size, String index) {

        mModel.getFuliDataRepository(size, index)
                .subscribe(new SugarHandleSubscriber<List<GirlsData>>() {
                    @Override
                    public void onNext(List<GirlsData> girlsData) {
                        mView.bindData(girlsData);
                    }
                });
    }
```
- kotlin
```
   mModel.getFuliDataRepository(size, index)
                .subscribe({
                    mView.bindData(it)
                })
```

3. `1.0.1.4`版本之后 `Presenter`不支持在`Activity/Fragment`后写泛型，只支持注解
- java:
```
@CreatePresenter(presenter = GankPresenter.class)
public class GankActivity extends BaseActivity implements GankContract.IView{
    @PresenterVariable
    GankPresenter mPresenter;
}
```
- kotlin
```
@CreatePresenter(presenter = [GankPresenter::class])
class KtGankActivity : BaseActivity(), GankContract.IView{
    @PresenterVariable
    internal var mPresenter: GankPresenter? = null
}
```

### 实用到的库(排名不分先后)
[`Retrofit你懂的`](https://github.com/square/retrofit)

[`ImmersionBar状态栏工具`](https://github.com/gyf-dev/ImmersionBar)

[`ToastUtils 简单实用toast`](https://github.com/getActivity/ToastUtils)

[`RxErroHandler rxjava异常获取`](https://github.com/JessYanCoding/RxErrorHandler)

[`RetrofitUrlManager retrofit动态绑定url`](https://github.com/JessYanCoding/RetrofitUrlManager)

[`EasyMvp个简单强大且灵活的MVP框架`](https://github.com/EspoirX/EasyMvp)

[`AndroidUtilCode 强大的工具库`](https://github.com/Blankj/AndroidUtilCode)

[`RxLifecycle 为rxjava而生你懂的`](https://github.com/trello/RxLifecycle)

[`Gloading 深度解耦Android App中全局加载中、加载失败及空数据视图`](https://github.com/luckybilly/Gloading)

[`RxJava 不解释`](https://github.com/ReactiveX/RxJava)

[`RxAndroid 不解释`](https://github.com/ReactiveX/RxAndroid)

[`RxPermissions Android runtime permissions powered by RxJava2`](https://github.com/tbruyelle/RxPermissions)

[`Okhttp 不解释`](https://github.com/square/okhttp)

[`Gson 不解释`](https://github.com/google/gson)

[`Timber JakeWharton大神的日志打印工具`](https://github.com/JakeWharton/timber)

[`ARouter 阿里出的路由库`](https://github.com/alibaba/ARouter)

[`lottie`](https://github.com/airbnb/lottie-android)

[`...以及忘了加上的`]()

### 使用效果

![](https://user-gold-cdn.xitu.io/2019/5/28/16afcdd70255ad14?w=327&h=599&f=gif&s=708012)
`图片有压缩,可以下载demo apk进行体验`
[demo-debug.apk](https://github.com/wobiancao/sugar/blob/master/demo-debug.apk)
### 安装和依赖
3种选择
```
1、 git clone https://github.com/wobiancao/sugar.git
    implementation project(':sugarlibrary')

2、 implementation 'com.wobiancao:sugarlibrary:{version}'

3、 allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
implementation 'com.github.wobiancao:sugar:{version}'
```



### 统一配置

#### 创建DemoConfigure 继承于SugarConfigure 重载相关方法即可：
- 网络配置(Retrofit和okhttp3)[`Retrofit`](https://github.com/square/retrofit)请求，rxbus等）
- 状态栏默认颜色[`ImmersionBar`](https://github.com/gyf-dev/ImmersionBar)
- toast样式[`ToastUtils`](https://github.com/getActivity/ToastUtils)你可以不用设置，有默认的
- [`RxErroHandler rxjava异常获取`](https://github.com/JessYanCoding/RxErrorHandler)
-  网络设置直接见代码即可，跟retrofit一样的配置,写了一些默认的拦截器可继承后使用也可自己写,host可以动态配置[`RetrofitUrlManager`](https://github.com/JessYanCoding/RetrofitUrlManager)
-  解耦页面状态切换[`Gloading` ](https://github.com/luckybilly/Gloading)你可以不用设置，有默认的
-  默认等待框[`BaseLoadingDialog`](https://github.com/wobiancao/sugar/blob/master/sugarlibrary/src/main/java/com/sugar/sugarlibrary/widget/BaseLoadingDialog.java)你可以不用设置，有默认的
- 更多详见[`AppConfigureDelegate`](https://github.com/wobiancao/sugar/blob/master/sugarlibrary/src/main/java/com/sugar/sugarlibrary/base/config/AppConfigureDelegate.java)
```
public class DemoConfigure extends SugarConfigure {


    public DemoConfigure(Application application) {
        super(application);
    }

    @Override
    public ResponseErrorListener getErrorResponse() {
        return new ResponseErrorListener() {
            @Override
            public void handleResponseError(Context context, Throwable t) {
                LogUtils.i("捕获异常---" + t.getMessage());
                ToastUtils.show("发生异常---" + t.getMessage());
            }
        };
    }

    @Override
    public int getStatusColor() {
        return R.color.colorPrimary;
    }

    @Override
    public AppHttpSetting getHttpSetting() {
        return AppHttpSetting
                .builder()
                .with(mApplication)
                //设置初始的baseUrl host
                .setBaseUrl(Gank.HOST)
                //动态修改baseUrl 具体看https://github.com/JessYanCoding/RetrofitUrlManager
                .putDomain(Wan.DOMAN, Wan.HOST)
                //是否打印网络请求日志 默认否
                .setHttpLog(true)
                //百度Stetho即可 网络监测等 默认否
                .setHttpMoniter(true)
                //设置缓存时间 默认60s
                .setCacheMaxTime(65)
                //设置连接超时 默认20s
                .connectTimeout(20)
                //设置读取超时 默认20s
                .readTimeout(20)
                //设置写入超时 默认20s
                .writeTimeout(20)
                //请求header
                .addHeaderInterceptor(getHeader())
                //添加请求明文公共参数
                .addCustomHeaderInterceptor(getCustomHeader())
                //token过期等请求成功处理 一般不需要处理
//                .addExceptionInterceptor(getExceptionInterceptor())
                //其它拦截
//                .addInterceptor(xx)
//                .addNetworkInterceptor(xxx)
//                配置自己的缓存
//                .cache(xx)
                //甚至另外写一套自己的okhttp builder 也行
//                .setOkHttpBuilder(xxx)
                .build();
    }

    @Override
    public IToastStyle getToastStyle() {
        return new ToastStyle();
    }
}

```


- 创建DemoApplication继承于 LibApplication < S extends SugarConfigure > 重写initConfigure()初始化配置即可,详见[`DemoApplication`](https://github.com/wobiancao/sugar/blob/master/demo/src/main/java/com/sugar/demo/core/DemoApplication.java)
```
public class DemoApplication extends LibApplication<DemoConfigure> {

    @Override
    protected void initConfigure() {
        mConfigure = new DemoConfigure(this);
    }

    @Override
    protected void init() {

    }

}

```
### 网络请求统一配置
- `问题： `我们使用[`RetrofitUrlManager`](https://github.com/JessYanCoding/RetrofitUrlManager) 解决了retorfit动态配置baseUrl的问题，但是每个域名或者说每个接口返回参数封装等的可能不统一（这种情况一般不会出现在公司项目）比如我这个app要展示[`Gank.io`](http://gank.io)和[`WanAndroid`](http://wanandroid.com)的界面,这样就是两个网络请求封装，使用sugar可以快速解决此类问题；
- 使用之前先看源码[`SugarRepository`](https://github.com/wobiancao/sugar/blob/master/sugarlibrary/src/main/java/com/sugar/sugarlibrary/http/SugarRepository.java)
```
/**
 * @author wobiancao
 * @date 2019/5/20
 * desc :
 */
public class SugarRepository {
    /**
     * 0 没loading 1 dialog形式  2page形式
     */
    protected final static int LOADING_TYPE_NULL = 0;
    /**
     * 0 没loading 1 dialog形式  2page形式
     */
    protected final static int LOADING_TYPE_DIALOG = 1;
    /**
     * 0 没loading 1 dialog形式  2page形式
     */
    protected final static int LOADING_TYPE_PAGE = 2;
    protected BaseIView mIView;

    public SugarRepository(BaseIView IView) {
        mIView = IView;
    }

    protected Observable addObservable(Observable observable) {
        if (mIView == null) {
            return null;
        }
        return customObservable(observable);
    }

    protected Observable addObservable(Observable observable, int loadingType) {
        if (mIView == null) {
            return null;
        }
        return customObservable(observable)
                .doOnSubscribe(disposable -> {
                    if (loadingType > 0) {
                        if (loadingType == LOADING_TYPE_DIALOG) {
                            mIView.showDialogLoading();
                        } else {
                            mIView.showLoading();
                        }
                    }
                });
    }

    private Observable customObservable(Observable observable) {
        return observable
                .compose(mIView.getProvider().bindToLifecycle())
                .retryWhen(new RetryWithDelay(2, 2))
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mIView != null) {
                        mIView.hideDialogLoading();
                    }
                })
                .doOnNext(o -> {
                    LogUtils.e("doOnNext------" + o);
                    if (mIView != null) {
                        mIView.showLoadSuccess();
                    }
                })
                .doOnError(throwable -> {
                    LogUtils.e("doOnError------" + throwable);
                    if (mIView != null) {
                        mIView.showLoadFailed();
                    }
                });
    }
}
```
 - addObservable(Observable observable)不会使用任何loading效果，
 addObservable(Observable observable, int loadingType) loadingType : 0 没loading 、1 dialog形式 、2 page形式

 -  Repository首先有个契约类，[`RepositoryContract`](https://github.com/wobiancao/sugar/blob/master/demo/src/main/java/com/sugar/demo/http/repository/RepositoryContract.java)
 xxxModel为需要增加的一个域名接口，统一配置apiService、请求函数、相应的transformer
 ```
 /**
 * @author wobiancao
 * @date 2019-05-21
 * desc :
 */
public class RepositoryContract {

    /**
     * gank.io
     */
    public interface GankModel  {
        Gank getService();
        /**
         * Transformer 需要处理api返回值包装的加上即可
         * @param <T>
         * @return
         */
        <T> ObservableTransformer<GirlsResult<T>, T> gankTransformer();

        Observable<List<GirlsData>> getFuliDataRepository(String size, String index);
    }

    /**
     * wanandroid
     */
    public interface WanModel{
        Wan getService();
        /**
         * Transformer 需要处理api返回值包装的加上即可
         * @param <T>
         * @return
         */
        <T> ObservableTransformer<WanResult<T>, T> wanTransformer();


        Observable<WanData> getWanArticleList(String index);
    }
}
 ```
  - 使用，如[GankRepository](https://github.com/wobiancao/sugar/blob/master/demo/src/main/java/com/sugar/demo/http/repository/GankRepository.java)
 ```
 /**
 * @author wobiancao
 * @date 2019/5/20
 * desc :
 */
public class GankRepository extends SugarRepository implements RepositoryContract.GankModel {


    public GankRepository(BaseIView IView) {
        super(IView);
    }

    @Override
    public Gank getService() {
        return AppHttpClient.getInstance().initService(Gank.class);
    }


    @Override
    public <T> ObservableTransformer<GirlsResult<T>, T> gankTransformer() {

        return upstream -> upstream
                .flatMap((Function<GirlsResult<T>, ObservableSource<T>>) tGirlsResult -> {
                    if (tGirlsResult == null) {
                        return Observable.error(new HttpException("返回值为null"));
                    }
                    if (!tGirlsResult.error) {
                        return Observable.just(tGirlsResult.results);
                    } else {
                        return Observable.error(new HttpException("接口异常"));
                    }
                });

    }

    @Override
    public Observable<List<GirlsData>> getFuliDataRepository(String size, String index) {
        return addObservable(getService()
                .getFuliData(size, index)
                .compose(gankTransformer()), LOADING_TYPE_PAGE);
    }

}

 ```
 ### 最后mvp创建（`之后会写相应的Template` ⬅️已写好）
 - 以[`WanActivity`](https://github.com/wobiancao/sugar/blob/master/demo/src/main/java/com/sugar/demo/ui/WanActivity.java)举例
 - 构成为 [`WanContract契约`](https://github.com/wobiancao/sugar/blob/master/demo/src/main/java/com/sugar/demo/ui/mvp/wan/WanContract.java)、[`WanPresenter`](https://github.com/wobiancao/sugar/blob/master/demo/src/main/java/com/sugar/demo/ui/mvp/wan/WanPresenter.java)
 好了，完了，结束。






![](https://user-gold-cdn.xitu.io/2019/5/28/16afc95055a35ffc?w=198&h=198&f=png&s=34998)




- =。=假的，接下来看代码

`WanContract`
```
/**
 * @author wobiancao
 * @date 2019-05-21
 * desc :
 */
public class WanContract {
    public interface PView{

        void getWanArticleList(String index);
    }

    public interface IView extends BaseIView {
        /**
         * 绑定列表数据
         * @param data
         */
        void bindData(WanData data);
    }
}
```
`WanPresenter`
```

/**
 * @author wobiancao
 * @date 2019-05-21
 * desc :
 */
public class WanPresenter extends BasePresenter<WanContract.IView, WanRepository> implements WanContract.PView {

    @Override
    protected void initRepository() {
        mModel = new WanRepository(mView);
    }

    @Override
    public void getWanArticleList(String index) {
        mModel.getWanArticleList(index)
                .subscribe(new ErrorHandleSubscriber<WanData>(rxErrorHandler) {
                    @Override
                    public void onNext(WanData wanData) {
                        mView.bindData(wanData);
                    }

                });
    }


}

```
- 就是这么简单，最后就是在view层如何使用了，老规矩先看代码[`WanActivity`](https://github.com/wobiancao/sugar/blob/master/demo/src/main/java/com/sugar/demo/ui/WanActivity.java)
```
/**
 * @author wobiancao
 * @date 2019-05-21
 * desc :
 */
@CreatePresenter(presenter = WanPresenter.class)
public class WanActivity extends BaseActivity<WanPresenter> implements WanContract.IView {
    @PresenterVariable
    WanPresenter mPresenter;
    TextView mInfoView;
    Toolbar mToolbar;
    @Override
    protected int getContentView() {
        return R.layout.gank_activity_list;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mInfoView = findViewById(R.id.tv_info);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("WanAndroid");
        }
    }

    @Override
    public void loadData() {
        mPresenter.getWanArticleList("1");
    }

    @Override
    public void bindData(WanData data) {
        String jsonStr = new Gson().toJson(data);
        mInfoView.setText(jsonStr);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
```
#### 是的Presenter创建只需要注解即可，并且`支持多个presenter`
#### 这里要万分感谢[`EasyMvp一个简单强大且灵活的MVP框架`](https://github.com/EspoirX/EasyMvp)
- 首先，单个presenter

```
@CreatePresenter(presenter = WanPresenter.class)
public class WanActivity extends BaseActivity<WanPresenter> implements WanContract.IView
```
获取presenter变量两种方式

 1、通过注解
```
@PresenterVariable
WanPresenter mPresenter;
```
2、通过getPresenter()函数
```
xxActivity extends BaseActivity<xxPresenter>...

xxPresenter getPresenter()
```
- 多个prenenter
就只有通过注解获得变量了
```
@CreatePresenter(presenter = {xxPresenter1.class, xxPresenter2.class})
xxActivity extends BaseActivity...

@PresenterVariable
xxPresenter1 mPresenter1;

@PresenterVariable
xxPresenter2 mPresenter2;
```



### mvp Template完成
- 效果图

![](https://user-gold-cdn.xitu.io/2019/5/30/16b0662a93e257ce?w=419&h=286&f=gif&s=1173861)
![](https://user-gold-cdn.xitu.io/2019/5/30/16b0682d74244bc2?w=1918&h=1634&f=png&s=504133)
- 使用见图解
![](https://user-gold-cdn.xitu.io/2019/5/30/16b065ee6afcd298?w=1602&h=1346&f=png&s=227964)
![](https://user-gold-cdn.xitu.io/2019/5/30/16b065e960f6c8b9?w=1600&h=1344&f=png&s=221002)

#### 使用步骤：
1. 下载源码目录在 Sugar/SugarMvpTemplate

![](https://user-gold-cdn.xitu.io/2019/5/30/16b0665b9d53c841?w=838&h=404&f=png&s=62417)

2. 把两个文件夹放入`{Android Studio installation dir}\plugins\android\lib\templates\activities\`路径下

3. 重启Android studio即可使用



About me
--------
* **Email**: <a420245103@gmail.com>
* **掘金**: <https://juejin.im/user/568be89760b24d71fed19d2b>
* **简书**: <https://www.jianshu.com/u/114bbbfb977f>
* **apkbus**: <http://www.apkbus.com/?496060>
* **github**: <https://github.com/wobiancao>


License
--------
```
Copyright 2019, wobiancao

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

```
