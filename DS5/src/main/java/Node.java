class Node {
    final int id;
    public int u;
    public int v;

    public Node(int id, int u, int v) {
        this.id = id;
        this.u = u;
        this.v = v;
    }

    public void block(Node other) {
        System.out.println("BLOCK RULE");
        System.out.println("initial: " + other + " -> " + this);
        other.u = Math.max(this.u, other.u) + 1;
        other.v = other.u;
        System.out.println("result: " + other + " -> " + this);
    }

    public void transmit(Node other) {
        System.out.println("TRANSMIT RULE");
        System.out.println("initial: " + other + " -> " + this);
        other.u = Math.max(other.u, this.u);
        System.out.println("result: " + other + " -> " + this);
    }

    public boolean detect(Node other) {
        if (this.u == this.v && this.u == other.u) {
            System.err.println("DEADLOCK DETECTED!");
            System.out.println(other + "-> " + this);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.id + " (" + this.u + ", " + this.v + ")";
    }
}
