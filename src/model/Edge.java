package model;

public class Edge {
    private int x1, y1, x2, y2;
    double k, q;

    public Edge(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        k = (y2 - y1) / (double) (x2 - x1);
        q = y1 - k * x1;
    }

    public boolean isHorizontal() {
        return y1 == y2;
    }

    public void orientate() {
        //vyřešit orientaci
        if (y1 > y2){
            int tempY = y1;
            int tempX = x1;
            y1 = y2;
            y2 = tempY;
            x1 = x2;
            x2 = tempX;
        }
    }

    public boolean isIntersection(int y) {
        //podmínka, jestli existuje průsečík
        return y1 <= y && y2 >= y;
    }

    //zkrácení o 1 pixel
    public void shorten(){
        if (x1 == x2){
            y2 -= 1;
        }
        x2 = (int)Math.round(((y2-1) - q)/k);
        y2 -= 1;
    }

    public int getIntersection(int y) {
        //spočítat průsečík
        float k = (x2 - x1) / (float) (y2 - y1);
        float q = (x1 - (k * y1));
        float x = (k * y) + q;
        return (int) x;
    }
}
