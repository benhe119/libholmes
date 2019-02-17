// This file is part of libholmes.
// Copyright 2018-2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import javax.json.JsonValue;
import javax.json.JsonObject;
import javax.json.JsonArray;

/** An abstract base class to represent a pattern for matching against a
 * sequence of octets.
 */
public abstract class OctetPattern {
    /** The registered constructors, indexed by pattern type name. */
    private static final HashMap<String, Constructor> types =
        new HashMap<String, Constructor>();

    /** Register an OctetPattern class.
     * @param typeName the name of the pattern type to be registered
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
        registerType("hex", "org.libholmes.HexOctetPattern");
        registerType("wildcard", "org.libholmes.WildcardOctetPattern");
        registerType("random", "org.libholmes.RandomOctetPattern");
        registerType("text", "org.libholmes.TextOctetPattern");
        registerType("timeval", "org.libholmes.TimevalOctetPattern");
        registerType("time", "org.libholmes.TimeOctetPattern");
        registerType("step", "org.libholmes.StepOctetPattern");
        registerType("domain", "org.libholmes.DomainOctetPattern");
        registerType("inet", "org.libholmes.inet.InetAddressOctetPattern");
    }

    /** An empty analysis context, for use when one has not been supplied. */
    private static final AnalysisContext emptyContext = new AnalysisContext();

    /** Determine whether this pattern matches a given sequence of octets.
     * If it is able to, this method will reade a maximal-length sequence of
     * octets from the given reader which are an exact match for the pattern.
     * If this is not possible then it is unspecified how many octets will be
     * be read.
     * @param octets the octet sequence to be matched
     * @param context information for context-dependent patterns
     * @return true if the octet sequence matched, otherwise false
     */
    public abstract boolean matches(OctetReader octets,
        AnalysisContext context);

    /** Determine whether this pattern matches a given sequence of octets.
     * This is a convenience method for when there is no supplied context.
     * @param octets the octet sequence to be matched
     * @return true if the octet sequence matched, otherwise false
     */
    public final boolean matches(OctetReader octets) {
        return matches(octets, emptyContext);
    }

    /** Parse OctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     * @return the resulting OctetPattern
     */
    public static OctetPattern parse(JsonValue jsonSpec) {
        if (jsonSpec instanceof JsonArray) {
            JsonArray jsonArray = (JsonArray) jsonSpec;
            return new SequenceOctetPattern(jsonArray);
        } else if (jsonSpec instanceof JsonObject) {
            JsonObject jsonObject = (JsonObject) jsonSpec;
            String typeName = jsonObject.getString("type");
            Constructor constructor = types.get(typeName);
            if (constructor != null) {
                try {
                    return (OctetPattern) constructor.newInstance(jsonSpec);
                } catch (ReflectiveOperationException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (types.containsKey(typeName)) {
                throw new IllegalArgumentException(String.format(
                    "Pattern type %s not available", typeName));
            } else {
                throw new IllegalArgumentException(String.format(
                    "Pattern type %s not recognised", typeName));
            }
        }
        throw new IllegalArgumentException("Invalid pattern type");
    }
}
