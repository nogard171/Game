import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.util.Point;
import org.newdawn.slick.Color;

public class Object extends ObjectData
{
	public boolean collosion = false;

	public Point getPosition()
	{
		return new Point(this._bounds.x, this._bounds.y);
	}

	public void SetPosition(Point newPosition)
	{
		this._bounds = new Rectangle(newPosition.getX(), newPosition.getY() - 32, this._bounds.width,
				this._bounds.height);
	}

	public void SetPosition(float x, float y)
	{
		this._bounds = new Rectangle((int) x, (int) y, this._bounds.width, this._bounds.height);
	}

	Direction direction = Direction.NORTH;
	Direction collisionDir = Direction.NONE;
	public org.lwjgl.util.Point texture_Coords = new Point(0, 0);
	float tex_X = 0;
	public Color color = new Color(255, 255,255);
	private boolean hovered = false;
	public boolean isSolid = false;
	int level = 0;

	public boolean isSolid()
	{
		return isSolid;
	}

	public void setSolid(boolean isSolid)
	{
		this.isSolid = isSolid;
	}

	public boolean isHovered()
	{
		return hovered;
	}

	public void setHovered(boolean hovered)
	{
		this.hovered = hovered;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public void move(float x, float y)
	{
		float xSpeed = getPosition().getX();
		float rSpeed = 0;
		float lSpeed = 0;
		float ySpeed = getPosition().getY();
		float nSpeed = 0;
		float sSpeed = 0;

		if (x < 0)
		{
			direction = Direction.WEST;
			this.texture_Coords = new Point(0, 3);
			lSpeed = x;
		}
		if (x > 0)
		{
			direction = Direction.EAST;
			this.texture_Coords = new Point(0, 2);
			rSpeed = x;
		}
		if (y < 0)
		{
			nSpeed = y;
			direction = Direction.NORTH;
			this.texture_Coords = new Point(0, 1);
		}
		if (y > 0)
		{
			sSpeed = y;
			direction = Direction.SOUTH;
			this.texture_Coords = new Point(0, 0);
		}

		if (collisionDirection.contains(Direction.EAST))
		{
			rSpeed = 0;
			// rSpeed=backStep;
		}
		if (collisionDirection.contains(Direction.WEST))
		{
			// xSpeed+=backStep;
			lSpeed = 0;
		}

		if (collisionDirection.contains(Direction.NORTH))
		{
			nSpeed = 0;
		}
		if (collisionDirection.contains(Direction.SOUTH))
		{
			sSpeed = 0;
		}

		xSpeed += rSpeed + lSpeed;
		ySpeed += nSpeed + sSpeed;

		if (x != 0 || y != 0)
		{
			this.setTextureX(tex_X);
			if (tex_X > 4)
			{
				tex_X = 0;
			}
			tex_X += 0.05;
		} else
		{
			this.setTextureX(0);
		}
		this.SetPosition(xSpeed, ySpeed);
		if (collosion)
		{
			collisionDirection.add(direction);
		} else if (!collosion)
		{
			collisionDirection.clear();
		}

	}

	ArrayList<Direction> collisionDirection = new ArrayList<Direction>();

	public void setTextureX(float value)
	{
		this.texture_Coords = new Point((int) value, this.texture_Coords.getY());
	}

	public void setTextureY(float value)
	{
		this.texture_Coords = new Point(this.texture_Coords.getX(), (int) value);
	}

	public Dimension getSize()
	{
		return new Dimension(this._bounds.width, this._bounds.height);
	}

	public void setSize(Dimension size)
	{
		this._bounds = new Rectangle(this._bounds.x, this._bounds.y, size.width, size.height);
	}

	public void setSize(int width, int height)
	{
		this._bounds = new Rectangle(this._bounds.x, this._bounds.y, width, height);
	}

	public String getType()
	{
		return this._type;
	}

	public void setType(String newType)
	{
		this._type = newType;
	}

	public String getName()
	{
		// TODO Auto-generated method stub
		return this._name;
	}

}
