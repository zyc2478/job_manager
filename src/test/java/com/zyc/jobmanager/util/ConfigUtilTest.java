package com.zyc.jobmanager.util;

import com.zyc.jobmanager.config.MyProps;
import com.zyc.jobmanager.entity.Person;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class ConfigUtilTest {

    //@Test
    public void testParse() {
        Yaml yaml = new Yaml(new Constructor(MyProps.class));
        //InputStream in = SnakeYAMLTest.class.getResourceAsStream("/sample.yml");
/*        Person ret = (Person) yaml.load(this.getClass().getClassLoader()
                .getResourceAsStream("application.yml"));*/
        MyProps myProps = (MyProps) yaml.load(this.getClass().getClassLoader()
                .getResourceAsStream("properties.yml"));
        System.out.println(myProps);
        System.out.println(myProps.getRedis());
        System.out.println(myProps.getRedis().getHost());
    }

    @Test
    public void testConfUtilParse(){
        MyProps myProps = ConfigUtil.getProps();
        System.out.println(myProps);
        System.out.println(myProps.getRedis());
    }
}
