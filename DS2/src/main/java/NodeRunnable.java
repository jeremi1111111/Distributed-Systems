import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NodeRunnable implements Runnable {
    public static ArrayList<NodeRunnable> nodes;
    public static int systemSize;
    private static final Random random = new Random();
    int id;
    int counter;
    ArrayList<Integer> awaitingApproval;
    ArrayList<Integer> incomingRequests;
    int requestTimestamp;
    boolean isInCriticalSection;

    public NodeRunnable(int id) {
        this.id = id;
        this.counter = 0;
        this.awaitingApproval = new ArrayList<>();
        this.incomingRequests = new ArrayList<>();
        this.requestTimestamp = 0;
        this.isInCriticalSection = false;
    }

    public void request(Integer id, int timestamp) {
        if (counter < timestamp)
            counter = timestamp;
        counter++;
        if (isInCriticalSection
                || (!awaitingApproval.isEmpty()
                && (this.requestTimestamp < timestamp || this.requestTimestamp == timestamp && this.id < id)))
            this.incomingRequests.add(id);
        else
            nodes.get(id).approve(this.id, counter);
    }

    public void approve(Integer id, int timestamp) {
        if (counter < timestamp)
            counter = timestamp;
        counter++;
        this.awaitingApproval.remove(id);
        if (awaitingApproval.isEmpty())
            isInCriticalSection = true;
    }

    @Override
    public void run() {
        while (counter < systemSize * 12.5) {
            if (isInCriticalSection) {
                System.out.println("[" + this.counter + "] Node " + this.id + " entered CS.");
                for (NodeRunnable node : nodes)
                    System.out.println(node.id + " awaiting: " + node.awaitingApproval + ", incoming: " + node.incomingRequests);
                counter++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("[" + this.counter + "] Node " + this.id + " left CS.");
                for (Integer v : incomingRequests)
                    nodes.get(v).approve(this.id, counter);
                incomingRequests = new ArrayList<>();
                isInCriticalSection = false;
            } else if (awaitingApproval.isEmpty() && random.nextInt(100) < 40 / systemSize) {
                System.out.println("[" + this.counter + "] Node " + this.id + " requested CS.");
                this.requestTimestamp = counter;
                for (NodeRunnable node : nodes)
                    if (node.id != this.id)
                        this.awaitingApproval.add(node.id);
                for (NodeRunnable node : nodes)
                    if (node.id != this.id)
                        node.request(this.id, this.counter);
                counter++;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
