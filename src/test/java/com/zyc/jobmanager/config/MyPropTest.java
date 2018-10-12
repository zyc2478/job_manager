package com.zyc.jobmanager.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class MyPropTest{

    @Autowired
    private MyProps myProps;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void propsTest() throws JsonProcessingException {
        //System.out.println("simpleProp: " + myProps.getSimpleProp());
/*        System.out.println("init_mode: " + myProps.getInit_mode());
        System.out.println("arrayProps: " + objectMapper.writeValueAsString(myProps.getArrayProps()));
        System.out.println("listProp1: " + objectMapper.writeValueAsString(myProps.getListProp1()));
        System.out.println("listProp2: " + objectMapper.writeValueAsString(myProps.getListProp2()));
        System.out.println("mapProps: " + objectMapper.writeValueAsString(myProps.getMapProps()));*/
    }
}

