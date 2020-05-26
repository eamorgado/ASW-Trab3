package wwwordz.puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import wwwordz.shared.WWWordzException;

/**
 * Class representing a Trie, a data structure to store words efficiently using a tree.
 * 
 * Each tree node represents a prefix that may be a complete word, the descendants of a node
 * 	are indexed by a letter, representing a possible continuation of that prefix followed by 
 * 	that letter
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 */
public class Trie implements Iterable<String>{
	private Node root;
	
	/**
	 * Class representing a node in the Trie, each node represents a prefix/letter
	 * 
	 * The class extends HashMap<Character,Node> in order to use super methods
	 * 	such as get containsKey and so on
	 * 
	 * A node does not contain a string with the words in a dictionary, they serve 
	 * 	only as branches that form words, containing the descendant nodes for each 
	 * 	existing prefix/letter however, since we use nodes to form words, they 
	 * 	have a flag attribute to mark the completion of a word.
	 */
	private class Node extends HashMap<Character,Node>{		
		private static final long serialVersionUID = 1L;
		private boolean is_word = false;
		private Random rand = new Random();
				
		boolean isWord() {return this.is_word;}
		
		
		/**
		 * Method that recursively builds a Trie section
		 * 
		 * In each recursion step:
		 * 	-> Check if prefix exists in node
		 * 
		 * 	-> If prefix exists, get the node for the corresponding prefix, proceed insertion
		 * 		for next letter in word
		 * 
		 * 	-> If prefix does not exist, generate new child node to current, proceed insertion
		 * 		for remaining word applied to the new node
		 * 	
		 * 	-> Base case: Stop recursion when index is bigger than provided word
		 * 
		 * @param String word - word used to build Trie section
		 * @param int index - specifies the position of the prefix in the word, if root index = 0
		 */
		void put(String word, int index) {
			//Base
			if(index == word.length()) {
				is_word = true;
				return;
			}
			
			char prefix = word.charAt(index);
			Node child;
			
			if(containsKey(prefix)) child = get(prefix);
			else put(prefix,child = new Node());
			child.put(word,++index);
		}	
		
		/**
		 * Method that performs a random walk in the data structure 
		 * 	build a word with the use of a StringBuilder
		 * 
		 * Stop when reaching leaf (node with no descendants, size of map is 0)
		 * @param StringBuilder sb
		 */
		void getRandomLargeWord(StringBuilder sb){
			if(size() == 0) return;
			ArrayList<Character> letters = new ArrayList<Character>(keySet());
			char letter = letters.get(rand.nextInt(size()));
			sb.append(letter);
			get(letter).getRandomLargeWord(sb);
		}
	}
	
	/**
	 * Iterator over strings stored in the internal node structure.
	 * 
	 * Traverses the node tree depth first, using coroutine with threads,
	 * 	collects all possible words in no particular order
	 */
	class NodeIterator extends Object implements Iterator<String>, Runnable{
		private String nextWord;
		private boolean terminated;
		private Thread thread;
		
		NodeIterator(){
			thread = new Thread(this,"Node iterator");
			thread.start();
		}
		
		@Override
		public void run() {
			terminated = false;
			visitNodes(root,"");			
			synchronized(this){
				terminated = true;
				try {
					handshake();
				} catch (WWWordzException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		/**
		 * Recursively descend trie, searching words
		 * 	Stop when found a word (isWord() true)
		 * @param Node node
		 * @param String word built so far
		 */
		private void visitNodes(Node node,String word) {
			for(char letter : node.keySet()) {
				visitNodes(node.get(letter),word + letter);
				synchronized(this) {
					if(nextWord != null) {
						try {
							handshake();
						} catch (WWWordzException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(node.get(letter).isWord())
						nextWord = word + letter;
				}
			}
		}

		@Override
		public boolean hasNext() {
			synchronized(this) {
				if(!terminated)
					try {
						handshake();
					} catch (WWWordzException e) {
						e.printStackTrace();
					}
			}
			return nextWord != null;
		}

		@Override
		public String next() {
			String value = nextWord;
			synchronized(this) {
				nextWord = null;
			}
			return value;
		}
		
		private void handshake() throws WWWordzException {
			notify();
			try {
				wait();
			}catch(InterruptedException cause) {
				throw new WWWordzException("Unexpected interruption while waiting",cause);
			}
		}
		 
	}
	
	/**
	 * Position in the node structure when looking for a word
	 * 
	 * A word can be searched giving successive words
	 */
	public static class Search{
		private Node node;
		
		/** 
		 * Creates a search starting in given node
		 */
		Search(Node node){
			this.node = node;
		}
		
		/**
		 * Creates a clone of the given search with the same fields
		 */
		Search(Search search){
			this.node = search.node;
		}
		
		/**
		 * Method to check if we can continue the search with the current letter
		 * @param char letter
		 * @return boolean
		 */
		boolean continueWith(char letter) {
			if(node == null)return false;
			node = node.containsKey(letter)? node.get(letter) : null;
			return node != null;
		}
		
		/** 
		 * Method to check if characters searched so far correspond to a word
		 * @return boolean
		 */
		boolean isWord() {
			return node != null && node.isWord();
		}
	}
	
	
	Trie(){
		root = new Node();
	}
	
	/**
	 * Perform a random walk in the data structure randomly selecting path in each node
	 * 	until reaching a leaf
	 * @return String of random large word
	 */
	String getRandomLargeWord() {
		StringBuilder sb = new StringBuilder();
		root.getRandomLargeWord(sb);
		return sb.toString();		
	}
	
	/**
	 * Inserts the word in the structure, calling Node put method to
	 * 	recursively insert the word in the structure
	 * @param String word
	 */
	void put(String word) {
		root.put(word,0);
	}
	
	/**
	 * Method that starts a word search from the root
	 * @return
	 */
	Search startSearch() {
		return new Search(root);		
	}

	@Override
	public Iterator<String> iterator() {
		return new NodeIterator();
	}
}
