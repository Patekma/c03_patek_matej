import fill.Filler;
import fill.SeedFiller;
import model.Point;
import model.Polygon;
import rasterize.*;
import model.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Canvas {
    private final JFrame frame;
    private final JPanel panel;
    private RasterBufferImage raster;
    private LineRasterizer lineRasterizer;
    private Polygon polygon;
    private PolygonRasterizer polygonRasterizer;

    private TriangleRasterizer triangleRasterizer;

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    //kontrola v jakem modu se program nachazi (T: rovnoramenny trojuhelnik, L: Linka, default: polygon)
    private boolean triangleMode = false;
    private boolean lineMode = false;

    public Canvas(int width, int height) {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("PGRF1");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferImage(800, 600);
        lineRasterizer = new LineRasterizerGraphics(raster);
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);
        triangleRasterizer = new TriangleRasterizer(lineRasterizer);
        polygon = new Polygon();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                raster.present(g);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        //vykreslení linky pomoci 2 bodu

        //press: zadani 1. bodu
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (!lineMode) {
                        return;
                    }
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        super.mousePressed(e);
                        raster.clear();
                        x1 = e.getX();
                        y1 = e.getY();
                    }
                }

                //release: zadani 2. bodu
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!lineMode) {
                        return;
                    }
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        super.mouseReleased(e);
                        raster.clear();
                        x2 = e.getX();
                        y2 = e.getY();
                        Line line = new Line(x1, y1, x2, y2);
                        lineRasterizer.rasterize(line);
                        panel.repaint();
                    }
                }
            });

            //vykreslovani táhnutim myši
            panel.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (!lineMode) {
                        return;
                    }
                    super.mouseDragged(e);
                    raster.clear();
                    Line line = new Line(x1, y1, e.getX(), e.getY());
                    lineRasterizer.rasterize(line);
                    panel.repaint();
                }
            });


        //polygon
        if (!triangleMode) {
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    if (triangleMode) {
                        return;
                    }
                    if (lineMode) {
                        return;
                    }
                    raster.clear();
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        polygon.addPoint(new Point(e.getX(), e.getY()));
                    }
                    if (polygon.getCount() >= 2) {
                        panel.addMouseMotionListener(new MouseAdapter() {
                            @Override
                            public void mouseDragged(MouseEvent e) {
                                super.mouseDragged(e);
                                if (triangleMode) {
                                    return;
                                }
                                if (lineMode) {
                                    return;
                                }
                                raster.clear();
                                for (int i = 0; i < polygon.getCount() - 1; i++) {
                                    Line line1 = new Line(polygon.getPoint(i).getX(), polygon.getPoint(i).getY(), polygon.getPoint(i + 1).getX(), polygon.getPoint(i + 1).getY());
                                    lineRasterizer.rasterize(line1);
                                    Line line2 = new Line(polygon.getPoint(polygon.getCount() - 1).getX(), polygon.getPoint(polygon.getCount() - 1).getY(), e.getX(), e.getY());
                                    lineRasterizer.rasterize(line2);
                                    Line line3 = new Line(polygon.getPoint(0).getX(), polygon.getPoint(0).getY(), e.getX(), e.getY());
                                    lineRasterizer.rasterize(line3);
                                    panel.repaint();
                                }
                            }
                        });
                        polygonRasterizer.rasterize(polygon);
                        panel.repaint();
                    }
                }
            });
        }


        //prepinani na vykreslovani linky pomoci klavesy L
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    triangleMode = false;
                    lineMode = !lineMode;
                    polygon.clearPoints();
                    raster.clear();
                    panel.repaint();
                    System.out.println("lineMode: " + lineMode);
                }
            }
        });

        //trojuhelnik pomoci klavesy T

        //prepinani modu pomoci klavesy T
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_T) {
                    lineMode = false;
                    triangleMode = !triangleMode;
                    polygon.clearPoints();
                    raster.clear();
                    panel.repaint();
                    System.out.println("triangleMode: " + triangleMode);
                }
            }
        });

        //vykresleni rovnoramenneho trojuhelniku
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (!triangleMode) {
                    return;
                }
                raster.clear();
                if (e.getButton() == MouseEvent.BUTTON1) {
                    polygon.addPoint(new Point(e.getX(), e.getY()));
                }
                triangleRasterizer.rasterize(polygon);
                panel.repaint();
            }
        });


        //smazani platna pomoci klavesy C

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    polygon.clearPoints();
                    raster.clear();
                    panel.repaint();
                }
            }
        });
    }

    public void start() {
        raster.clear();
        panel.repaint();
    }
}




