package com.zyc.jobmanager.config;

import com.zyc.jobmanager.entity.Address;
import com.zyc.jobmanager.entity.Person;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.List;
import java.util.Map;

public class TestSnakeYaml {

    @Test
    public void testLoad() {
        String yamlStr = "key: hello yaml";
        Yaml yaml = new Yaml();
        Object ret = yaml.load(yamlStr);
        System.out.println(ret);
    }

    @Test
    public void testType() throws Exception {
        Yaml yaml = new Yaml();
        List<String> ret = (List<String>)yaml.load(this.getClass().getClassLoader()
                .getResourceAsStream("test1.yml"));
        System.out.println(ret);
    }

    @Test
    public void test2() throws Exception {
        Yaml yaml = new Yaml();
        Map<String, Object> ret = (Map<String, Object>) yaml.load(this
                .getClass().getClassLoader().getResourceAsStream("test2.yml"));
        System.out.println(ret);
    }

    @Test
    public void test3() throws Exception {
        Yaml yaml = new Yaml();
        Iterable<Object> ret = yaml.loadAll(this.getClass().getClassLoader()
                .getResourceAsStream("test3.yml"));
        for (Object o : ret) {
            System.out.println(o);
        }
    }

    @Test
    public void testAddress() throws Exception {
        Yaml yaml = new Yaml();
        Address ret = yaml.loadAs(this.getClass().getClassLoader()
                .getResourceAsStream("address.yml"), Address.class);
        System.out.println(ret);
        Assert.assertNotNull(ret);
        Assert.assertEquals("MI", ret.getState());
    }

/*    @Test
    public void testPerson() throws Exception {
        Yaml yaml = new Yaml();
        Person ret = (Person) yaml.load(this.getClass().getClassLoader()
                .getResourceAsStream("person.yml"));
        Assert.assertNotNull(ret);
        Assert.assertEquals("MI", ret.getAddress().getState());
    }*/

    @Test
    public void testPerson2() {
        Yaml yaml = new Yaml(new Constructor(Person.class));
        Person ret = (Person) yaml.load(this.getClass().getClassLoader()
                .getResourceAsStream("person.yml"));
        Assert.assertNotNull(ret);
        Assert.assertEquals("MI", ret.getAddress().getState());
    }
}
