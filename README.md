# xyTalk-pc

Xy.Platform is A High Performance and Scalable Platform for enterprise communication and collaboration.
<br>Xy.Platform是一个高性能、可扩展的企业通信和协作平台。
<br>
<p>
XyTalk.pc is Windows/linux/Mac XMPP instant messaging client software.
<br>
XyTalk.pc是XMPP协议的即时通讯IM客户端软件，可用于Windows/linux/Mac.
<p>

## 技术选型 ##
<br>
<p>
<UL>
  <LI>通讯协议： XMPP（用于消息）、HTTP
  <LI>XMPP Server： Tigase，我们做了部分插件
  <LI>PC端UI： Swing（很low？知道intellij idea、Google Android Studio用什么开发的吗？）
  <LI>异步框架： java8内置CompletableFuture、或SwingWorker，原则是适合就好，不用搬用过重框架
  <LI>DB： MySQL（Server side）、SQLite（客户端）
  <LI>ORM：Mybatis
  <LI>皮肤：Darcula
  <LI>日志库：logback、slf4j-log4j12
/UL>


<p>

## TODO LIST ##
<br>
<p>
<b>登陆  </b>
<br>
1、登陆记住密码；
<br>
2、登陆自动登陆Jframe；
<br>
<p>

<b>消息  </b>
<br>
1、来消息声音提示
<br>
2、消息的回执状态处理和ui展示，“未读”、“已读”
<br>
<p>
<b>聊天窗</b>
<br>
1、截图时隐藏当前窗口
<br>
<p>

<b>UI</b>
<br>
1、编辑头像时更专业的截取器，支持缩放、位移。兼容vacrd的小文件（小于30K），对大图做压缩处理
<br>
<p>

## 更新日志 ##
<br>
<p>
<br>2018-05-06以前没有记录了，从6号开始更新
2018-05-06： <br>
<br>1、保存自身的Avater头像至XMPP服务端，并刷新用户MyInfoPanel区的头像
<br>2、刷新头像缓存；
<br>3、bugfix：修正SessionManager认证后UserCache.CurrentUserName的空值；

