<!-- TOC -->

- [Junit 规范](#junit-规范)
- [测试粒度](#测试粒度)
    - [选择测试粒度的原则](#选择测试粒度的原则)
    - [测试数据的覆盖率](#测试数据的覆盖率)
- [Junit使用](#junit使用)
    - [Junit常用注解](#junit常用注解)
    - [Spring Boot单元测试常用的注解](#spring-boot单元测试常用的注解)
    - [@Test中的expected属性和timeout属性](#test中的expected属性和timeout属性)
    - [Junit测试用例执行顺序](#junit测试用例执行顺序)
    - [添加依赖](#添加依赖)
    - [常用断言](#常用断言)
- [参考博客](#参考博客)

<!-- /TOC -->
# Junit 规范
1） 测试类的命名规则是：**被测试类的类名+Test**
比如有一个类叫MessageSendMonitor，那么它的测试类的命名就是MessageSendMonitorTest
2） 测试用例的命名规则是：**test+用例方法名称**
比如MessageSendMonitor中有一个方法叫做sendMessage，那么在MessageSendMonitorTest中对应的测试用例名称就是testSendMessage。
3） 测试程序的包名定义规范
单元测试包结构和源码结构必须保持一致。
4） 测试方法使用@Test标注
5） 所有测试方法返回类型必须为void且无参数；
6） 每个测试方法之间相互独立；

# 测试粒度
## 选择测试粒度的原则
1) 被测试类中所有public、protected方法都要测到。
2) 对于简单的set和get方法没有必要做测试。

## 测试数据的覆盖率
测试时所准备的测试数据要覆盖程序中所有可能出现的CASE。

# Junit使用
## Junit常用注解
@Before：初始化方法
@After：释放资源
@Test：测试方法，在这里可以测试期望异常和超时时间
@Ignore：忽略的测试方法
@BeforeClass：针对所有测试，只执行一次，且必须为static void
@AfterClass：针对所有测试，只执行一次，且必须为static void
@RunWith：指定使用的单元测试执行类

## Spring Boot单元测试常用的注解
* @RunWith(SpringRunner.class)
JUnit运行使用Spring的测试支持。SpringRunner是SpringJUnit4ClassRunner的新名字，这样做的目的
仅仅是为了让名字看起来更简单一点。
* @SpringBootTest
该注解为SpringApplication创建上下文并支持Spring Boot特性，其webEnvironment提供如下配置： 
    1) Mock-加载WebApplicationContext并提供Mock Servlet环境，嵌入的Servlet容器不会被启动。
    2) RANDOM_PORT-加载一个EmbeddedWebApplicationContext并提供一个真实的servlet环境。嵌入的Servlet容器将被启动并在一个随机端口上监听。
    3) DEFINED_PORT-加载一个EmbeddedWebApplicationContext并提供一个真实的servlet环境。嵌入的Servlet容器将被启动并在一个默认的端口上监听
    （application.properties配置端口或者默认端口8080）。
    4) NONE-使用SpringApplication加载一个ApplicationContext，但是不提供任何的servlet环境。
* @MockBean
在你的ApplicationContext里为一个bean定义一个Mockito mock.把原来的bean用mock的bean整个替换掉了。  
 
[demo](/src/test/java/com/junittest/demo/service/UserServiceTest.java)

* SpyBean
定制化Mock某些方法。使用@SpyBean还会执行原来bean的方法, 但是后面可以mock想要的方法

```java
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserResourceTests {
 
    @Autowired
    private MockMvc mockMvc;
 
    @SpyBean
    private MongoTemplate mongoTemplate;
 
    @Test
    public void should_create_a_user() throws Exception {
        String json = "{\"username\":\"shekhargulati\",\"name\":\"Shekhar Gulati\"}";
        doReturn(null)
                .when(mongoTemplate).findOne(Mockito.any(Query.class), Mockito.eq(User.class));
        doNothing().when(mongoTemplate).save(Mockito.any(User.class));
        this.mockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(mongoTemplate).save(Mockito.any(User.class));
    }
}
```

在上述例子中

我们使用@SpyBean注解将MongoTemplate定义为spy。这将不会使用mock bean代替原来spring上下文内容，所以这个容器还是可以使用的。
接下来，我们使用doReturn语法设置期望值。 如果你按正常方式调用，那么真正的bean会调用这个方法，它会抛出异常。
我们使用了Mockito 的doNothing API 模拟这个void方法mongoTemplate.save().
最后, 我们确定save call 使用了verify Mockito方法.

* @WebMvcTest
测试Spring MVC controllers是不是按正常情况运行。
该注解被限制为一个单一的controller，需要利用@MockBean去Mock合作者（如service）。

```java
@RunWith(SpringRunner.class)
@WebMvcTest(UserVehicleController.class)
public class MyControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserVehicleService userVehicleService;

	@Test
	public void testExample() throws Exception {
		given(this.userVehicleService.getVehicleDetails("sboot"))
				.willReturn(new VehicleDetails("Honda", "Civic"));
		this.mvc.perform(get("/sboot/vehicle").accept(MediaType.TEXT_PLAIN))
				.andExpect(status().isOk()).andExpect(content().string("Honda Civic"));
	}

}
```



## @Test中的expected属性和timeout属性
* expected属性
用来指示期望抛出的异常类型。
比如除以0的测试：

```java
 @Test(expected = Exception.class)
    public void testDivide() throws Exception
    {
        cal.divide(1, 0);
    }
```

抛出指定的异常类型，则测试通过 。
如果除数改为非0值，则不会抛出异常，测试失败，报Failures。
* timeout属性
用来指示时间上限。
比如把这个属性设置为100毫秒：

```java
@Test(timeout=100) 
public void infinity() 
{
    while(true);
}
```

当测试方法的时间超过这个时间值时测试就会失败。
（注意超时了报的是Errors，如果是值错了是Failures）

## Junit测试用例执行顺序
@BeforeClass ==> @Before ==> @Test ==> @After ==> @AfterClass 
过程：就是先加载模拟的环境，再进行测试。

## 添加依赖

```java
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <version> 1.5.1.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
      <version>4.3.6.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version> 4.12</version>
</dependency>
```

根据项目改变版本号，demo中采取version 1.5

## 常用断言
assertArrayEquals(expecteds, actuals)   
查看两个数组是否相等。  
assertEquals(expected, actual)	 
查看两个对象是否相等。类似于字符串比较使用的equals()方法   
assertNotEquals(first, second)	  
查看两个对象是否不相等。   
assertNull(object)	 
查看对象是否为空。   
assertNotNull(object)	
查看对象是否不为空。    
assertSame(expected, actual)	
查看两个对象的引用是否相等。类似于使用“==”比较两个对象   
assertNotSame(unexpected, actual)	
查看两个对象的引用是否不相等。类似于使用“!=”比较两个对象        
assertTrue(condition)	
查看运行结果是否为true。   
assertFalse(condition)	
查看运行结果是否为false。    
assertThat(actual, matcher)	  
查看实际值是否满足指定的条件   
fail()	
让测试失败  
 
[demo](/src/test/java/com/junittest/demo/AssertTest.java)


# 参考博客
参考1 : [spring-boot官网](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-testing)

参考2 : [SpringBoot单元测试](https://www.jianshu.com/p/813fd69aabee)

参考3 : [在 Spring Boot 1.5.3 中进行 Spring MVC 测试](https://aisensiy.github.io/2017/05/04/spring-mvc-and-test/)

参考4 : [Spring Boot整合MyBatis，附带单元测试](https://blog.csdn.net/KunGe_/article/details/78435264)

参考5 : [Using Spring Boot @SpyBean](https://shekhargulati.com/2017/07/20/using-spring-boot-spybean/)

参考6 : [使用JUnit测试预期异常](https://blog.csdn.net/tayanxunhua/article/details/20570457)
