import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int[][] connectionMatrix;
        Node[] nodeArray;
        int n;
        Scanner inScanner = new Scanner(System.in);

        System.out.println("Input file name:");
        String fileName = inScanner.nextLine();

        try {
            Scanner fileScanner = new Scanner(new File(fileName));
            n = fileScanner.nextInt();
            connectionMatrix = new int[n][n];
            nodeArray = new Node[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++)
                    connectionMatrix[i][j] = fileScanner.nextInt();
                nodeArray[i] = new Node(i, connectionMatrix[i][i], connectionMatrix[i][i]);
                connectionMatrix[i][i] = 0;
                System.out.println(nodeArray[i]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Arrays.stream(connectionMatrix).forEach(a -> System.out.println(Arrays.toString(a)));

        while (true) {
            System.out.println("Make a request: ");
            int n1 = inScanner.nextInt();
            int n2 = inScanner.nextInt();
            nodeArray[n2].block(nodeArray[n1]);
            ArrayList<Integer> transmit = new ArrayList<>();
            transmit.add(n1);
            while (!transmit.isEmpty()) {
                int i = transmit.removeFirst();
                if (i == n2) break;
                for (int j = 0; j < n; j++) {
                    if (j == i) continue;
                    if (connectionMatrix[j][i] == 0) continue;
                    nodeArray[i].transmit(nodeArray[j]);
                    transmit.add(j);
                }
            }
            connectionMatrix[n1][n2] = 1;
            if (nodeArray[n1].detect(nodeArray[n2])) break;
        }
    }
}
