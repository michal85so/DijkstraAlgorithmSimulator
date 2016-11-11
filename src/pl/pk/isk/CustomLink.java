package pl.pk.isk;

public class CustomLink {
    private double capacity;
    private int weight;
    private int id;
    private static int idGenerator = 0;

    public CustomLink(double capacity, int weight) {
        this.id = ++idGenerator;
        this.capacity = capacity;
        this.weight = weight;
    }

    public String toString() {
        return "E" + id + " Cap=" + capacity + " Wei=" + weight;
    }
}
