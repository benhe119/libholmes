// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import javax.json.JsonValue;
import javax.json.JsonObject;

/** An abstract base class to represent a generic fingerprint.
 * A fingerprint is a set of characteristics which can be compared against
 * an artefact.
 */
public abstract class Fingerprint {
    /** The registered constructors, indexed by fingerprint type. */
    private static final HashMap<String, Constructor> types =
        new HashMap<String, Constructor>();

    /** Register a Fingerprint class.
     * @param typeName the name of the fingerprint type to be registered
     * @param className the name of the corresponding Java class
     */
    private static void registerType(String typeName, String className) {
        Constructor constructor = null;
        try {
            Class classObj = Class.forName(className);
            constructor = classObj.getConstructor(
                new Class[]{JsonObject.class});
        } catch (ClassNotFoundException ex) {
            // No action: will be registered with null value.
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
        types.put(typeName, constructor);
    }

    static {
        registerType("inet4", "org.libholmes.inet.Inet4Fingerprint");
        registerType("udp", "org.libholmes.udp.UdpFingerprint");
        registerType("icmp_echo", "org.libholmes.icmp.Icmp4EchoFingerprint");
    }

   /** An empty pattern matching context, for use when one has not been
     * supplied. */
    private static final AnalysisContext emptyContext = new AnalysisContext();

    /** Determine whether this fingerprint matches a given artefact.
     * @param artefact to be matched
     * @param context information for context-dependent fingerprints
     * @return true if the artefact matched, otherwise false
     */
    public abstract boolean matches(Artefact artefact,
        AnalysisContext context);

    /** Determine whether this fingerprint matches a given artefact.
     * This is a convenience method for when there is no supplied context.
     * @param artefact to be matched
     * @return true if the artefact matched, otherwise false
     */
    public final boolean matches(Artefact artefact) {
        return matches(artefact, emptyContext);
    }

    /** Parse fingerprint from a specification in JSON format.
     * @param typeName the name of the fingerprint type to be parsed
     * @param jsonSpec the specification to be parsed
     * @return the resulting fingerprint
     */
    public static Fingerprint parse(String typeName, JsonObject jsonSpec) {
        Constructor constructor = types.get(typeName);
        if (constructor != null) {
            try {
                return (Fingerprint) constructor.newInstance(jsonSpec);
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        } else if (types.containsKey(typeName)) {
            throw new IllegalArgumentException(String.format(
                "Fingerprint type %s not available", typeName));
        } else {
            throw new IllegalArgumentException(String.format(
                "Fingerprint type %s not recognised", typeName));
        }
    }
}
