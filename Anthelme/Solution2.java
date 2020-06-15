import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

enum MazeTile {
    Floor,
    Wall,
    Exit;

    @Override
    public String toString() {
        switch (this) {
            case Exit: return "*";
            case Floor: return " ";
            case Wall: return "#";
            default: return "";
        }
    }
}

//Anthelme Clisson 2000114, Samuel Lavallée 2032266
public class Solution2 {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        List<List<MazeTile>> board = new ArrayList<>();
        MazeTile[] mazeTiles = MazeTile.values();
        while (scanner.hasNextLine()) {
            String listString = scanner.nextLine();
            if (listString.equals(""))
                break;
            List<MazeTile> row = Arrays
                    .stream(listString.split(" +"))
                    .map(Integer::parseInt)
                    .map(i -> mazeTiles[i])
                    .collect(Collectors.toList());
            board.add(row);
        }

        Labyrinthe l = new Labyrinthe(board);

        System.out.print(l.findPath().size());

        //printBoard(board);
    }

    public static void printBoard(List<List<MazeTile>> board) {
        for (List<MazeTile> row : board) {
            System.out.println(row.stream().map(String::valueOf).collect(Collectors.joining(" ")));
        }
    }
}

class Labyrinthe {
    private MazeTile[][] labyrinthe;
    private Point end, start;

    public Labyrinthe(List<List<MazeTile>> board) {
        this.labyrinthe = new MazeTile[board.size()][board.get(0).size()];

        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(0).size(); j++){
                this.labyrinthe[i][j] = board.get(i).get(j);

                if(board.get(i).get(j) == MazeTile.Exit) {

                    if (start == null)
                        start = new Point(i, j);
                    else
                        end = new Point(i, j);
                }
            }
        }
    }


    public List<Point> findPath () {

        ArrayDeque<Point> queue = new ArrayDeque<>();

        queue.add(start);

        boolean[][] visited = new boolean[this.labyrinthe.length][this.labyrinthe[0].length];

        visited[start.x][start.y] = true;

        HashMap<Point, Point> prev = new HashMap<>();

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            List<Point> neighbours = getNeighbours(current);

            if(current == end)
                break;

            for (Point next : neighbours) {
                if (!visited[next.x][next.y] && !isWall(next)) {
                    queue.addLast(next);
                    visited[next.x][next.y] = true;

                    prev.put(next, current);
                }
            }
        }

        List<Point> path = new ArrayList<>();

        Point p = end;
        while (p != start && p != null) {

            path.add(p);
            p = prev.get(p);
        }

        return path;
    }

    public List<Point> getNeighbours(Point p) {
        List<Point> neighbours = new ArrayList<>();

        if (p.x > 0)
            neighbours.add(new Point(p.x-1, p.y));
        if (p.x+1 < this.labyrinthe.length)
            neighbours.add(new Point(p.x+1, p.y));
        if (p.y > 0)
            neighbours.add(new Point(p.x, p.y-1));
        if (p.y+1 < this.labyrinthe[0].length)
            neighbours.add(new Point(p.x, p.y+1));

        return neighbours;
    }

    public boolean isWall (Point p) { return this.labyrinthe[p.x][p.y] == MazeTile.Wall; }

    public boolean isExit (Point p) { return this.labyrinthe[p.x][p.y] == MazeTile.Exit; }



    @Override
    public String toString(){
        String src = "";
        for (int i = 0; i < labyrinthe.length; i++) {
            for (int j = 0; j < labyrinthe[0].length; j++){
                src += labyrinthe[i][j] + " ";
            }
            src += '\n';
        }
        return  src;
    }
}

