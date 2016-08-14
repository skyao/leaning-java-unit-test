package com.github.skyao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//import org.mockito.runners.MockitoJUnitRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FinalClassDemo.class)
//@RunWith(MockitoJUnitRunner.class)
public class FinalClassDemoTest {

    @Mock
    private FinalClassDemo demo;

    @Test
    public void getSomething() throws Exception {
        when(demo.getSomething()).thenReturn(5);
        assertThat(demo.getSomething()).isEqualTo(5);
    }
}