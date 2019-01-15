package com.volmit.phantom.api.fission;

import java.io.Serializable;

import javax.annotation.Nullable;

import org.bukkit.Location;

public class FVector implements Comparable<FVector>, Serializable
{
	private static final long serialVersionUID = -8111230409815199389L;
	public static final FVector ZERO = new FVector(0, 0, 0);
	public static final FVector UNIT_X = new FVector(1, 0, 0);
	public static final FVector UNIT_Y = new FVector(0, 1, 0);
	public static final FVector UNIT_Z = new FVector(0, 0, 1);
	public static final FVector ONE = new FVector(1, 1, 1);
	private transient double x;
	private transient double y;
	private transient double z;
	private transient int sy;

	public FVector(double x, double y, double z)
	{
		this.mutX(x);
		this.mutY(y);
		this.mutZ(z);
	}

	public FVector(int x, int y, int z)
	{
		this.mutX(x);
		this.mutY(y);
		this.mutZ(z);
	}

	public FVector(float x, float y, float z)
	{
		this.mutX((double) x);
		this.mutY((double) y);
		this.mutZ((double) z);
	}

	public FVector(FVector other)
	{
		this.mutX(other.getX());
		this.mutY(other.getY());
		this.mutZ(other.getZ());
	}

	public FVector(double[] arr)
	{
		this.mutX(arr[0]);
		this.mutY(arr[1]);
		this.mutZ(arr[2]);
	}

	public FVector()
	{
		this.mutX(0);
		this.mutY(0);
		this.mutZ(0);
	}

	public FVector(Location l)
	{
		this.mutX(roundInt(l.getX()));
		this.mutY(roundInt(l.getY()));
		this.mutZ(roundInt(l.getZ()));
	}

	public FVector setComponents(int x, int y, int z)
	{
		this.mutX(x);
		this.mutY(y);
		this.mutZ(z);
		return this;
	}

	public FVector setComponents(double x, double y, double z)
	{
		this.mutX(x);
		this.mutY(y);
		this.mutZ(z);
		return this;
	}

	public void mutX(int x)
	{
		this.x = (double) x;
	}

	public void mutY(int y)
	{
		this.y = (double) y;
	}

	public void mutZ(int z)
	{
		this.z = (double) z;
	}

	public void mutX(double x)
	{
		this.x = x;
	}

	public void mutY(double y)
	{
		this.y = y;
		this.sy = roundInt(y) >> 4;
	}

	public void mutZ(double z)
	{
		this.z = z;
	}

	public double getX()
	{
		return this.x;
	}

	public static final int roundInt(double value)
	{
		return (int) (value < 0.0D ? (value == (double) ((int) value) ? value : value - 1.0D) : value);
	}

	public int getBlockX()
	{
		return roundInt(this.getX());
	}

	public FVector setX(double x)
	{
		return new FVector(x, this.getY(), this.getZ());
	}

	public FVector setX(int x)
	{
		return new FVector((double) x, this.getY(), this.getZ());
	}

	public double getY()
	{
		return this.y;
	}

	public int getBlockY()
	{
		return roundInt(this.getY());
	}

	public FVector setY(double y)
	{
		return new FVector(this.getX(), y, this.getZ());
	}

	public FVector setY(int y)
	{
		return new FVector(this.getX(), (double) y, this.getZ());
	}

	public double getZ()
	{
		return this.z;
	}

	public int getBlockZ()
	{
		return roundInt(this.getZ());
	}

	public FVector setZ(double z)
	{
		return new FVector(this.getX(), this.getY(), z);
	}

	public FVector setZ(int z)
	{
		return new FVector(this.getX(), this.getY(), (double) z);
	}

	public FVector add(FVector other)
	{
		return new FVector(this.getX() + other.getX(), this.getY() + other.getY(), this.getZ() + other.getZ());
	}

	public FVector add(double x, double y, double z)
	{
		return new FVector(this.getX() + x, this.getY() + y, this.getZ() + z);
	}

	public FVector add(int x, int y, int z)
	{
		return new FVector(this.getX() + (double) x, this.getY() + (double) y, this.getZ() + (double) z);
	}

	public FVector add(FVector... others)
	{
		double newX = this.getX();
		double newY = this.getY();
		double newZ = this.getZ();
		FVector[] var8 = others;
		int var9 = others.length;

		for(int var10 = 0; var10 < var9; ++var10)
		{
			FVector other = var8[var10];
			newX += other.getX();
			newY += other.getY();
			newZ += other.getZ();
		}

		return new FVector(newX, newY, newZ);
	}

	public FVector subtract(FVector other)
	{
		return new FVector(this.getX() - other.getX(), this.getY() - other.getY(), this.getZ() - other.getZ());
	}

	public FVector subtract(double x, double y, double z)
	{
		return new FVector(this.getX() - x, this.getY() - y, this.getZ() - z);
	}

	public FVector subtract(int x, int y, int z)
	{
		return new FVector(this.getX() - (double) x, this.getY() - (double) y, this.getZ() - (double) z);
	}

