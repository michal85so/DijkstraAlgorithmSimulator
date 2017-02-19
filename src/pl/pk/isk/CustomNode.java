package pl.pk.isk;

public class CustomNode {
    private int id;
    public CustomNode(int id) {
        this.id = id;
    }
    public String toString() {
        return "V" + id;
    }
    public int getId() { return id; }
}
