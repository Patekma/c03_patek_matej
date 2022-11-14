package model;

public class Edge {
    private final int x1, y1, x2, y2;

    public Edge(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public boolean isHorizontal() {
        return y1 == y2;
    }

    public void orientate() {
        // TODO: vyřešit orientaci
    }

    public boolean isIntersection(int y) {
        // TODO: podmínka, jestli existuje průsečík
        // TODO: vyřešit zkrácení o 1 pixel
        return true;
    }

    public int getIntersection(int y) {
        // TODO: spočítat průsečík
        return 0;
    }
}
