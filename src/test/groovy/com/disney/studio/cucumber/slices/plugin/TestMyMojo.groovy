package com.disney.studio.cucumber.slices.plugin

import org.junit.Test

class TestMyMojo {
    @Test
    public void shouldRunSimpleMathTest() {
        int a = 1;
        int b = 2;
        int c = a + b;

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("TEST OUTPUT: a + b = " + c);
        assert a + b == c;
    }
}
