package labyrinth_search;

import losev.lib.ArrayQueue;
import losev.lib.ArrayStack;

public class PathFinder{

	private boolean[] marked;
	private int[] edgeTo;
	private int[] distTo;
	private final int s;
	
	public PathFinder(Graph G, int s, boolean bfs) {
		this.s = s;
		edgeTo = new int[G.V()];
		distTo = new int[G.V()];
		marked = new boolean[G.V()];
		if(bfs) bfs(G, s);
		else dfs(G, s);
	}

	 /**
     * пошук в глибину
     * @param G - граф
     * @param v - dfs з вершини v
     */
    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }
    
    /**
     * пошук в ширину
     * @param G - граф
     * @param v - bfs з вершини v
     */
	private void bfs(Graph G, int s) {
		ArrayQueue<Integer> q = new ArrayQueue<Integer>();
		q.enqueue(s);
		marked[s] = true;
		distTo[s] = 0;
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					q.enqueue(w);
					marked[w] = true;
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
				}
			}
		}
	}

	 /**
     * Чи присутній шлях з v в s, що задана конструктором
     * @param v - вершина до чкої шукаємо шлях
     * @return true якщо є шлях, false якщо немає
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }
    
    /**
     * повертає шлях між s та v; null якщо шляху немає
     */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        ArrayStack<Integer> path = new ArrayStack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    } 
	
    public int getVisited(){
    	int result = 0;
    	for(int i=0; i<marked.length; i++)
    		if(marked[i])
    			result++;
    	return result;
    }
    
}
