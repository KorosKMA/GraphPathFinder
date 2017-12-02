package losev.lib;

import java.util.Iterator;

public class ArrayQueue<Item> implements Iterable<Item>{

	protected Item[] q;
	protected int n =0, start =0, end =-1;
	
	public ArrayQueue(){
		q = (Item[])new Object[1];
	}
	
	public void enqueue(Item item) {
		if (n==q.length) resize(2*q.length);
		if(++end>=q.length)
			end-=q.length;
		q[end] = item;
		n++;
	}

	
	public Item dequeue() {
		Item item = q[start];
		q[start]=null;
		if(++start>=q.length)
			start-=q.length;
		n--;
		if (n>0&&n==q.length/4) resize(q.length/2);
		return item;
	}

	
	public boolean isEmpty() {
		return n==0;
	}

	
	public int size() {
		return n;
	}
	
	private void resize(int capacity){
		Item[] copy = (Item[])new Object[capacity];
		if(start<=end)
			for (int i=start;i<=end;i++)
				copy[i-start]=q[i];
		else{
			for (int i=start;i<q.length;i++)
				copy[i-start]=q[i];
			for (int i=0;i<=end;i++)
				copy[i+q.length-start]=q[i];
		}
		q = copy;
		start=0;
		end=n-1;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ArrayIterator();
	}
	
	private class ArrayIterator implements Iterator<Item>{

		private int i=start;
		@Override
		public boolean hasNext() {
			if(start<=end)
				return i<end;
			else 
				if(i>=start)
					return true;
				else
					return i<end;
		}

		@Override
		public Item next() {
			if(start<=end)
				return q[i++];
			else 
				if(i<q.length)
					return q[i++];
				else
					return q[0];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
}