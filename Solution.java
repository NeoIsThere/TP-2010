import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Solution {

    //HACKER RANK
    //Albert Camus @samuel_lavallee
    //@anthelme_clisson


    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean isBfs = scanner.nextLine().equals("1");
        List<List<Integer>> world = new ArrayList<>();
        String listString;
        while (!(listString = scanner.nextLine()).isEmpty()) {
            List<Integer> row = Arrays
                    .stream(listString.split(" +"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            world.add(row);
        }
        //System.out.println(world);
        World w = new World(world,isBfs);
        //System.out.println(world);

        w.Search();
        w.printSearchResults();


    }
}

    class World {

        public Node[][] world;
        public List<TreeSet<Integer>> continents;
        public int height, width, id;
        int[] directions = {1,-1};
        boolean isBfs;


        private class Node {
            Node(int p, boolean isV) {
                pays = p;
                isVisited = isV;


            }
            HashSet<Node> neighbors = new HashSet<>();
           // int continent =0;
            int pays;
            boolean isVisited;
        }

        World(List<List<Integer>> rawWorld, boolean isBfs) {
            continents = new ArrayList<TreeSet<Integer>>();
            this.isBfs = isBfs;
            height = rawWorld.size(); width = rawWorld.get(0).size(); id=0;
            world = new Node[height][width];
            for(int i=0; i<height; i++)
                for(int j=0; j<width; j++){
                    //System.out.println(i+" "+j);
                    world[i][j] = new Node(rawWorld.get(i).get(j), false);
                }
            setNeighbors();
        }

        void printNeighbors(){
            for(int i=0; i<height; i++)
                for(int j=0; j<width; j++){
                    System.out.print(world[i][j].pays+" voisins: ");
                    for(Node n : world[i][j].neighbors)
                        System.out.print(n.pays+" ");
                    System.out.println();
                }
        }

        void setNeighbors(){
            for(int i=0; i<height; i++)
                for(int j=0; j<width; j++){
                    if(world[i][j].pays!=0) {
                        for (int k : directions) {
                            if (i + k >= 0 && i + k < height)
                                if (world[i + k][j].pays != 0) {
                                    world[i][j].neighbors.add(world[i + k][j]);
                                }
                            if (j + k >= 0 && j + k < width)
                                if (world[i][j + k].pays != 0) {
                                    world[i][j].neighbors.add(world[i][j + k]);
                                }
                        }
                    }
                }
        }

        public void Search() {
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++) {
                    if (!world[i][j].isVisited && world[i][j].pays != 0) {
                        continents.add(new TreeSet<>());
                        if (isBfs)
                            breadthSearch(world[i][j]);
                        else
                            depthSearch(world[i][j]);
                        id++;
                    }
                }
        }
        public void printSearchResults(){

            for(int i=0; i< continents.size();i++) {
                    for(int j: continents.get(i))
                        System.out.print(j+" ");
                System.out.println(" ");
            }

        }



        public void breadthSearch(Node n) {
            Queue<Node> q = new ArrayDeque<>();
            n.isVisited=true;
            q.add(n);
            while(!q.isEmpty()){
                Node a = q.poll();
                for(Node neighbor : a.neighbors)
                    if(!neighbor.isVisited && neighbor.pays!=0){
                        neighbor.isVisited=true;
                        q.add(neighbor);
                    }
                continents.get(id).add(a.pays);
            }
        }

        public void depthSearch(Node n){
            n.isVisited=true;
            continents.get(id).add(n.pays);
            for(Node neighbor: n.neighbors)
                if(!neighbor.isVisited && neighbor.pays!=0)
                    depthSearch(neighbor);

        }
    }

