package com.juniorjrc.orderservice.annotations;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@ActiveProfiles("integration-tests")
public @interface IntegrationTest {
}
