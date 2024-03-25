import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

class TreeNode {
    public static final ArrayList<TreeNode> system = new ArrayList<>();
    public static final TreeMap<String, Integer> indexMap = new TreeMap<>();
    public static final Random workTimeRoller = new Random();
    private final int id;
    private final String name;
    private TreeNode parent;
    private int counter;
    private boolean hasToken = false;
    private boolean isWorking = false;
    private final ArrayList<Integer> requests;
    private int finishWorkTimestamp;

    public TreeNode(int id, String name, TreeNode parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.counter = 0;
        this.requests = new ArrayList<>();
        this.finishWorkTimestamp = 0;
        indexMap.put(name, id);
    }

    public void passToken(int timestamp, int id) {
        if (counter < timestamp)
            counter = timestamp;
        counter++;
        hasToken = true;
        parent = system.get(indexMap.get("root"));
        if (!requests.isEmpty()) {
            int reqId = requests.removeFirst();
            if (reqId == this.id) {
                startWork();
                return;
            }
            parent = system.get(reqId);
            hasToken = false;
            System.out.println("[" + counter + "]" + " Node " + this.name + " sent the token to node " + parent.name + ".");
            System.out.println("Node " + this.name + " requests: " + this.requests.stream().map(v ->system.get(v).name).toList());
            parent.passToken(counter, this.id);
            return;
        }
        System.out.println("[" + counter + "]" + " Node " + this.name + " is holding the token.");
        System.out.println("Node " + this.name + " requests: " + this.requests.stream().map(v ->system.get(v).name).toList());
    }

    private void startWork() {
        finishWorkTimestamp = counter + workTimeRoller.nextInt(4, 8);
        isWorking = true;
        System.out.println("[" + counter + "]" + " Node " + this.name + " is holding the token and it started working.");
        System.out.println("Node " + this.name + " requests: " + this.requests.stream().map(v ->system.get(v).name).toList());
    }

    private void sync(int timestamp) {
        if (counter < timestamp)
            counter = timestamp;
        counter++;
        if (!hasToken)
            parent.sync(counter);
        if (isWorking && counter >= finishWorkTimestamp) {
            isWorking = false;
            System.out.println("[" + counter + "]" + " Node " + this.name + " has finished working.");
            if (requests.isEmpty()) return;
            TreeNode reqNode = system.get(requests.removeFirst());
            finishWork(reqNode);
        }
    }

    public void requestToken(int id, int timestamp) {
        sync(timestamp);
        System.out.println("[" + counter + "]" + " Node " + this.name + " received a token request from node " + system.get(id).name + ".");
        if (requests.contains(id)) {
            System.out.println("Request is already on the list.");
            System.out.println("Node " + this.name + " requests: " + this.requests.stream().map(v ->system.get(v).name).toList());
            return;
        } else {
            requests.add(id);
            System.out.println("Node " + this.name + " requests: " + this.requests.stream().map(v ->system.get(v).name).toList());
        }
        if (!hasToken) {
            parent.requestToken(this.id, counter);
            return;
        }
        if (isWorking) {
            if (counter >= finishWorkTimestamp) {
                isWorking = false;
                System.out.println("[" + counter + "]" + " Node " + this.name + " is holding the token and has finished working.");
            } else {
                System.out.println("[" + counter + "]" + " Node " + this.name + " is holding the token and is working.");
            }
            System.out.println("Node " + this.name + " requests: " + this.requests.stream().map(v ->system.get(v).name).toList());
        }
        TreeNode reqNode = system.get(requests.getFirst());
        if (reqNode.id == this.id) {
            requests.removeFirst();
            startWork();
            return;
        }
        if (!isWorking) {
            requests.removeFirst();
            finishWork(reqNode);
        }
    }

    private void finishWork(TreeNode reqNode) {
        hasToken = false;
        parent = reqNode;
        System.out.println("[" + counter + "]" + " Node " + this.name + " passed the token to node " + reqNode.name + ".");
        System.out.println("Node " + this.name + " requests: " + this.requests.stream().map(v ->system.get(v).name).toList());
        parent.passToken(counter, this.id);
        if (!requests.isEmpty())
            parent.requestToken(this.id, counter);
    }

    public String getName() {
        return name;
    }
}