	public FVector subtract(FVector... others)
	{
		double newX = this.getX();
		double newY = this.getY();
		double newZ = this.getZ();
		FVector[] var8 = others;
		int var9 = others.length;

		for(int var10 = 0; var10 < var9; ++var10)
		{
			FVector other = var8[var10];
			newX -= other.getX();
			newY -= other.getY();
			newZ -= other.getZ();
		}

		return new FVector(newX, newY, newZ);
	}

	public FVector multiply(FVector other)
	{
		return new FVector(this.getX() * other.getX(), this.getY() * other.getY(), this.getZ() * other.getZ());
	}

	public FVector multiply(double x, double y, double z)
	{
		return new FVector(this.getX() * x, this.getY() * y, this.getZ() * z);
	}

	public FVector multiply(int x, int y, int z)
	{
		return new FVector(this.getX() * (double) x, this.getY() * (double) y, this.getZ() * (double) z);
	}

	public FVector multiply(FVector... others)
	{
		double newX = this.getX();
		double newY = this.getY();
		double newZ = this.getZ();
		FVector[] var8 = others;
		int var9 = others.length;

		for(int var10 = 0; var10 < var9; ++var10)
		{
			FVector other = var8[var10];
			newX *= other.getX();
			newY *= other.getY();
			newZ *= other.getZ();
		}

		return new FVector(newX, newY, newZ);
	}

	public FVector multiply(double n)
	{
		return new FVector(this.getX() * n, this.getY() * n, this.getZ() * n);
	}

	public FVector multiply(float n)
	{
		return new FVector(this.getX() * (double) n, this.getY() * (double) n, this.getZ() * (double) n);
	}

	public FVector multiply(int n)
	{
		return new FVector(this.getX() * (double) n, this.getY() * (double) n, this.getZ() * (double) n);
	}

	public FVector divide(FVector other)
	{
		return new FVector(this.getX() / other.getX(), this.getY() / other.getY(), this.getZ() / other.getZ());
	}

	public FVector divide(double x, double y, double z)
	{
		return new FVector(this.getX() / x, this.getY() / y, this.getZ() / z);
	}

	public FVector divide(int x, int y, int z)
	{
		return new FVector(this.getX() / (double) x, this.getY() / (double) y, this.getZ() / (double) z);
	}

	public FVector divide(int n)
	{
		return new FVector(this.getX() / (double) n, this.getY() / (double) n, this.getZ() / (double) n);
	}

	public FVector divide(double n)
	{
		return new FVector(this.getX() / n, this.getY() / n, this.getZ() / n);
	}

	public FVector divide(float n)
	{
		return new FVector(this.getX() / (double) n, this.getY() / (double) n, this.getZ() / (double) n);
	}

