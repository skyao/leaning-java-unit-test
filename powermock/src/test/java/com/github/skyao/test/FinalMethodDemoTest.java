package com.github.skyao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FinalMethodDemo.class)
public class FinalMethodDemoTest {

    @Mock
    private FinalMethodDemo demo;

    @Test
    public void getSomething() throws Exception {
        when(demo.getSomething()).thenReturn(5);
        assertThat(demo.getSomething()).isEqualTo(5);
    }
}