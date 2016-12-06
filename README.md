# Manga
**开源漫画阅读器，提供多个漫画来源。实现切换漫画来源、漫画推荐、漫画搜索、漫画收藏、漫画阅读、历史记录、下载漫画
等功能。最好在Android5.0以上运行,才能体现出更好的效果 - -**

### 项目
* 项目基于RxJava+Retrofit2+Glide+ButterKnife，结合MVP模式开发
* API由本人编写的Python项目提供，暂不提供源代码
* 本项目纯属学习交流使用，数据由非正常路径获得，不得用于商业用途

### 技术点
* 使用RxJava配合Retrofit做网络请求
* 整个项目使用MVP架构，对应model,view,presenter三个包
* 使用Glide加载图片和缓存图片
* 使用Material Design控件和动画

### TODO
* 下载功能完善
* 登录注册模块完善，提供第三方登录(QQ,微信)
* 阅读体验优化

### 首页预览
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/home_page_1.jpg?raw=true" width="200"/>
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/home_page_0.jpg?raw=true" width="200"/>
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/home_page_2.jpg?raw=true" width="200"/>
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/home_page_3.jpg?raw=true" width="200"/>

### 漫画页面
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/comic_detail.jpg?raw=true" width="280"/>
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/comic_page.jpg?raw=true" width="280"/>
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/search.jpg?raw=true" width="280"/>

### 历史记录
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/comic_history.jpg?raw=true" width="280"/>

### 下载页面
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/download_manager.jpg?raw=true" width="280"/>
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/download_detail.jpg?raw=true" width="280"/>

### 设置页面
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/setting.jpg?raw=true" width="280"/>
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/watch_setting.jpg?raw=true" width="280"/>

### 个人中心
<img src="https://github.com/TongmingWu/Manga/blob/master/screenshot/person_center.jpg?raw=true" width="280"/>

### MIT License

Copyright (c) 2016 TongmingWu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.