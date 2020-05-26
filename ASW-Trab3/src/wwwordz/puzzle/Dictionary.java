package wwwordz.puzzle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;

import wwwordz.puzzle.Trie;
import wwwordz.puzzle.Trie.Search;

/**
 * Organized collection of words, optimized for searching them.
 * 
 * This class is a singleton, there is at most, a single instance of this class
 * 	per algorithm
 * This dictionary uses a collection of Portuguese words loaded as a resource from
 * 	a file in this package. It is backed by a Trie to index words and speedup searches
 */
public class Dictionary {
	private static final String DICT_FILE = "wwwordz/puzzle/pt-PT-AO.dic";
	private static final String PAT = "[A-Z]+";
	private static final Dictionary dictionary = new Dictionary();
	private Trie trie = new Trie();
	
	
	private Dictionary() {
		try {
			InputStream stream = ClassLoader.getSystemResourceAsStream(DICT_FILE);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"UTF-8"));
			
			String line;
			while((line = reader.readLine()) != null) {
				if(Character.isAlphabetic(line.charAt(0))) {
					String word = line.split("/|\\s+")[0];
					//Ignore words with less than 3 chars
					if(word.length() < 3) continue;
					
					//Ignore accent marks
					word = Normalizer.normalize(word.toUpperCase(Locale.ENGLISH),Form.NFD).
		            replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
					
					if(!isAlpha(word)) continue;
					trie.put(word);
				}
			}
			reader.close();
		}catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Check if word is alpha based on regular expression
	 * @param String word
	 * @return boolead
	 */
	private boolean isAlpha(String word) {
	    return word.matches(PAT);
	}
	
	/**
	 * Obtain the sole instance of this class. Multiple invocations will receive
	 * 	the exact same instance.
	 * @return singleton
	 */
	static Dictionary getInstance() {
		return dictionary;
	}
	
	/**
	 * Return a large word from the trie
	 * @return String large word
	 */
	String getRandomLargeWord() {
		return trie.getRandomLargeWord();
	}
	
	/**
	 * Start a dictionary search
	 * @return Search
	 */
	Search startSearch() {
		return trie.startSearch();
	}
}
