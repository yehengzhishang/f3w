# buildSrc
## 坑 1 ： 文件夹(module)命名
buildSrc  
+ ~~B 不要写~~
+ S 要大写
+ 别想玩的太花，直接把名字复制一下

## 坑 2 ：META-INF.gradle-plugins 的显示问题
新建package/dir的时候 ，两个路径中间是 "/",而不是idea上面展示的“.”  
buildSrc/src/main/resources/META-INF/gradle-plugins/

## 坑 3 ： gradle 新API  : plugins
    plugins{
      id "plugin's id"
    }
在buildSrc中的plugin虽然可以通过import导入but,并没有id,即使已经完成.properties文件
没有发布的plugin当前只能通过apply plugin 导入 

## 坑 4 ：uploadArchives 无效
需要在setting.gradle里面加入':buildSrc' 原因未知 