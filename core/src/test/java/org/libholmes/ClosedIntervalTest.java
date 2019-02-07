// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import static org.junit.Assert.*;
import org.junit.Test;

public class ClosedIntervalTest {
    @Test
    public void test() {
        ClosedInterval<Integer> interval = new ClosedInterval<Integer>(10, 20);
        assertEquals(new Integer(10), interval.getLeftEndpoint());
        assertEquals(new Integer(20), interval.getRightEndpoint());
        assertFalse(interval.isLeftOpen());
        assertFalse(interval.isRightOpen());
        assertTrue(interval.isLeftClosed());
        assertTrue(interval.isRightClosed());
        assertFalse(interval.contains(9));
        assertTrue(interval.contains(10));
        assertTrue(interval.contains(20));
        assertFalse(interval.contains(21));
    }

    @Test
    public void testLeftUnbounded() {
        ClosedInterval<Integer> interval = new ClosedInterval<Integer>(null, 20);
        assertEquals(null, interval.getLeftEndpoint());
        assertEquals(new Integer(20), interval.getRightEndpoint());
        assertTrue(interval.contains(-1000));
        assertTrue(interval.contains(20));
        assertFalse(interval.contains(21));
    }

    @Test
    public void testRightUnbounded() {
        ClosedInterval<Integer> interval = new ClosedInterval<Integer>(10, null);
        assertEquals(new Integer(10), interval.getLeftEndpoint());
        assertEquals(null, interval.getRightEndpoint());
        assertFalse(interval.contains(9));
        assertTrue(interval.contains(10));
        assertTrue(interval.contains(1000));
    }
}
