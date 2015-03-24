/**
 * from http://java.sun.com/j2se/1.5.0/docs/guide/language/enums.html
 */

package apps.realgravity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

/**
 * The Class MassEntity.
 */
public class MassEntity implements Serializable, Cloneable {
	
	/** The Constant EARTH. */
	public static final MassEntity EARTH = new MassEntity("Earth", 5.976e+24,
			6.37814e6);

	/** The Constant JUPITER. */
	public static final MassEntity JUPITER = new MassEntity("Jupiter", 1.9e+27,
			7.1492e7);
	
	/** The Constant LUNA. */
	public static final MassEntity LUNA = new MassEntity("Luna", 7.3477e22,
			1.737e6);
	
	/** The Constant MARS. */
	public static final MassEntity MARS = new MassEntity("Mars", 6.421e+23,
			3.3972e6);
	
	/** The Constant MERCURY. */
	public static final MassEntity MERCURY = new MassEntity("Mercury",
			3.303e+23, 2.4397e6);
	
	/** The Constant NEPTUNE. */
	public static final MassEntity NEPTUNE = new MassEntity("Nepture",
			1.024e+26, 2.4746e7);
	
	/** The Constant PLUTO. */
	public static final MassEntity PLUTO = new MassEntity("Pluto", 1.27e+22,
			1.137e6);
	
	/** The Constant SATURN. */
	public static final MassEntity SATURN = new MassEntity("Saturn", 5.688e+26,
			6.0268e7);
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2001739616370432224L;
	
	/** The Constant SUN. */
	public static final MassEntity SUN = new MassEntity("Sun", 1.9891e30,
			1.392e9);
	
	/** The Constant URANUS. */
	public static final MassEntity URANUS = new MassEntity("Uranus", 8.686e+25,
			2.5559e7);
	
	/** The Constant VENUS. */
	public static final MassEntity VENUS = new MassEntity("Venus", 4.869e+24,
			6.0518e6);
	
	/** The mass. */
	private double mass; // in kilograms
	
	/** The name. */
	private String name;
	
	/** The pos. */
	private Point.Double pos = new Point.Double(); // position in meters
	
	/** The radius. */
	private double radius; // in meters
	
	/** The vel. */
	private Point.Double vel = new Point.Double(); // velocity in meters per
													// hour
	
	/**
	 * Instantiates a new mass entity.
	 *
	 * @param name
	 *            the name
	 * @param mass
	 *            the mass
	 * @param radius
	 *            the radius
	 */
	MassEntity(String name, double mass, double radius) {
		this.mass = mass;
		this.radius = radius;
		this.name = name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		MassEntity me = new MassEntity(this.getName(), this.getMass(),
				this.getRadius());
		me.pos = new Point.Double(pos.x, pos.y);
		me.vel = new Point.Double(vel.x, vel.y);
		return me;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof MassEntity) {
			MassEntity me = (MassEntity) obj;
			return me.getName().equals(name) && me.getPos().equals(pos);
		}
		return false;
	}
	
	/**
	 * Gets the mass.
	 *
	 * @return the mass
	 */
	public double getMass() {
		return mass;
	}
	
	// universal gravitational constant (m3 kg-1 s-2)
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the pos.
	 *
	 * @return the pos
	 */
	public Point.Double getPos() {
		return pos;
	}
	
	/**
	 * Gets the radius.
	 *
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Gets the vel.
	 *
	 * @return the vel
	 */
	public Point.Double getVel() {
		return vel;
	}
	
	/**
	 * Render.
	 *
	 * @param g2d
	 *            the g2d
	 * @param xOffset
	 *            the x offset
	 * @param yOffset
	 *            the y offset
	 * @param metersPerPixel
	 *            the meters per pixel
	 */
	public void render(Graphics2D g2d, int xOffset, int yOffset,
			double metersPerPixel) {
		int locX = (int) (pos.x / metersPerPixel);
		int locY = (int) (pos.y / metersPerPixel);
		int pixelRadius = (int) (radius / metersPerPixel);

		int x = (xOffset + locX) - (pixelRadius / 2);
		int y = (yOffset + locY) - (pixelRadius / 2);
		
		g2d.setColor(Color.green);
		g2d.fillOval(x, y, pixelRadius, pixelRadius);
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the pos.
	 *
	 * @param pos
	 *            the new pos
	 */
	public void setPos(Point.Double pos) {
		this.pos = pos;
	}
	
	/**
	 * Sets the radius.
	 *
	 * @param r
	 *            the new radius
	 */
	public void setRadius(double r) {
		this.radius = r;
	}

	/**
	 * Sets the vel.
	 *
	 * @param vel
	 *            the new vel
	 */
	public void setVel(Point.Double vel) {
		this.vel = vel;
	}
	
	/**
	 * Surface gravity.
	 *
	 * @return the double
	 */
	public double surfaceGravity() {
		return (Universe.G * mass) / (radius * radius);
	}

	/**
	 * Surface weight.
	 *
	 * @param otherMass
	 *            the other mass
	 * @return the double
	 */
	public double surfaceWeight(double otherMass) {
		return otherMass * surfaceGravity();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName() + " @" + pos + " -> " + vel;
	}
	
}