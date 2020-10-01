HttpRunner原版已经更新到3.x了，在3.x中，debugtalk引入了pytest，在之前的版本中，HttpRunner支持两种方式的使用，
1)通过api testcase testsuite三级目录，编辑测试用例的调用关系，以命令行方式去使用；
2)把HttpRunenr作为一个第三方包，引入到其他中，通过调用HttpRunenr.run_test方法，传入必要的用例参数，可以执行测试。

在3.x版本中，除了以上两种方法，开始支持直接通过写代码的方式去执行测试，比如：
from httprunner import HttpRunner, Config, Step, RunRequest, RunTestCase

```class TestCaseRequestWithFunctions(HttpRunner):
    config = (
        Config("request methods testcase with functions")
        .variables(
            **{
                "foo1": "config_bar1",
            }
        )
        .base_url("https://postman-echo.com")
        .verify(False)
        .export(*["foo3"])
    )

    teststeps = [
        Step(
            RunRequest("get with params")
            .with_variables(
                **{"foo1": "bar11", "foo2": "bar21", "sum_v": "${sum_two(1, 2)}"}
            )
            .get("/get")
            .with_params(**{"foo1": "$foo1", "foo2": "$foo2", "sum_v": "$sum_v"})
            .extract()
            .validate()
            .assert_equal("status_code", 200)
        )
    ]


if __name__ == "__main__":
    TestCaseRequestWithFunctions().test_start()
```

稍微阅读了一下源码，用例的编写方式就是写一个继承HttpRunner的子类，子类中修改config和teststep两个成员变量，然后执行pytest的main方法，就能自动调用父类HttpRunner中的test_start方法
这样，我们只需要在子类中，修改config和teststep两个成员变量，就可以实现通过代码方式写测试用例了。

通过理解和查阅资料觉得这样写有两点好处，
一是通过代码写用例，不会担心写错 variables export等关键字，而且写用例也相对简单一些了，因为继承了父类的方法，开发工具还会给出关键字提示。
二是pytest有很多好的特性，可以进行套用，比如最基本的测试报告的内容，可以通过自己写插件实现报告的多样性，还有测试日志文件地址，执行过程中异常处理等，都有一些参数配置。

java没有现成的pytest类似框架可以使用，于是我便想着也通过一些java的单测库实现写代码直接跑用例；
对于pytest的其他特性，可能需要后期修改支持。

挑来挑去，感觉java中junit单测框架符合自己的预期，可以实现类似手写代码的用例编写方式。另外junit还可以通过命令行方式直接调用class文件实现测试用例的执行，使用起来还是比较方便的，后期可以通过动态编译java文件实现命令行方式的调用。