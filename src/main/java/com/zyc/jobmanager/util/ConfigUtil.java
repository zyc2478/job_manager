package com.zyc.jobmanager.util;

import com.zyc.jobmanager.config.MyProps;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import sun.security.krb5.Config;

import java.io.InputStream;

public class ConfigUtil {

    private static MyProps myProps;

    public static void parseConfig(){
        Yaml yaml = new Yaml(new Constructor(MyProps.class));
        //InputStream in = SnakeYAMLTest.class.getResourceAsStream("/sample.yml");
/*        Person ret = (Person) yaml.load(this.getClass().getClassLoader()
                .getResourceAsStream("application.yml"));*/
        myProps = yaml.load(ConfigUtil.class.getClass().getClassLoader()
                .getResourceAsStream("properties.yml"));
    }

    public static MyProps getProps(){
        //parseConfig();
        Yaml yaml = new Yaml(new Constructor(MyProps.class));
        //InputStream in = SnakeYAMLTest.class.getResourceAsStream("/sample.yml");
/*        Person ret = (Person) yaml.load(this.getClass().getClassLoader()
                .getResourceAsStream("application.yml"));*/
        InputStream in = ConfigUtil.class.getResourceAsStream("/properties.yml");
        //MyProps myProps = yaml.loadAs(in, MyProps.class );
        //myProps = yaml.loadAs(in, MyProps.class );
        myProps = yaml.load(in);
/*        myProps = yaml.load(ConfigUtil.class.getClass().getClassLoader()
                .getResourceAsStream("properties.yml"));*/


        return myProps;
    }
}
