package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentList extends ArrayList<Fragment> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * read the given file and convert data into fragment 
	 * @param filename, the filepath to file to read
	 * @return the arrayList of the fragments readed in the file
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
	
	public static void writeToFile(Fragment f, String collectionNum, String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write("> Groupe-4 Collection "+collectionNum);
			writer.write(" Longueur "+Integer.toString(f.size()));
			writer.newLine();
			int i = 0;
			String result = f.toString();
			while ((i+1)*81<f.size()) {
				writer.write(result.substring(i*81, (i+1)*81));
				writer.newLine();
				i++;
			}
			writer.write(result.substring(i*81, f.size()));
			writer.close();
		} catch (IOException e) {
			System.out.println("Erreur dans le fichier");
			e.printStackTrace();
		}
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
