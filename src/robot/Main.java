package robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;


class Item{
	private String t;
	private int val;
	public Item(String _t, int _val){
		t = _t;
		val = _val;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public int getVal() {
		return val;
	}
	public void setVal(int val) {
		this.val = val;
	}
}



public class Main {
	final static String input = "F:\\in.txt";
	final static String outputHtml = "F:\\myRefs.txt";
	final static String output = "F:\\outMap.txt";
	final static String output1 = "F:\\outMapSorted.txt";
	final static String output2 = "F:\\outReduce.txt";
	final static String locationMap = "F:\\со c++\\map\\Debug\\map.exe";
	final static String locationReduce = "F:\\со c++\\reduce\\Debug\\reduce.exe";
	final static String locationRobot = "F:\\со c++\\ReadAHREF\\Debug\\ReadAHREF.exe";
	static int a;
	public static void main(String[] args) throws IOException {
		if (args.length>1){
			File[] refs = new File[args.length-1];
			for (int i=0; i<refs.length; i++){
				refs[i]=new File("F:\\"+i+".txt");
				PrintWriter printref = new PrintWriter(refs[i].getAbsoluteFile());
				printref.print(args[i]);
				printref.close();
			}
			a = Integer.parseInt(args[args.length-1]);
			int counter = 0;
			BufferedReader bf;
			ArrayList<String> q = new ArrayList<>();
			for (int i=0; i<refs.length; i++){
				q.add(args[i]);
				while (q.isEmpty() == false && counter<a){
					counter++;
					URL url = new URL(q.get(0));
					q.remove(0);
					File filei = new File("F:\\"+i+".txt");
					LineNumberReader reader = new LineNumberReader(new InputStreamReader(url.openStream()));
	                StringBuilder html = new StringBuilder(new String());
	               	PrintWriter pw = new PrintWriter(filei.getAbsoluteFile());
	               	pw.print(html.toString());
	                reader.close();
	                ProcessBuilder bd = new ProcessBuilder(locationRobot);
	    			bd.redirectInput(refs[i]);
	    			bd.redirectOutput(filei);
	    			Process process = bd.start();
	    			try {
	    				process.waitFor();
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			process.destroy();
	    			pw.close();
	    			BufferedReader br = new BufferedReader(new FileReader(filei.getAbsoluteFile()));
	    			PrintWriter pw1 = new PrintWriter(refs[i].getAbsoluteFile());
	    			String temp;
	    			while ((temp = br.readLine()) != null){
	    				pw1.println(temp);
	    				q.add(temp);
	    			}
	    			br.close();
	    			pw1.close();
	    			filei.delete();
				}
			}
			
			for (int i=0; i<refs.length; i++){
				
				ProcessBuilder bd = new ProcessBuilder(locationMap);
				bd.redirectInput(refs[i]);
				bd.redirectOutput(new File("F:\\"+i+".txt"));
		        Process process = bd.start();
		        try {
					process.waitFor();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        process.destroy();
		        ArrayList<Item> items = new ArrayList<>();
		        File file = new File("F:\\"+i+".txt");
		        try{
					BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
					String str[];
					String temp;
					while ((temp = reader.readLine()) != null) {
						str = temp.split("\t");
						items.add(new Item(str[0], Integer.parseInt(str[1])));
					}
					reader.close();
				} catch(IOException e) {e.printStackTrace(); }
				items.sort(new Comparator<Item>() {
					@Override
					public int compare(Item o1, Item o2) {
						return o1.getT().compareTo(o2.getT());
					}
				});
				File out = new File("F:\\toReduce"+i+".txt");
				try {
					PrintWriter writer = new PrintWriter(out.getAbsoluteFile());
					for (int j = 0; j < items.size(); j++) {
					    writer.println(items.get(i).getT() + "\t" + items.get(i).getVal());
					}
					writer.close();
				} catch (FileNotFoundException e) { }
				ProcessBuilder bd1 = new ProcessBuilder(locationReduce);
				bd1.redirectInput(out);
				bd1.redirectOutput(new File("F:\\"+refs[i].getName()+".txt"));
		        Process process1 = bd1.start();
		        try {
					process1.waitFor();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        process1.destroy();
			
			}
		}
    }

}
