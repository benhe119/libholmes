// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import static org.junit.Assert.*;
import org.junit.Test;

public class OpenIntervalTest {
    @Test
    public void test() {
        OpenInterval<Integer> interval = new OpenInterval<Integer>(10, 20);
        assertEquals(new Integer(10), interval.getLeftEndpoint());
        assertEquals(new Integer(20), interval.getRightEndpoint());
        assertTrue(interval.isLeftOpen());
        assertTrue(interval.isRightOpen());
        assertFalse(interval.isLeftClosed());
        assertFalse(interval.isRightClosed());
        assertFalse(interval.contains(10));
        assertTrue(interval.contains(11));
        assertTrue(interval.contains(19));
        assertFalse(interval.contains(20));
    }

    @Test
    public void testLeftUnbounded() {
        OpenInterval<Integer> interval = new OpenInterval<Integer>(null, 20);
        assertEquals(null, interval.getLeftEndpoint());
        assertEquals(new Integer(20), interval.getRightEndpoint());
        assertTrue(interval.contains(-1000));
        assertTrue(interval.contains(19));
        assertFalse(interval.contains(20));
    }

    @Test
    public void testRightUnbounded() {
        OpenInterval<Integer> interval = new OpenInterval<Integer>(10, null);
        assertEquals(new Integer(10), interval.getLeftEndpoint());
        assertEquals(null, interval.getRightEndpoint());
        assertFalse(interval.contains(10));
        assertTrue(interval.contains(11));
        assertTrue(interval.contains(1000));
    }
}
