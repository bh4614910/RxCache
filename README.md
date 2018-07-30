# 简介
提供Android缓存功能，包括对`SD卡`，`内存`、`Sharedpreference`以及同时存储`SD卡`和`内存`的双层缓存操作，缓存对象包括：`实现序列化的对象`，`Bitmap`以及`字符数组`。
详细的代码说明[https://www.jianshu.com/p/a16ee1ff4da1](https://www.jianshu.com/p/a16ee1ff4da1)
# 使用
导入项目依赖

```
    implementation "io.reactivex:rxandroid:1.2.1"
    implementation "io.reactivex:rxjava:1.1.6"
```
在调用缓存API之前需要初始化缓存配置，推荐在Application当中进行初始化.

```
        //初始化缓存配置，包括磁盘缓存路径，缓存大小，内存缓存大小，加密策略等。
        // 最后调用.install(this)方法完成初始化
        CacheInstaller.get()
                .configDiskCache("TestCache", 50 * 1024 * 1024, 1)
                .install(this);
```
完成初始化之后就可以正常使用缓存操作了。
### 存储
项目本身一共两种缓存的调用方式：
- 直接在项目当中进行链式的调用。
- 一种是类似于retrofit的接口调用方式。

存储的对象可以是`实现序列化的对象`，`Bitmap`以及`字符数组`。以缓存`bitmap`为例，看一下调用实例：
###### 调用方式一
```
/**
 * 定义接口
 */
public interface TestInerface {
    //注解标明请求方式，超时时间等等
    //method设置当前操作为put，调用缓存到SD卡以及内存当中的双层缓存
    @Method(methodType = MethodType.PUT,cacheType = CacheType.TWO_LAYER)
    //设置过期时间为1天
    @Lifecycle(time = 1,unit = TimeUnit.DAYS)
    <T> Observable<Boolean> putData( @CacheKey String key,@CacheValue T value, @CacheClass Class<T> clazz);
}

//调用缓存存储bitmap
TestInerface testInerface = RetrofitCache.create(TestInerface.class);
testInerface.putData("testKey", bitmap, Bitmap.class).observeOn(AndroidSchedulers.mainThread())
.subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Toast.makeText(TestActivity.this, aBoolean + "", Toast.LENGTH_SHORT).show();
                    }
                });
```
整个存储过程可分为两步：
1. 定义接口，并通过注解标明请求方式，请求参数等。
2. 在项目中调用缓存API。

整个API的调用过程与Retrofit很相似，在定义接口时的注解说明如下：

注解 |类型| 说明
---|--- |---
@Lifecycle | 方法注解 | 设置过期时间，包括时长和单位，存储时调用
@Method | 方法注解 | 设置缓存方法以及存储方式
@ShareName | 方法注解 | sharedPreference缓存时的文件名
@Strategy | 方法注解 | 设置超时策略,读取缓存时调用
@CacheClass | 参数注解 | 设置缓存类，标注一个Class对象
@CacheKey | 参数注解 | 设置缓存的key值，标注一个String对象
@CacheValue | 参数注解 | 设置缓存内容

###### 调用方式二
直接通过链式调用

```
        //调用put方法存储数据
        RxCache.get().setTimeout(1, TimeUnit.DAYS)
                .putData2TwoLayer("diskKey", bitmap).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Toast.makeText(TestActivity.this, aBoolean + "", Toast.LENGTH_SHORT).show();
                    }
                });
            //setTimeout方法设置超时时间
            //putData2TwoLayer调用双层缓存，参数为缓存的key值以及缓存内容
```
调用存储方法`putXX`后返回一个`Observable<Boolean>`对象，当返回true时代表缓存成功，返回false代表缓存失败。

两种方法各有利弊
- 方式一方便对缓存的管理，并省去在项目中对缓存策略等的配置内容。
- 方式二调用方式更直接，代码也相对更少一些。

> 注意：无论哪种调用方式，都需要先初始化配置信息。

### 读取
读取方式和存储类似，也分为两种，详细调用内容不再赘述，直接看代码。

```
//----------------------方式一------------------------------
//定义接口
public interface TestInerface {
    //注解标明请求方式，超时策略等等
    //请求方式为get，读取对象为从SD卡中读取
    @Method(methodType = MethodType.GET,cacheType = CacheType.DISK)
    //设置超时策略，当数据超时时返回null
    @Strategy(key = ExpirationPolicies.ReturnNull)
    <T> Observable<T> getData(@CacheKey String key, @CacheClass Class<T> clazz);
}
testInerface.getData("testKey",Bitmap.class).observeOn(AndroidSchedulers.mainThread())
.subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap s) {
                        if (s != null) {
                            testImage.setImageBitmap(s);
                        } else {
                            Toast.makeText(TestActivity.this, "数据为null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//----------------------方式二------------------------------
 RxCache.get().getDataTwoLayer("diskKey", Bitmap.class).observeOn(AndroidSchedulers.mainThread())
 .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap s) {
                        if (s != null) {
                            testImage.setImageBitmap(s);
                        } else {
                            Toast.makeText(TestActivity.this, "数据为null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

```
读取缓存会返回一个Observable对象，通过subscribe()订阅后可以拿到返回的数据，并进行操作。
> 注意：默认执行subscribe()的线程为调用时所在线程，如果需要修改线程，需自行调用observeOn()方法修改调用线程。

另外还有`删除`和`清空缓存`等API，调用方式与`存储`、`读取`类似，省略这部分内容，感兴趣的可以自己下载试一下。

# 架构设计
说完对整个API的使用，再来详细看一下整个缓存的项目结构。
![CacheUML.png](https://upload-images.jianshu.io/upload_images/10294405-18bd8c839722c5e0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

