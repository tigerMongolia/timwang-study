#### 一、YApi介绍

YApi旨在为开发、产品、测试人员提供更优雅的接口管理服务。可以帮助开发者轻松创建、发布、维护 API，包括提供了一系列可视化接口管理，还有自动化测试

##### 1.1 自动化测试的场景及缺点

传统的接口自动化测试成本高，大量的项目没有使用自动化测试保证接口的质量，仅仅依靠手动测试，是非常不可靠和容易出错的。

YApi 为了解决这个问题，开发了可视化接口自动化测试功能，只需要配置每个接口的入参和对 RESPONSE 断言，即可实现对接口的自动化测试。而且大部分接口支持Swagger导入或者从Idea配置直接导入。大大提升了接口测试的效率。

#### 二、新建测试集合

使用 YApi 自动化测试，第一步需要做得是创建测试集合和导入接口,点击添加集合创建，创建完成后导入接口(同一个接口可以多次导入)。

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhcthnkr2j30ji0aodg2.jpg)

#### 三、编写测试用例

编写测试用例主要涉及两个方面，一个是请求参数，另外一个是断言脚本。

##### 3.1 编辑请求参数

请求参数可以填写期望的字符串，YApi 还提供了 Mock 参数和 变量参数。Mock参数用来生成随机字符串，变量参数是为了解决请求参数依赖其他接口的返回数据或参数。

##### 3.2 Mock 参数

Mock 参数每次请求都会生成随机字符串

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhftzudz6j31jc0oaq43.jpg)

##### 3.3 变量参数

YApi 提供了强大的变量参数功能，你可以在测试的时候使用前面接口的 `参数` 或 `返回值` 作为 `后面接口的参数`，即使接口之间存在依赖，也可以轻松 **一键测试~**

> Tips: 参数只能是测试过程中排在前面的接口中的变量参数

格式：`$.{key}.{params|body}.{path}`

例如：现有两个接口，分别是“导航标题”和“文章列表”

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhfvyk173j30iu047aa0.jpg)

文章列表接口需要传参数: `当前标题(id)`，而这个 id 需要通过 `导航标题` 的返回值获取，这时应在 `文章列表` 的参数输入框中根据前者的 key 找到对应 id。`导航标题` 的参数和返回值有如下结构：

参数：![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhfwxkss1j304l010jr5.jpg)

返回值：![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhfxc9awpj303e03aglg.jpg)

则 `文章列表` 的参数可以如下配置：

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhfy1ewltj30hc045aa0.jpg)

其中 **$.** 是使用 **动态变量** 的标志，$.269.**params** 即表示 key 值为 269 用例的请求参数，$.269.**body** 即表示 key 值为 269 用例的返回值。

如果 requestBody 是 json 格式也可以在 json 中写变量参数，如下图：

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhfyorxd5j30mu04u745.jpg)

> Tips: 上下拖动测试集合的列表项可以调整测试的顺序。

目前 yapi 中的`query`，`body`,`header`和`pathParam`的输入参数已经支持点击选择功能。无需自己填写表达式，只需在弹窗中选择需要展示的表达式即可。 输入选项包括`常量`，`mock数据`，在测试集合中也支持`变量`选择。具体用法：单击编辑按钮打开表达式生成器，点击需要的数据创建表达式，这里也可以实时查看表达式结果。

> Tips: 在测试集合中插入变量参数可以会出现下图的提示信息，这是正常现象。因为该参数只能在各个接口顺序执行的时候才能拉到变量参数中的值

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhfzd0vfuj30sf04174a.jpg)

#### 四、编写断言脚本

编写完请求参数，可通过 js 脚本写断言，实现精准测试，在接口用例页面点击 Test 编辑。

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhfztk84ej317s0mwq3b.jpg)

#### 五、运行自动化测试

在测试列表可以看到每个测试用例的 key,还有 开始测试、报告等功能

点击开始测试会按照 case 定义的参数从上往下一个一个进行测试，如果顺序有问题，可以拖动调整

测试完成之后，点击报告查看该次请求的结果

#### 六、断言脚本公共变量

参考：https://nodejs.org/dist/latest-v8.x/docs/api/assert.html

#### 七、服务端自动化测试

开始测试功能是在浏览器跑自动化测试，他依赖于浏览器的使用环境。服务端自动化测试功能是在YApi服务端跑自动化测试，不需要依赖浏览器环境，只需要访问 YApi 提供的 url 链接就能跑自动化测试，非常的简单易用，而且可以集成到 jenkins。

##### 7.1 详细使用方法

点击服务端测试，出现如下弹窗，用户访问该 url 就可以获取当前测试用例的所有测试结果。

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhg2y51z4j30y00f5mxi.jpg)

然后可以在xxl-job添加这个脚本的地址，可以选择邮件通知，来进行定时的自动化测试

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhg3y0pnjj31dr0u0jt8.jpg)

然后就能看到邮件了~

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghhg522zhoj314u0gk74s.jpg)

https://zhuanlan.zhihu.com/p/32202008

https://hellosean1025.github.io/yapi/

https://testerhome.com/topics/20437