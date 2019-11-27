package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentList extends ArrayList<Fragment> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FragmentList() {
		super();
	}
	
	/**
	 * read file and convert data into fragment
	 * 
	 * @param filename, file to read
	 * @return the fragments from the filename
	 */
	public static FragmentList getFragmentsFromFile(String filename) {
		FragmentList fragments = new FragmentList();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			line = reader.readLine();
			while(line != null) {
				StringBuilder fragment = new StringBuilder();
				
				line = reader.readLine();

				while(line != null && line.charAt(0) != '>') {
					fragment.append(line);
					line = reader.readLine();
				}
				fragments.add(new Fragment(fragment.toString()));
				//System.out.println(fragment);
			}	
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Erreur dans le fichier");
			e.printStackTrace();
		}
		return fragments;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String tmp;
		for (int i=0; i<this.size(); i++) {
			tmp = "Fragment " + Integer.toString(i+1) + "\n";
			result.append(tmp);
			result.append(this.get(i));
			result.append("\n");
		}
		return result.toString();
	}
	
}
