package it.unical.scalab.parsoda.common;

public class Pair<T,V> {
	
	public T left;
	public V right;
	
	public Pair() {
	}
	
	public Pair(T left, V right) {
		this.left = left;
		this.right = right;
	}
	
	public T getLeft() {
		return left;
	}
	public void setLeft(T left) {
		this.left = left;
	}
	public V getRight() {
		return right;
	}
	public void setRight(V right) {
		this.right = right;
	}
	
	


}