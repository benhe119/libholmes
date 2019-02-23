// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.netbios.ns;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashSet;

import org.libholmes.HexOctetReader;
import org.libholmes.ParseException;

public class NetbiosScopedNameTest {
    @Test
    public void test() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "204645474947464341454F4746484545" +
            "43454a455046444341474f4742474e47" +
            "46074e455442494f5303434f4d00");

        NetbiosScopedName name = new NetbiosScopedName(reader, reader);
        assertEquals("The NetBIOS name", name.getNetbiosName());
        assertEquals("NETBIOS.COM", name.getScopeId());
    }
}
