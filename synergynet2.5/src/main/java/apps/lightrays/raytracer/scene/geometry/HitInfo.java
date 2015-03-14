package apps.lightrays.raytracer.scene.geometry;

import apps.lightrays.raytracer.scene.Ray;
import apps.lightrays.raytracer.scene.Vector;

public class HitInfo {
	    Object ignore; // One object to ignore (used by Cast).
	    Object object; // The object that was hit (set by Intersect).
	    double  distance;     // Distance to hit (used & reset by Intersect).
	    Vector    point;        // ray-object intersection point (set by Intersect).
	    Vector    normal;       // Surface normal (set by Intersect).
	    Vector    uv;           // Texture coordinates (set by intersect).
	    Ray     ray;          // The ray that hit the surface (set by Intersect).
	    
}
