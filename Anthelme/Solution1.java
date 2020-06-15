import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;

//Anthelme Clisson 2000114, Samuel Lavallée 2032266
public class Solution {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean isBfs = scanner.nextLine().equals("1");
        List<List<Integer>> world = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String listString = scanner.nextLine();

            if (listString.equals(""))
                break;

            List<Integer> row = Arrays
                    .stream(listString.split(" +"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            world.add(row);
        }

        World w = new World(world, isBfs);

        w.getContinents();
    }
}

class World {
    private Integer[][] world;
    private boolean isBfs;

    public World(List<List<Integer>> world, boolean isBfs) {
        this.world = new Integer[world.size()][world.get(0).size()];
        this.isBfs = isBfs;

        for (int i = 0; i < world.size(); i++) {
            for (int j = 0; j < world.get(0).size(); j++){
                this.world[i][j] = world.get(i).get(j);
            }
        }
    }

    public TreeSet<Integer> bfs(Point start, boolean visited[][]) {
        ArrayDeque<Point> queue = new ArrayDeque<>();
        queue.addLast(start);
        TreeSet<Integer> countries = new TreeSet<>();
        countries.add(this.world[start.x][start.y]);

        visited[start.x][start.y] = true;

        while (!queue.isEmpty()) {
            Point current = queue.pollFirst();
            List<Point> neighbours = getNeighbours(current);

            for (Point next : neighbours) {
                if (!visited[next.x][next.y] && !isWater(next)) {
                    queue.addLast(next);
                    visited[next.x][next.y] = true;

                    countries.add(this.world[next.x][next.y]);
                }
            }
        }

        return countries;
    }

    public void dfsRecursive(Point current, boolean[][] visited, TreeSet<Integer> countries) {
        List<Point> neighbours = getNeighbours(current);
        visited[current.x][current.y] = true;
        countries.add(this.world[current.x][current.y]);

        for (Point next : neighbours) {
            if (!visited[next.x][next.y] && !isWater(next)) {
                dfsRecursive(next, visited, countries);
            }
        }
    }

    public TreeSet<Integer> dfs(Point start, boolean[][] visited) {
        ArrayDeque<Point> queue = new ArrayDeque<>();
        queue.addFirst(start);
        TreeSet<Integer> countries = new TreeSet<>();
        countries.add(this.world[start.x][start.y]);

        visited[start.x][start.y] = true;

        while (!queue.isEmpty()) {
            Point current = queue.pollFirst();
            List<Point> neighbours = getNeighbours(current);

            for (Point next : neighbours) {
                if (!visited[next.x][next.y] && !isWater(next)) {
                    queue.addFirst(next);
                    visited[next.x][next.y] = true;

                    countries.add(this.world[next.x][next.y]);
                }
            }
        }

        return countries;
    }

    public void getContinents() {
        boolean visited[][] = new boolean[world.length][world[0].length];

        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[0].length; j++) {
                if (!visited[i][j] && this.world[i][j] > 0) {
                    TreeSet<Integer> countries;
                    if (this.isBfs)
                        countries = bfs(new Point(i, j), visited);
                    else
                        countries = dfs(new Point(i, j), visited);

                    for (Integer r : countries)
                        System.out.print(r + " ");

                    System.out.println();
                }
            }
        }
    }

    public boolean isWater(Point p) {
        return this.world[p.x][p.y] == 0;
    }

    public List<Point> getNeighbours(Point p) {
        List<Point> neighbours = new ArrayList<>();

        if (p.x > 0)
            neighbours.add(new Point(p.x-1, p.y));
        if (p.x+1 < this.world.length)
            neighbours.add(new Point(p.x+1, p.y));
        if (p.y > 0)
            neighbours.add(new Point(p.x, p.y-1));
        if (p.y+1 < this.world[0].length)
            neighbours.add(new Point(p.x, p.y+1));

        return neighbours;
    }

    @Override
    public String toString() {
        String str = "";

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++){
                str += this.world[i][j] + " ";
            }
            str += "\n";
        }

        return str;
    }
}