package by.bsuir.polyline;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

public class Polyline extends Figure {
	private static final long serialVersionUID = 1L;
	public Polyline(List<Point> points) {
		this.addPoints(points);
	}

	@Override
	public void draw(Graphics g) {
		for (int i = 0; i < getPoints().size() - 1; i++) {
			Point p1 = getPoints().get(i);
			Point p2 = getPoints().get(i + 1);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
}
