package by.bsuir.polyline;



import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Figure implements Serializable {
	private List<Point> points ;
	private static final long serialVersionUID = 1L;
	public abstract void draw(Graphics g);
	public List<Point> getPoints() {
		return points;
	}
	public void addPoint(Point point){
		if (points == null) {
			points = new ArrayList<Point>();
			points.add(point);
		}
		else{
			points.add(point);
		}
	}
	public void addPoints(List<Point> points) {
		this.points = points;
	}
}
