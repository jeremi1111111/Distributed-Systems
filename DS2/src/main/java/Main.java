import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Input number of nodes:");
        Scanner inScanner = new Scanner(System.in);
        int size = 0;
        while (size == 0)
            try {
                size = Integer.parseInt(inScanner.nextLine());
                if (size == 0)
                    System.err.println("Value can't be zero!");
            } catch (Exception ex) {
                System.err.println("Value must be a number! Try again:");
                size = 0;
            }
        NodeRunnable.nodes = new ArrayList<>();
        NodeRunnable.systemSize = size;

        ArrayList<Thread> system = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            NodeRunnable node = new NodeRunnable(i);
            Thread thread = new Thread(node);
            system.add(thread);
            NodeRunnable.nodes.add(node);
            thread.start();
        }

        System.out.println("Created " + size + " nodes.");

        for (Thread thread : system)
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}
