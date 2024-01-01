package model;


import groovy.lang.Singleton;

import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
@Singleton
public class RequestInfo {
    String phoneNumber;
    String accessToken;
    String pin;
    String env;
    String userID;

}
