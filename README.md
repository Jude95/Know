# Know

本来是想做一套API给学员玩的。最近有点空就自己也撸了撸。用了几个黑科技

[material-ripple](https://github.com/balysv/material-ripple) 和网易一样的ripple。  

[nucleus](https://github.com/konmik/nucleus) 一套用的比较顺的mvp框架,不过他有点bug自己改了改   

[material-dialogs](https://github.com/afollestad/material-dialogs)  十分好用的dialog库

[gradle-retrolambda](https://github.com/evant/gradle-retrolambda) AndroidStudio的lambda表达式

[fresco](https://github.com/facebook/fresco)  不是现在最屌的图片加载库吗

[SuperRecyclerView](https://github.com/Malinskiy/SuperRecyclerView) 比较好用的支持下拉上拉刷新recyclerview。不要再提Listview

###分包

用这套开发框架做APP只用替换moudle和model里的内容就行。

>app ——所有基类与管理类  
>config  ——APP配置  
>model ——数据管理提供者
> bean  ——javabean
>module  ——具体模块。里面详分不同模块  
>util  ——工具类  
>widget  ——公共组件  
