# tiny-spring

[![License](https://img.shields.io/badge/license-MIT-4EB1BA.svg)]()
[![Java](https://img.shields.io/badge/language-Java-orange.svg)]()

> since 2021-03-31 17:08

## Introduction

tiny-spring is a simplified framework for learning fundamental principles of Spring.

- IOC (Bean Container)
- AOP (CGLIB, AspectJ)
- Web MVC 
- Quick Launch Starter (Embed Tomcat)

## Example
import dependency (maven): 
```xml
<dependency>
    <groupId>com.ywh.spring</groupId>
    <artifactId>tiny-spring</artifactId>
    <version>0.1</version>
</dependency>
```
create main class:
```java
import com.ywh.spring.SpringApplication;

public class APP {
    public static void main(String[] args) {
        SpringApplication.run(APP.class);
    }
}
```
add controller:
```java
import com.ywh.spring.core.annotation.Controller;
import com.ywh.spring.ioc.Autowired;

@Controller
public class TestController {
    @RequestMapping(value = "index")
    @ResponseBody
    public String hello() {
        return "Hello, World!";
    }
}
```

startup:
```
 ___________.__                 _________            .__                
 \__    ___/|__| ____ ___.__.  /   _____/____________|__| ____    ____  
   |    |   |  |/    <   |  |  \_____  \\____ \_  __ \  |/    \  / ___\ 
   |    |   |  |   |  \___  |  /        \  |_> >  | \/  |   |  \/ /_/  >
   |____|   |__|___|  / ____| /_______  /   __/|__|  |__|___|  /\___  / 
                    \/\/              \/|__|                 \//_____/
 :: tiny-spring ::                                         (0.0.1-alpha)

2021-04-20 21:53:39,657 [INFO] -- c.y.s.m.s.TomcatServer         Tomcat: application resolved root folder: [C:\Project\other-project\tiny-spring\example]
2021-04-20 21:53:39,659 [INFO] -- c.y.s.m.s.TomcatServer         Tomcat: configuring app with basedir: [C:\Project\other-project\tiny-spring\example\src\main\resources]
...
```

## License

See LICENSE file.