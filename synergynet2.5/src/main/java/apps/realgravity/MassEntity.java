/**
 * from http://java.sun.com/j2se/1.5.0/docs/guide/language/enums.html
 */

package apps.realgravity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

public class MassEntity implements Serializable, Cloneable {

	private static final long serialVersionUID = -2001739616370432224L;
	
	public static final MassEntity SUN     = new MassEntity("Sun", 1.9891e30, 1.392e9);
    public static final MassEntity MERCURY = new MassEntity("Mercury", 3.303e+23, 2.4397e6);
    public static final MassEntity VENUS   = new MassEntity("Venus", 4.869e+24, 6.0518e6);
    public static final MassEntity EARTH   = new MassEntity("Earth", 5.976e+24, 6.37814e6);
    public static final MassEntity MARS    = new MassEntity("Mars", 6.421e+23, 3.3972e6);
    public static final MassEntity JUPITER = new MassEntity("Jupiter", 1.9e+27,   7.1492e7);
    public static final MassEntity SATURN  = new MassEntity("Saturn", 5.688e+26, 6.0268e7);
    public static final MassEntity URANUS  = new MassEntity("Uranus", 8.686e+25, 2.5559e7);
    public static final MassEntity NEPTUNE = new MassEntity("Nepture", 1.024e+26, 2.4746e7);
    public static final MassEntity PLUTO   = new MassEntity("Pluto", 1.27e+22,  1.137e6);
    public static final MassEntity LUNA    = new MassEntity("Luna", 7.3477e22, 1.737e6);

    private String name;
    private double mass;   // in kilograms
    private double radius; // in meters
    private Point.Double pos = new Point.Double();			 // position in meters
    private Point.Double vel = new Point.Double();			 // velocity in meters per hour
    
    
    MassEntity(String name, double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
        this.name = name;
    }
    
    public double getMass()   { return mass; }
    public double getRadius() { return radius; }
    
    public void setRadius(double r) {
    	this.radius = r;
    }

    // universal gravitational constant  (m3 kg-1 s-2)

    public double surfaceGravity() {
        return Universe.G * mass / (radius * radius);
    }
    
    public double surfaceWeight(double otherMass) {
        return otherMass * surfaceGravity();
    }

	public void setPos(Point.Double pos) {
		this.pos = pos;
	}

	public Point.Double getPos() {
		return pos;
	}

	public void setVel(Point.Double vel) {
		this.vel = vel;
	}

	public Point.Double getVel() {
		return vel;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof MassEntity) {
			MassEntity me = (MassEntity) obj;
			return me.getName().equals(name) && me.getPos().equals(pos);
		}
		return false;
	}

	public Object clone() {
		MassEntity me = new MassEntity(this.getName(), this.getMass(), this.getRadius());
		me.pos = new Point.Double(pos.x, pos.y);
		me.vel = new Point.Double(vel.x, vel.y);
		return me;
	}
	
	public String toString() {
		return getName() + " @" + pos + " -> " + vel;
	}
	
	public void render(Graphics2D g2d, int xOffset, int yOffset, double metersPerPixel) {		
		int locX = (int)(pos.x / metersPerPixel);
		int locY = (int)(pos.y / metersPerPixel);
		int pixelRadius = (int)(radius / metersPerPixel);
		
		int x = xOffset + locX - (pixelRadius/2);
		int y = yOffset + locY - (pixelRadius/2);
				
		g2d.setColor(Color.green);
		g2d.fillOval(x, y, pixelRadius, pixelRadius);		
	}

}