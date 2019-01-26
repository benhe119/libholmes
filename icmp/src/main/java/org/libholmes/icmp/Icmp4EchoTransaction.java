// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import javax.json.JsonObjectBuilder;

import org.libholmes.Artefact;
import org.libholmes.inet.Inet4Datagram;

/** A class to represent an ICMP echo reqest/reply pair.
 * A transmission normally consists of a request and a matching reply.
 * It may also include matching retransmissions of the request and/or
 * the reply. Either the request or the reply may be omitted.
 */
public class Icmp4EchoTransaction extends Artefact {
    /** The first observed echo request message, or null if none. */
    Icmp4EchoMessage request = null;

    /** The first observed echo reply message, or null if none. */
    Icmp4EchoReplyMessage reply = null;

    /** The number of matching echo requests observed. */
    int requestCount = 0;

    /** The number of matching echo replies observed. */
    int replyCount = 0;

    public Icmp4EchoTransaction() {
        super(null);
    }

    /** Get the first observed echo request message.
     * @return the first request message, or null if none
     */
    public final Icmp4EchoMessage getRequest() {
        return request;
    }

    /** Get the first observed echo reply message.
     * @return the first reply message, or null if none
     */
    public final Icmp4EchoReplyMessage getReply() {
        return reply;
    }

    /** Get the number of matching requests observed.
     * @return the number of matching requests
     */
    public final int getRequestCount() {
        return requestCount;
    }

    /** Get the number of matching replies observed.
     * @return the number of matching replies
     */
    public final int getReplyCount() {
        return replyCount;
    }

    /** Attempt to process an ICMP echo request message.
     * @param packet the message to be processed
     * @return true if accepted as part of this transaction, otherwise false
     */
    public final boolean process(Icmp4EchoMessage message) {
        if (request != null) {
            // If a request has been seen previously then do not accept this
            // one unless it is a duplicate.
            Inet4Datagram thisInet4 = request.find(Inet4Datagram.class);
            Inet4Datagram thatInet4 = message.find(Inet4Datagram.class);
            if (!thatInet4.isDuplicate(thisInet4)) {
                return false;
            }
        } else if (reply != null) {
            // Otherwise, if a reply has been seen previously then this
            // request must be consistent with that reply.
            if (message.getIdentifier() != reply.getIdentifier()) {
                return false;
            }
            if (message.getSequenceNumber() != reply.getSequenceNumber()) {
                return false;
            }
            if (!message.getData().equals(reply.getData())) {
                return false;
            }
            request = message;
        } else {
            // If nothing seen previously then accept unconditionally.
            request = message;
        }
        requestCount += 1;
        return true;
    }

    /** Attempt to process an ICMP echo reply message.
     * @param packet the message to be processed
     * @return true if accepted as part of this transaction, otherwise false
     */
    public final boolean process(Icmp4EchoReplyMessage message) {
        if (reply != null) {
            // If a reply has been seen previously then do not accept this
            // one unless it is a duplicate.
            Inet4Datagram thisInet4 = reply.find(Inet4Datagram.class);
            Inet4Datagram thatInet4 = message.find(Inet4Datagram.class);
            if (!thatInet4.isDuplicate(thisInet4)) {
                return false;
            }
        } else if (request != null) {
            // Otherwise, if a request has been seen previously then this
            // reply must be consistent with that request.
            if (message.getIdentifier() != request.getIdentifier()) {
                return false;
            }
            if (message.getSequenceNumber() != request.getSequenceNumber()) {
                return false;
            }
            if (!message.getData().equals(request.getData())) {
                return false;
            }
            reply = message;
        } else {
            // If nothing seen previously then accept unconditionally.
            reply = message;
        }
        replyCount += 1;
        return true;
    }

    @Override
    protected void buildJson(JsonObjectBuilder builder) {
        builder.add("requestCount", requestCount);
        builder.add("replyCount", replyCount);
    }
}
