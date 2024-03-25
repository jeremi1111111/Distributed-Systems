import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner inScanner = new Scanner(System.in);
        System.out.println("Input file name:");
        String fileName = "";
        Scanner fileScanner;
        while (true) {
            try {
                fileName = inScanner.next();
                File file = new File(fileName);
                fileScanner = new Scanner(file);
                break;
            } catch (FileNotFoundException e) {
                System.out.println("No file named \"" + fileName + "\" was found. Try again:");
            }
        }
        TreeNode.system.add(new TreeNode(0, "root", null));
        int idCounter = 0;
        while (fileScanner.hasNextLine()) {
            idCounter++;
            String[] params = fileScanner.nextLine().split(",");
            TreeNode.system.add(new TreeNode(idCounter, params[0], TreeNode.system.get(TreeNode.indexMap.get(params[1].strip()))));
        }
        System.out.println("\nImported tree from file. Sending token to node " + TreeNode.system.get(1).getName() + ".");
        TreeNode.system.get(1).passToken(0, 0);
        while (true) {
            System.out.println("Pick a node for a request: ");
            String name = inScanner.next();
            Integer id = TreeNode.indexMap.get(name);
            if (id == null)
                try {
                    id = Integer.parseInt(name);
                } catch (Exception ex) {
                    System.out.println("Node " + name + " wasn't found.");
                    continue;
                }
            System.out.println("Added token request for node " + name + ".");
            TreeNode.system.get(id).requestToken(id, 0);
        }
    }
}