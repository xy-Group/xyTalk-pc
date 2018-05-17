<H1>XyTalk-pc 企业协作通讯系统</H1>

Xy.Platform is A High Performance and Scalable Platform for enterprise communication and collaboration.
<br>Xy.Platform是一个高性能、可扩展的企业通信和协作平台。包含通讯服务（XMPP协议）、客户端（PC、Android、iOS）、Web门户（用于集成企业应用）、WebAPI（用于企业扩展调用）。
<br>
<p>
XyTalk.pc is Windows/linux/Mac XMPP instant messaging PC client software.
<br>
XyTalk.pc是XMPP协议的即时通讯IM客户端PC软件，可用于Windows/linux/Mac.
<br>
联系邮箱：475660@qq.com
<br>
联系微信：wx13710637136
<p>
<H2>## 技术相关 ##</H2>

<br>
<UL>
  <LI>开发语言：Java8
  <LI>通讯协议： XMPP（用于消息）、HTTP
  <LI>通讯Server： Tigase + Nginx + Kafka + Zookeeper，针对XMPP和企业业务我们做了部分定制，如离线文件、组织架构同步服务、企业集成登陆验证服务、消息同步、消息撤回、消息翻译、流程机器人等服务。注意：不支持Openfire服务器
  <LI>OSGi service platform： Apache Felix
  <LI>WebServer Portal & API： Spring、Shiro、Activiti、Websocket 
  <LI>PC端UI： Java Awt/Swing（very low？知道intellij idea、Google Android Studio用什么开发的吗？）
  <LI>Swing皮肤：Darcula
  <LI>异步框架： Java8内置CompletableFuture、SwingWorker，原则是适合就好，不必过重
  <LI>缓存：客户端堆内缓存，服务器端Ehcache
  <LI>DB： MySQL（Server side）、SQLite（客户端）
  <LI>ORM：Mybatis
  <LI>日志库：logback、slf4j-log4j12
  <LI>其他客户端：Android、iOS、Web
</UL>
<br>
<p>
<H2>## TODO LIST 待开发项  ##</H2>

 
<H4>登陆</H4>
<br>
1、登陆记住密码；
<br>
2、登陆自动登陆Jframe；
<br>
<H4>消息</H4>
<br>
1、来消息声音提示
<br>
2、tofix：MUC可能会重复推送已读消息，因为tigase离线消息持久化后不销毁，需要从服务器端解决
<br>
3、消息的回执状态处理和ui展示，“未读”、“已读”
<br>
4、消息撤回
<br>
<H4>聊天窗</H4>

<br>
1、截图时隐藏当前窗口
<br>

<H4>UI</H4>

<br>
1、编辑头像时更专业的截取器，支持缩放、位移。兼容vacrd的小文件（小于30K），对大图做压缩处理
<br>
2、在登陆后mainframe出来前增加一个过场frame，用于loading，改善用户体验
<br>
<p>
<H2>## 更新日志 ##</H2>
2018-05-06以前没有记录了，从6号开始更新
<br>2018-05-06： 
<br>1、保存自身的Avater头像至XMPP服务端，并刷新用户MyInfoPanel区的头像
<br>2、刷新头像缓存；
<br>3、bugfix：修正SessionManager认证后UserCache.CurrentUserName的空值；
<p><br>2018-05-07： 
<br>1、smack换用4.2.4，替换调用代码
<br>2、bugfix：修正因chat state消息造成的未读消息UI错误
<p><br>2018-05-07： 
<br>1、Smack里的RoomInfo并没有管理员和拥有者信息，MucRoomInfo加入以进行成员的持久化
<br>2、mucInvitation增加判断条件
<br>3、mucInvitation增加收到邀请后的群组DB持久化
<p><br>2018-05-09： 
<br>1、bugfix：muc消息在当前聊天窗无刷新
<br>2、bugfix：muc离线消息重复的ui处理
<br>3、muc消息抵达的chatPanel处理
<p><br>2018-05-10： 
<br>1、縮減了發送消息的UI更新速度
<p><br>2018-05-11： 
<br>1、启动进行muc的订阅，对原xmpp muc的邀请模式进行自动化处理，抛弃原来发消息即邀请群成员的模式；
<br>2、创建muc群组
<p><br>2018-05-16： 
<br>1、roster禁用的情况下获取fulljid（用于si文件）
<br>2、在线文件发送
<br>3、在线文件接收
<p><br>2018-05-17： 
<br>1、XMPP 离线文件扩展
<br>2、和离线文件机器人交互，离线文件发送和接收