	public double length()
	{
		return Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ());
	}

	public double lengthSq()
	{
		return this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ();
	}

	public double volume()
	{
		return this.getX() * this.getY() * this.getZ();
	}

	public double distance(FVector other)
	{
		return Math.sqrt(Math.pow(other.getX() - this.getX(), 2.0D) + Math.pow(other.getY() - this.getY(), 2.0D) + Math.pow(other.getZ() - this.getZ(), 2.0D));
	}

	public double distanceSq(FVector other)
	{
		return Math.pow(other.getX() - this.getX(), 2.0D) + Math.pow(other.getY() - this.getY(), 2.0D) + Math.pow(other.getZ() - this.getZ(), 2.0D);
	}

	public FVector normalize()
	{
		return this.divide(this.length());
	}

	public double dot(FVector other)
	{
		return this.getX() * other.getX() + this.getY() * other.getY() + this.getZ() * other.getZ();
	}

	public FVector cross(FVector other)
	{
		return new FVector(this.getY() * other.getZ() - this.getZ() * other.getY(), this.getZ() * other.getX() - this.getX() * other.getZ(), this.getX() * other.getY() - this.getY() * other.getX());
	}

	public boolean containedWithin(FVector min, FVector max)
	{
		return this.getX() >= min.getX() && this.getX() <= max.getX() && this.getY() >= min.getY() && this.getY() <= max.getY() && this.getZ() >= min.getZ() && this.getZ() <= max.getZ();
	}

	public boolean containedWithinBlock(FVector min, FVector max)
	{
		return this.getBlockX() >= min.getBlockX() && this.getBlockX() <= max.getBlockX() && this.getBlockY() >= min.getBlockY() && this.getBlockY() <= max.getBlockY() && this.getBlockZ() >= min.getBlockZ() && this.getBlockZ() <= max.getBlockZ();
	}

	public FVector clampY(int min, int max)
	{
		return new FVector(this.getX(), Math.max((double) min, Math.min((double) max, this.getY())), this.getZ());
	}

	public FVector floor()
	{
		return new FVector(Math.floor(this.getX()), Math.floor(this.getY()), Math.floor(this.getZ()));
	}

	public FVector ceil()
	{
		return new FVector(Math.ceil(this.getX()), Math.ceil(this.getY()), Math.ceil(this.getZ()));
	}

	public FVector round()
	{
		return new FVector(Math.floor(this.getX() + 0.5D), Math.floor(this.getY() + 0.5D), Math.floor(this.getZ() + 0.5D));
	}

	public FVector positive()
	{
		return new FVector(Math.abs(this.getX()), Math.abs(this.getY()), Math.abs(this.getZ()));
	}

	public FVector transform2D(double angle, double aboutX, double aboutZ, double translateX, double translateZ)
	{
		angle = Math.toRadians(angle);
		double x = this.getX() - aboutX;
		double z = this.getZ() - aboutZ;
		double x2 = x * Math.cos(angle) - z * Math.sin(angle);
		double z2 = x * Math.sin(angle) + z * Math.cos(angle);
		return new FVector(x2 + aboutX + translateX, this.getY(), z2 + aboutZ + translateZ);
	}

	public boolean isCollinearWith(FVector other)
	{
		if(this.getX() == 0.0D && this.getY() == 0.0D && this.getZ() == 0.0D)
		{
			return true;
		}
		else
		{
			double otherX = other.getX();
			double otherY = other.getY();
			double otherZ = other.getZ();
			if(otherX == 0.0D && otherY == 0.0D && otherZ == 0.0D)
			{
				return true;
			}
			else if(this.getX() == 0.0D != (otherX == 0.0D))
			{
				return false;
			}
			else if(this.getY() == 0.0D != (otherY == 0.0D))
			{
				return false;
			}
			else if(this.getZ() == 0.0D != (otherZ == 0.0D))
			{
				return false;
			}
			else
			{
				double quotientX = otherX / this.getX();
				if(!Double.isNaN(quotientX))
				{
					return other.equals(this.multiply(quotientX));
				}
				else
				{
					double quotientY = otherY / this.getY();
					if(!Double.isNaN(quotientY))
					{
						return other.equals(this.multiply(quotientY));
					}
					else
					{
						double quotientZ = otherZ / this.getZ();
						if(!Double.isNaN(quotientZ))
						{
							return other.equals(this.multiply(quotientZ));
						}
						else
						{
							throw new RuntimeException("This should not happen");
						}
					}
				}
			}
		}
	}

	public float toPitch()
	{
		double x = this.getX();
		double z = this.getZ();
		if(x == 0.0D && z == 0.0D)
		{
			return this.getY() > 0.0D ? -90.0F : 90.0F;
		}
		else
		{
			double x2 = x * x;
			double z2 = z * z;
			double xz = Math.sqrt(x2 + z2);
			return (float) Math.toDegrees(Math.atan(-this.getY() / xz));
		}
	}

	public float toYaw()
	{
		double x = this.getX();
		double z = this.getZ();
		double t = Math.atan2(-x, z);
		double _2pi = 6.283185307179586D;
		return (float) Math.toDegrees((t + _2pi) % _2pi);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof FVector))
		{
			return false;
		}
		else
		{
			FVector other = (FVector) obj;
			return other.getX() == this.getX() && other.getZ() == this.getZ() && other.getY() == this.getY();
		}
	}

	@Override
	public int compareTo(@Nullable FVector other)
	{
		if(other == null)
		{
			throw new IllegalArgumentException("null not supported");
		}
		else if(this.getY() != other.getY())
		{
			return Double.compare(this.getY(), other.getY());
		}
		else if(this.getZ() != other.getZ())
		{
			return Double.compare(this.getZ(), other.getZ());
		}
		else
		{
			return this.getX() != other.getX() ? Double.compare(this.getX(), other.getX()) : 0;
		}
	}

	@Override
	public int hashCode()
	{
		return (int) this.getX() ^ (int) this.getZ() << 16 ^ (int) this.getY() << 30;
	}

	@Override
	public String toString()
	{
		String x = this.getX() == (double) this.getBlockX() ? "" + this.getBlockX() : "" + this.getX();
		String y = this.getY() == (double) this.getBlockY() ? "" + this.getBlockY() : "" + this.getY();
		String z = this.getZ() == (double) this.getBlockZ() ? "" + this.getBlockZ() : "" + this.getZ();
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public static FVector getMinimum(FVector v1, FVector v2)
	{
		return new FVector(Math.min(v1.getX(), v2.getX()), Math.min(v1.getY(), v2.getY()), Math.min(v1.getZ(), v2.getZ()));
	}

	public static FVector getMaximum(FVector v1, FVector v2)
	{
		return new FVector(Math.max(v1.getX(), v2.getX()), Math.max(v1.getY(), v2.getY()), Math.max(v1.getZ(), v2.getZ()));
	}

	public static FVector getMidpoint(FVector v1, FVector v2)
	{
		return new FVector((v1.getX() + v2.getX()) / 2.0D, (v1.getY() + v2.getY()) / 2.0D, (v1.getZ() + v2.getZ()) / 2.0D);
	}

	public static Class<?> inject()
	{
		return FVector.class;
	}

	public int sectionY()
	{
		return sy;
	}
}