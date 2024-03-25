import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner inScanner = new Scanner(System.in);
        System.out.println("Input number of nodes: ");
        int n = inScanner.nextInt();

        NodeRunnable first = new NodeRunnable(0, null);
        NodeRunnable node = first;
        for (int i = n - 1; i > 0; i--) {
            node = new NodeRunnable(i, node);
        }
        first.next = node;

        System.out.println("Send Token to node: ");
        int token = inScanner.nextInt();
        while (first.id != token) first = first.next;
        first.awaitingToken = true;
        first.passToken(token);

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            threads.add(new Thread(first));
            threads.getLast().start();
            first = first.next;
        }

        System.err.println("System started. Type 'stop' to shut down.");
        while (!inScanner.nextLine().equals("stop")) {}
        first.close();
        System.err.println("System shut down initiated. No new requests permitted.");

        for (Thread thread : threads)
            thread.join();
        System.err.println("System shut down successfully.");
    }
}
