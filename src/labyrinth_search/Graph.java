package labyrinth_search;

import java.util.Iterator;

import losev.lib.Bag;

public class Graph implements Iterable<Bag> {

	private final int V;
	private int E;
	private Bag<Integer>[] adj;

	/**
	 * ��������� ������� ���� ��������� V ���������� ����� ��������
	 * �������� ���� Bag
	 * 
	 * @param V
	 *            - ������� ������
	 */
	public Graph(int V) {
		this.V = V;
		adj = (Bag<Integer>[]) new Bag[V];
		for (int v = 0; v < V; v++)
			adj[v] = new Bag<Integer>();
	}

	/**
	 * ������ ����� �� ����� ���������
	 * 
	 * @param v
	 *            - �������
	 * @param w
	 *            - �������
	 */
	public void addEdge(int v, int w) {
		adj[v].add(w);
		adj[w].add(v);
		E++;
	}

	/**
	 * @param v
	 *            - ������� �����
	 * @return - ������ ������� ����� v
	 */
	public int degree(int v) {
		int degree = 0;
		for (int w : adj(v))
			degree++;
		return degree;
	}

	/**
	 * �������� �� �������� ������� � �������� v
	 * 
	 * @param v
	 *            - �������
	 * @return - �������� �� ���� ������� � v ������
	 */
	public Iterable<Integer> adj(int v) {
		return adj[v];
	}

	/**
	 * @return - ������� ������
	 */
	public int V() {
		return V;
	}

	/**
	 * @return - ������� �����
	 */
	public int E() {
		return E;
	}

	@Override
	public Iterator<Bag> iterator() {
		return new VerticiesIterator();
	}

	private class VerticiesIterator implements Iterator<Bag> {
		private int current = 0;

		@Override
		public boolean hasNext() {
			return current < V;
		}

		@Override
		public Bag next() {
			return adj[current++];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
