package com.flycamel.AnnoGettingTest;

@CustomApi
public class ApiMethodsImpl implements ApiMethods {

    @Override
    @CustomApi(apiValue = "sayHi")
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }

    @Override
    public String sayYeah(String name) {
        return "Yeah, " + name + "!";
    }
}
