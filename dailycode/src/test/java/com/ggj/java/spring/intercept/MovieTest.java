package com.ggj.java.spring.intercept;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.BaseTest;

import static org.junit.Assert.*;

public class MovieTest extends BaseTest {
    @Autowired
    private  Movie movie;
    @Test
    public void paly() {
        movie.paly();
    }
}
