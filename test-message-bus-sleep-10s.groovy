package s;

import com.liferay.portal.kernel.messaging.Message
import com.liferay.portal.kernel.messaging.MessageBusUtil
import com.liferay.portal.kernel.messaging.MessageListener
import com.liferay.portal.kernel.messaging.MessageListenerException
import com.liferay.portal.kernel.messaging.SynchronousDestination

public class RequestedDispatcherPOC implements MessageListener {
    @Override
    public void receive(Message message) throws MessageListenerException {
        // payload processing
        // send response
        Thread.sleep(10000);
        Message response = MessageBusUtil.createResponseMessage(message);
        response.setPayload("some response");
        MessageBusUtil.sendMessage(response.getDestinationName(), response);
    }
}

class TestBusTimeout {

    public static test(PrintWriter out) {

// create synchronous destination
        SynchronousDestination destination = new SynchronousDestination();
        destination.setName("some/address");
        MessageBusUtil.addDestination(destination);
// register listener for destination
        RequestedDispatcherPOC dispatcherPOC = new RequestedDispatcherPOC();
        MessageBusUtil.registerMessageListener("some/address", dispatcherPOC);

        send(1000, out);
        send(5000, out);
        send(10000, out);
    }

    public static send(long timeout_ms, PrintWriter out) {
        Message message = new Message();
        message.setResponseDestinationName("some/address" + "/answer");
        long start = System.currentTimeMillis();
        Object respObj = MessageBusUtil.sendSynchronousMessage("some/address", message, timeout_ms);
        long stop = System.currentTimeMillis();
        out.println("Timeout: " + timeout_ms + "ms. Received response to payload: " + respObj + ", after: " + Long.toString(stop - start) + "ms");
    }
}

try {
    TestBusTimeout.test(out);
} catch (Exception e) {
    e.printStackTrace(out);
}
