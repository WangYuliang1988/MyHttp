This is an Android project for learning **OkHttp**, **Retrofit**, and **RxJava**.

# 前言
最初该项目的目的是学习OkHttp、Retrofit、RxJava在处理网络请求时的结合用法。在写完使用OkHttp进行网络请求的一个小示例后，忽然觉得该示例可以套用MVVM架构重写一下。因为最初示例里面只有一个Activity，网络请求、UI交互、逻辑处理全在Activity里面完成，UI、数据、逻辑没有清晰的界定，而这正是架构可以解决的问题。

一开始考虑直接上官方的JetPack MVVM，但是又担心如果这样的话，会容易陷入LiveData、Room、Lifecycle这些库的技术细节里，不利于从整体上看清楚MVVM架构的本质。

因此决定自己动手写一个MVVM架构实现。照抄容易，自写困难。虽然是一个非常简单的示例，但在过程中确实也有很多纠结，发现了一些之前只看技术文档或者照搬教程时没有重视的内容。最突出的就是**数据驱动**的概念：当数据变化后，ViewModel需要**通知**View，然后由View自己完成界面更新，而不是由ViewModel通过**调用**View的方法完成界面更新。

现在看上面关于数据驱动的描述，会觉得理所当然，因为这是MVVM的本质，是每一个介绍MVVM的技术文档都会提到的内容。但如果没有自己动手，按照自己的理解把它转化成代码，印象就不会如此深刻。

自己动手实现后，又写了一版使用官方JetPack MVVM进行实现的示例，通过对比加深对JetPack MVVM的理解。

接着，回归初心，通过编写示例来学习OkHttp、Retrofit、RxJava在处理网络请示时的结合用法。

# My MVVM
位于mymvvm包下，一共五个类，其中Model、ViewActivity、ViewModel三个类，分别对应M、V、VM。
```
Model：负责数据的获取及储存，本示例中主要是通过网络请求获取数据；
ViewActivity：负责显示界面及接收用户操作事件，然后调用ViewModel完成具体的业务逻辑处理；
ViewModel：负责具体的业务逻辑处理，完成后通知ViewActivity更新界面。
```
为实现**通知**操作，定义了被观察者和观察者两个接口，ViewModel通过实现被观察者接口成为被观察者，ViewActivity通过实现观察者接口成为观察者。

# JetPack MVVM
完成自写的MVVM架构实现后，开始考虑使用Android官方提供的一系列JetPack MVVM库进行实现，通过对比加深对官方库的理解。

## 改写 Model
原计划采用Room对远程数据进行本地存储和查询，但一是感觉这样对理解架构没意义不大，二是懒惰心理（主要原因），所以没改，Model类基本没改动。

## 改写 ViewModel
首先想到的是使用LiveData来替换自实现的开发者模式，一来减少代码，二来可以具备自动取消观察、只对处于活跃状态的观察者发送通知等能力。

改写完后运行正常，这个时候有个问题就出现了，那ViewModel这个库是用来干嘛的，不用不也没什么问题？

仔细观察运行的App，当屏幕翻转后，原来通过网络请求获得的数据就消失了。若要保持数据，一种方法就是把数据存下来，当屏幕翻转后再拿出来用。ViewModel
库就实现了类似的功能，使用了ViewModel库，当屏幕翻转后，新的界面仍能获取到翻转前已经存在的数据，不需要再手动去处理了。

## 改写 View
对Activity的改写不大，且主要是跟随ViewModel的变化，调整对ViewModel实例的初始化、注册对LiveData的监听。

## 代码结构
位于jetpackmvvm包下，一共三个类，其中JetModel、JetViewActivity、JetViewModel，分别对应M、V、VM。

同时在Gradle文件中新增对LiveData和ViewModel两个库的依赖。

# OkHttp + Retrofit + RxJava
首先，使用OkHttp实现了功能，没什么问题。然后改用Retrofit实现，也没什么问题。最后结合使用OkHttp + Retrofit + RxJava，这时候出现两个疑问：

1. 即使不用OkHttp，只用Retrofit + RxJava也能实现，那么OkHttp的位置在哪里？
答：可以通过OkHttp控制基础网络通信，如设置连接超时时间、设置通用的Header等。

2. RxJava的作用体现的好像不那么明显？
答：从代码表面看，确实好像变化不大，但异步操作的逻辑更清晰了，而且可能是因为功能太简单，威力没能体现出来...好吧其实我也还没体会，有待日后再论吧。
## OkHttp
OkHttp是一个在Android和Java应用中处理网络请求的开源库，负责按照http协议要求实现真正的请求过程，用于替代HttpUrlConnection和Apache HttpClient。

## Retrofit
Retrofit是一个基于OkHttp封装的网络请求库，使用注解的方式来提供功能，侧重于处理请求发送的数据和响应的结果，具体的请求过程则交由OkHttp实现。

## RxJava
RxJava是一个可以让异步操作更加简单和清晰的开源库。

## 代码结构
位于Origin包下，一共三个类
```
OkHttpActivity：只用OkHttp实现功能；
RetrofitActivity：只用Retrofit实现功能；
OrrActivity：使用OkHttp + Retrofit + RxJava实现功能。
```