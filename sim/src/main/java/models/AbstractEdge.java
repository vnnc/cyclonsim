package models;

public abstract class AbstractEdge<T> {
	T x;
	T y;

	public AbstractEdge(T n1, T n2) {
		this.x = n1;
		this.y = n2;
	}

	@Override
	public String toString() {
		return "["+x+" "+y+"]";
	}
}
