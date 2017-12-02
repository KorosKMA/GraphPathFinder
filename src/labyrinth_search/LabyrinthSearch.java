package labyrinth_search;

import java.awt.event.KeyEvent;
import java.util.Random;

import losev.lib.Bag;
import princeton.lib.StdDraw;

public class LabyrinthSearch {

	private static final double tau = 2 * Math.PI;

	private static final Random rnd = new Random();
	private static final int V = rnd.nextInt(10) + 3;
	private static final int delay = 500;
	private static final Graph lab = newLabyrinth();
	private static double[] x = new double[V];
	private static double[] y = new double[V];

	private static Graph newLabyrinth() {
		Graph lab = new Graph(V);
		int E = rnd.nextInt((V * (V - 1)) / 2);
		int v, w;
		for (int i = 0; i <= E; i++) {
			v = rnd.nextInt(V);
			w = rnd.nextInt(V);
			while (v == w)
				w = rnd.nextInt(V);
			add(lab, v, w, E);
		}
		return lab;
	}

	private static void add(Graph lab, int v, int w, int exitCount) {
		if (exitCount == 0)
			return;
		for (int x : lab.adj(v))
			if (w == x) {
				add(lab, v, rnd.nextInt(V), exitCount - 1);
				return;
			}
		lab.addEdge(v, w);
	}

	private static void initGraph(Graph lab, int start, int goal) {
		double angle;
		if (V > 1)
			angle = tau / (V - 1);
		else
			angle = 0;
		short wentThroughStart = 0;
		for (int i = 0; i < V; i++)
			if (i == start) {
				x[i] = 0.125;
				y[i] = 0.5;
				wentThroughStart++;
			} else {
				x[i] = 0.5 + Math.cos(angle * (i - wentThroughStart)) / 4;
				y[i] = 0.5 + Math.sin(angle * (i - wentThroughStart)) / 3;
			}
		drawGraph(start, goal);
	}

	@SuppressWarnings("unchecked")
	private static void drawGraph(int start, int goal) {
		int i;
		StdDraw.clear();
		StdDraw.setPenRadius();
		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		i = 0;
		for (Bag<Integer> v : lab) {
			for (Integer j : v)
				if (x[i] >= x[j])
					connect(i, j);
			i++;
		}
		StdDraw.setPenRadius(0.02);
		for (i = 0; i < V; i++)
			if (i == start) {
				StdDraw.setPenColor(StdDraw.GREEN);
				StdDraw.point(x[i], y[i]);
				StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			} else if (i == goal) {
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.point(x[i], y[i]);
				StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			} else
				StdDraw.point(x[i], y[i]);
		StdDraw.setPenRadius();
	}

	private static void connect(int i, int j) {
		if (Math.abs(y[i] - 0.5) < 0.0001 && Math.abs(y[i] - y[j]) < 0.0001) {
			double xMidDistance = (x[i] - x[j]) / 2;
			double radius = Math.sqrt(xMidDistance * xMidDistance + 0.04);
			double angle = Math.acos(xMidDistance / radius) * 360 / tau;
			StdDraw.arc((x[i] + x[j]) / 2, 0.3, radius, angle, 180 - angle);
		} else
			StdDraw.line(x[i], y[i], x[j], y[j]);
	}

	private static void drawPath(PathFinder pathFinder, int start, int goal, String name) {
		int numInSequence = 0;
		int current = start;
		StdDraw.setPenColor();
		StdDraw.text(0.795, 0.14, name);
		StdDraw.text(0.857, 0.09, "V's visited: " + pathFinder.getVisited());
		StdDraw.text(0.8625, 0.04, "Path length: 0");
		Iterable<Integer> it = pathFinder.pathTo(goal);
		if (it != null)
			for (int i : it) {
				if (x[i] < x[current])
					connect(current, i);
				else
					connect(i, current);
				StdDraw.setPenColor(StdDraw.WHITE);
				StdDraw.filledRectangle(0.98, 0.04, 0.04, 0.02);
				StdDraw.setPenColor();
				StdDraw.text(0.96, 0.04, (numInSequence++) + "");
				StdDraw.setPenColor(StdDraw.YELLOW);
				StdDraw.setPenRadius(0.02);
				StdDraw.point(x[i], y[i]);
				StdDraw.show(delay);
				current = i;
				if (current == start)
					StdDraw.setPenColor(StdDraw.GREEN);
				else
					StdDraw.setPenColor();
				StdDraw.point(x[current], y[current]);
				StdDraw.setPenColor();
				StdDraw.setPenRadius();
			}
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.setPenRadius(0.02);
		StdDraw.point(x[goal], y[goal]);
		StdDraw.setPenColor();
		StdDraw.setPenRadius();
		StdDraw.show(0);
	}

	private static boolean showButtons() {
		StdDraw.setPenColor();
		StdDraw.setPenRadius();
		StdDraw.text(0.18, 0.02, "Next goal (DFS): press 'B' key");
		StdDraw.text(0.18, 0.06, "Next goal (BFS): press 'D' key");
		StdDraw.show(0);
		short choice = -1;
		while (choice < 0)
			choice = choiceMade();
		return choice == 1;
	}

	private static short choiceMade() {
		if (StdDraw.isKeyPressed(KeyEvent.VK_B))
			return 1;
		if (StdDraw.isKeyPressed(KeyEvent.VK_D))
			return 0;
		return -1;
	}

	public static void main(String[] args) {
		StdDraw.setXscale(0, 1);
		StdDraw.setYscale(0, 1);
		int start = rnd.nextInt(V);
		int goal = rnd.nextInt(V);
		while (goal == start)
			goal = rnd.nextInt(V);
		initGraph(lab, start, start);
		boolean choice;
		PathFinder bfs = new PathFinder(lab, start, true);
		PathFinder dfs = new PathFinder(lab, start, false);
		int previousGoal = goal;
		while (true) {
			previousGoal = goal;
			choice = showButtons();
			if (choice){
				drawGraph(start, goal);
				drawPath(bfs, start, goal, "BFS");
			}else{
				drawGraph(start, goal);
				drawPath(dfs, start, goal, "DFS");
			}
			goal = rnd.nextInt(V);
			while (goal == previousGoal || goal == start)
				goal = rnd.nextInt(V);
		}
	}
}
