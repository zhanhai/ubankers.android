# ubankers andorid代码重构

### 目的
本次重构，是为了解决之前代码架构中存在的问题：

- 大多数时候没有正确区分VIEW和MODEL，业务逻辑在`Activity/Fragment/View`中直接实现,显示逻辑和业务逻辑纠结在一起，Activity变成一个上帝类（**注意：IOS中的ViewController也容易出现类似问题，请参见唐巧的相关博客**），动辄几百行，难于维护和测试。
	
- 有时我们抽象出了Model层面的Service类，负责调用后台API，但是在如何同View进行交互，如何把Model数据提供给View等方面，如何异步调用API，如何依赖公共组件(session，app)等方面，架构/框架层面没有提供足够支撑，导致代码质量不可控，完全取决于开发人员的开发水平。

###重构设计
这里简单介绍一下重构的设计思路，在另外的文档中(**准备中**)会提供更详细的说明。

####MVP
MVP是`model view presenter`的简称。MVP中，M是指后台提供的API以及涉及的资源；V是指显示模型的组件；P则是服务于V的组件，响应V的要求同M交互，并将M反馈给V。 <br>

MVP和MVC比较，两者最大的区别就是：MVC中的C与VIEW无关，多个VIEW可以共享一个CONTROLLER；而在MVP中,PRESENTER只服务于特定的VIEW，VIEW和PRESENTER之间是一一对应的关系。更多信息可以参见：[MVC和MVP](
http://stackoverflow.com/questions/2056/what-are-mvp-and-mvc-and-what-is-the-difference)。<br>

在我们的框架中，Activity或者Fragment并不是View，Activity/Framgent的职责包括：

- 管理和显示相关的资源（包括View)；
- 接收用户和系统事件并转发给Presenter；
- 提供显示服务供View调用。 

而View的职责从Activity中剥离出来后，有几个好处：

- Presener和Activity之间没有直接的循环依赖
- 和View相关的模型(ViewModel)在View中维护，简化了Activity 
- View的代码里不再有和Android组件相关的东西，便于测试




在我们设计中，用户和组件的交互流程如下为：<br> 
		
			User --(Event)--> Activity --(转发事件)--> P --[M]--> V --> Activity


####组件生命周期
我们框架中的生命周期管理模型是：View的生命周期由Activity负责，完全和Activity同步；而Presenter的生命周期不受Activity影响，实际上一直存在内存中，直到APP退出。<br>

由于View和Presenter的生命周期并不一致，所以我们需要在Presenter中处理View的生命周期事件：在View创建时关联，在View销毁时解绑。另外，API异步返回结果时，我们需要处理View不存在或者View已被重新创建的情况。在实现是，我们是通过Rx java提供的`combineLatest`操作符来实现的。<br>

框架提供的基类(`MvpActivity`）实现了Activity对View的管理。


####使用Rx java调用API
Rx java最大的好处就是提供了声明式的运算符，便于处理多个异步请求。比如：需要同时调用两个API，只有当这两个API都返回成功才算完成一次调用，我们可以使用`zip`运算符来构造`Observable`。<br>

Rx java还简化了线程的处理，通过`subscrieOn`和`observeOn`就可以定义异步计算的工作线程。


在重构中，我们使用了Retrofit作为rest框架，一方面是它支持Rx Observable, 另一方面也是因为它提供了强类型的API定义，简化和规范了对HTTP API的调用。

####依赖注入
代码中，有许多需要全局共享的组件或者对象，比如：`Context`、`Session`、`Cookie`等。原来的做法是把他们放到App对象中，作为单例或者静态属性暴露，但是这种做法不利于单元测试。所以我们引入Dagger2作为依赖管理框架。

另外，使用Bufferknife框架简化Activity对资源的绑定管理

###代码
本次重构把原来的`ProductDetailActivity`以及与之相关的代码按照新的模式，利用新的框架进行了重写。<br><br>
项目已经移植到android stuio, 使用gradle构建，可以在android studio查看和运行项目。<br><br>

代码结构如下：<br>

	com.ubankers				----  所有的代码在新的包
	|
	|	-	mvp					---- 框架相关代码
			|
			|	-	presener
			|	-	view
	|
	|	-	app					---- 所有和应用相关的代码
			|	
			|	-	base		---- 公共组件
			|
			|	-	member		---- 会员模块
			|
			|	-	product		---- 产品模块


在产品模块的目录中，我们包含了产品相关的API定义以及更细粒度的业务模块(这里只有产品详情模块）<br>
		
	product
	|
	|	-	ProductModule.java	---- 定义product模块输出的component（dagger2依赖框架) 
	|
	|	-	model				---- Product相关的API接口以及使用的bean
	|	
	|	-	detail				---- 产品详情模块
			|	
			|	-	reserve		---- 产品详情预约模块
			|	-	share		---- 产品详情分享模块
			|
			|	-	ProductDetailActivity.java		//产品详情Activity
			|	-	ProductDetailPresenter.java		//产品详情Presenter
			|	-	ProductDetailView.java			//产品详情View
			|	-	ProductDetailModel.java			//产品详情视图对应的模型
			|	-	ProductDetailComponent.java		//定义产品详情依赖管理
			|	-	ProductDetailModule.java		//定义产品详情依赖组件
			|	-	ProductDetailWebview.java		//产品详情的WebView控件