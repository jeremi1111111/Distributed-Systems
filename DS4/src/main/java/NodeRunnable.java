import java.util.ArrayList;
import java.util.Random;

public class NodeRunnable implements Runnable {
    static final Random random = new Random();
    static final int requestChance = 2;
    final Object lock = new Object();
    final int id;
    NodeRunnable next;
    Integer token;
    ArrayList<Integer> requests;
    boolean awaitingToken;
    boolean isRunning;
    boolean isShutdown;

    public NodeRunnable(int id, NodeRunnable next) {
        this.id = id;
        this.next = next;
        this.token = -1;
        this.requests = new ArrayList<>();
        this.awaitingToken = false;
        this.isRunning = true;
        this.isShutdown = false;
    }

    @Override
    public void run() {
        while (!isShutdown) {
            if (token == this.id) {
                if (awaitingToken) synchronized (lock) {
                    System.out.println("Node " + this.id + " has entered CS.");
                    awaitingToken = false;
                    sleep(3000);
                    wait(lock, 1);
                    System.out.println("Node " + this.id + " has left CS.");
                    if (!isRunning && next.getNode() == this) break;
                    if (requests.isEmpty()) {
                        System.out.println("Node " + this.id + " is waiting for requests.");
                        wait(lock, 0);
                    }
                    System.out.println("Node " + this.id + " requests: " + requests + ".");
                    token = requests.removeFirst();
                }
            }
            if (isRunning && !awaitingToken && random.nextInt(100) < requestChance) {
                System.out.println("Node " + this.id + " requested CS.");
                requests.add(id);
                awaitingToken = true;
            }
            if (!isRunning)
                if (shutdown()) break;
            sleep(500);
            if (!requests.isEmpty() && token != this.id) {
                synchronized (next.lock) {
                    while (!requests.isEmpty())
                        next.getNode().sendRequest(requests.removeFirst());
                    next.lock.notify();
                }
            }
            if (token != -1 && token != this.id) {
                token = next.getNode().passToken(token);
            }
        }
        System.err.println("Node " + this.id + " is offline.");
    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void wait(Object lock, long millis) {
        try {
            lock.wait(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    Integer passToken(Integer token) {
        System.out.println("Node " + this.id + " received Token for node " + token + ".");
        this.token = token;
        return -1;
    }

    void sendRequest(Integer id) {
        System.out.println("Node " + this.id + " received TR from node " + id + ".");
        this.requests.add(id);
    }

    void close() {
        if (!isRunning) return;
        isRunning = false;
        next.close();
    }

    boolean shutdown() {
        if (token == -1 && requests.isEmpty() && !awaitingToken) {
            System.err.println("Node " + this.id + " shut down.");
            return isShutdown = true;
        }
        return false;
    }

    NodeRunnable getNode() {
        if (isShutdown) return next.getNode();
        return this;
    }
}
