package net.chocomint.tdx.district

import org.locationtech.jts.geom.Polygonal

interface BoundaryShape {
    fun getBoundaryGeometry(): Polygonal
}