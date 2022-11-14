package rasterize;

import model.Line;
import model.Point;
import model.Polygon;

public class TriangleRasterizer {
    private LineRasterizer lineRasterizer;

    public TriangleRasterizer(LineRasterizer lineRasterizer) { this.lineRasterizer = lineRasterizer; }

    private Point point0;
    private Point point1;
    private Point point2;
    private double x;
    private double y;

    public void rasterize(Polygon polygon){
        if (polygon.getCount() < 3){ return; }
        point0 = polygon.getPoint(0);
        point1 = polygon.getPoint(1);
        point2 = polygon.getPoint(2);

        double x1 = point0.getX() - point1.getX();
        double y1 = point0.getY() - point1.getY();

        double mx = point0.getX() + x1 * -0.5;
        double my = point0.getY() + y1 * -0.5;

        double a = (double) (point1.getY() - point0.getY()) / (point1.getX() - point0.getX());
        double b = point2.getY() - a * point2.getX();

        double k = -1.0 / a;
        double q = my - k * mx;

        x = (q - b) / (a - k);
        y = a * x + b;

        lineRasterizer.rasterize(new Line(point0.getX(), point0.getY(), point1.getX(), point1.getY()));
        lineRasterizer.rasterize(new Line(point0.getX(), point0.getY(), (int) Math.round(x), (int) Math.round(y)));
        lineRasterizer.rasterize(new Line(point1.getX(), point1.getY(), (int) Math.round(x), (int) Math.round(y)));
    }
}
