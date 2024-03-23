package model;


import groovy.lang.Singleton;

import io.cucumber.guice.ScenarioScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ScenarioScoped
public class RequestInfo {
    String phoneNumber;
    String accessToken;
    String pin;
    String env;
    String userID;

}
