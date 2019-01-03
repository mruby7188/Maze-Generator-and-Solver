package mazes.generators.maze;

import datastructures.interfaces.ISet;
import datastructures.concrete.ChainedHashSet;
import mazes.entities.Maze;
import mazes.entities.Wall;
import mazes.entities.Room;
import java.util.Random;
import misc.graphs.Graph;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.

        Maze orig = maze;
        Random rand = new Random();
        
        ISet<Wall> walls = new ChainedHashSet<Wall>();
        
        for (Wall wall : maze.getWalls()) {
            Wall temp = wall;
            temp.setDistance(rand.nextDouble());
            walls.add(temp);
        }
        Graph<Room, Wall> graph = new Graph<Room, Wall>(maze.getRooms(), walls);

        return graph.findMinimumSpanningTree();
    }
}
