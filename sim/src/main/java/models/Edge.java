package models;

public class Edge<T> {

    T x;
    T y;

    public Edge(T n1, T n2)
    {
        this.x = n1;
        this.y = n2;
    }

    @Override
    public String toString() {
        return "["+x+" "+y+"]";
    }
}
