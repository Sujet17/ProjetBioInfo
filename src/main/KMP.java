package main;

public class KMP {
	
	private KMP() {};
	
	public static int[] KMP_Search(String word, String text) {
		int M = word.length();
		int N = text.length();
		if (M > N)
			return null;
		else {
			int lps[] = computelpsArray(word, M);
			int j = 0;
			
			int i = 0; // index for txt[] 
	        while (i < N) { 
	            if (word.charAt(j) == text.charAt(i)) { 
	                j++; 
	                i++; 
	            } 
	            if (j == M) { 
	                System.out.println("Found pattern "
	                                   + "at index " + (i - j)); 
	                j = lps[j - 1]; 
	            } 
	  
	            // mismatch after j matches 
	            else if (i < N && word.charAt(j) != text.charAt(i)) { 
	                // Do not match lps[0..lps[j-1]] characters, 
	                // they will match anyway 
	                if (j != 0) 
	                    j = lps[j - 1]; 
	                else
	                    i = i + 1; 
	            }
	        }
	        int[] tab = {0};
	        return tab;
		}
	}
	
	/**
	 * 
	 * @param word
	 * @param M the length of the word param
	 * @return
	 */
	private static int[] computelpsArray(String word, int M) {
        // length of the previous longest prefix suffix 
        int len = 0; 
        int i = 1; 
        int[] lps = new int[M];
        lps[0] = 0; // lps[0] is always 0 
  
        // the loop calculates lps[i] for i = 1 to M-1 
        while (i < M) { 
            if (word.charAt(i) == word.charAt(len)) { 
                len++; 
                lps[i] = len; 
                i++; 
            } 
            else // (pat[i] != pat[len]) 
            { 
                // This is tricky. Consider the example. 
                // AAACAAAA and i = 7. The idea is similar 
                // to search step. 
                if (len != 0) { 
                    len = lps[len-1]; 
  
                    // Also, note that we do not increment 
                    // i here 
                } 
                else // if (len == 0) 
                { 
                    lps[i] = 0; 
                    i++; 
                } 
            } 
        }
        return lps;
	}
	
}
