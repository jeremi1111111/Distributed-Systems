import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static boolean checkVertex(int source, int[][] graph, boolean[] isReached) {
        isReached[source] = true;
        for (int i = 0; i < graph.length; i++)
            if (!isReached[i] && graph[source][i] != 0) checkVertex(i, graph, isReached);
        for (boolean v : isReached)
            if (!v) return false;
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Input file name: ");
        Scanner inScanner = new Scanner(System.in);
        String filename = inScanner.nextLine();
        int[][] graph;

        try {
            Scanner fileScanner = new Scanner(new File(filename));
            int vertices = Integer.parseInt(fileScanner.nextLine());
            graph = new int[vertices][];
            for (int i = 0; i < vertices; i++)
                graph[i] = Arrays.stream(fileScanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        } catch (FileNotFoundException ex) {
            System.out.println("File " + filename + " not found.");
            return;
        }

        System.out.println("Input vertex to check (write 'all' to check each vertex): ");
        String vertex = inScanner.nextLine();
        if (!vertex.equals("all"))
            if (Integer.parseInt(vertex) >= graph.length)
                System.out.println("Vertex " + vertex + " is not a part of this graph.");
            else if (checkVertex(Integer.parseInt(vertex), graph, new boolean[graph.length]))
                System.out.println("Vertex " + vertex + " can be the initiator.");
            else System.out.println("Vertex " + vertex + " can't be the initiator.");
        else for (int i = 0; i < graph.length; i++)
            if (checkVertex(i, graph, new boolean[graph.length]))
                System.out.println("Vertex " + i + " can be the initiator.");
    }
}
