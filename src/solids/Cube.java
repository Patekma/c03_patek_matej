package solids;

import transforms.Point3D;

public class Cube extends Solid {

    public Cube() {
        // Geometrie
        vb.add(new Point3D(200, 400, 1));
        vb.add(new Point3D(400, 400, 1));
        vb.add(new Point3D(400, 200, 1));
        vb.add(new Point3D(200, 200, 1));

        // Topologie
        addIndices(0, 1, 1, 2, 2, 3, 3, 0);

    }
}
