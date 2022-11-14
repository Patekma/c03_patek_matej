package rasterize;

import model.Line;
import model.Point;
import model.Polygon;

public class PolygonRasterizer {
    private LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    private Point point0;
    private Point point1;
    private Point point2;


    public void rasterize(Polygon polygon) {

        for (int i = 0; i < polygon.getCount() - 1; i++) {
            point0 = polygon.getPoint(0);
            point1 = polygon.getPoint(i);
            point2 = polygon.getPoint(i+1);
            lineRasterizer.rasterize(new Line(point1.getX(), point1.getY(), point2.getX(), point2.getY()));
        }
        lineRasterizer.rasterize(new Line(point2.getX(), point2.getY(), point0.getX(), point0.getY()));
    }
}
