package acids2.test;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import acids2.MainAlgorithm;
import acids2.Resource;
import au.com.bytecode.opencsv.CSVReader;

public class Test {

	private static ArrayList<Resource> sources = new ArrayList<Resource>();
	private static ArrayList<Resource> targets = new ArrayList<Resource>();
	
    private static ArrayList<String> ignoredList = new ArrayList<String>();
    
    static {
    	ignoredList.add("id");
    	ignoredList.add("venue");
//    	ignoredList.add("description");
//    	ignoredList.add("price");
    }

    /**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
//		buildTinyDataset("data/1-dblp-acm/", "data/01-tiny/");
//		System.exit(0);
				
		String datasetPath = args[0];
//		String datasetPath = "1-dblp-acm";
//		String datasetPath = "2-dblp-scholar";
//		String datasetPath = "4-abt-buy";
		int k = Integer.parseInt(args[1]);
		double beta = Double.parseDouble(args[2]);
		int mip = Integer.parseInt(args[3]);
		
		String sourcePath = "data/" + datasetPath + "/sources.csv";
		String targetPath = "data/" + datasetPath + "/targets.csv";
		loadKnowledgeBases(sourcePath, targetPath);
		
		String mappingPath = "data/" + datasetPath + "/mapping.csv";
		loadMappings(mappingPath);
		
		System.out.println("dataset = "+datasetPath+"\tk = "+k+"\tbeta = "+beta);
		MainAlgorithm.start(sources, targets, k, beta, mip);
		
	}

    private static void loadKnowledgeBases(String sourcePath, String targetPath) throws IOException {
    	loadKnowledgeBases(sourcePath, targetPath, 0, Integer.MAX_VALUE);
    }

    private static void loadKnowledgeBases(String sourcePath, String targetPath, int startOffset, int endOffset) throws IOException {
    	
        CSVReader reader = new CSVReader(new FileReader(sourcePath));
        String [] titles = reader.readNext(); // gets the column titles
        for(int i=0; i<startOffset; i++) // skips start offset
        	reader.readNext();
        String [] nextLine;
        int count = 0;
        while ((nextLine = reader.readNext()) != null) {
            Resource r = new Resource(nextLine[0]);
            for(int i=0; i<nextLine.length; i++) {
                if(!ignoredList.contains( titles[i].toLowerCase() )) {
                    if(nextLine[i] != null)
                        r.setPropertyValue(titles[i], nextLine[i]);
                    else
                        r.setPropertyValue(titles[i], "");
                }
            }
            sources.add(r);
            if(++count >= endOffset)
            	break;
        }
        
        reader = new CSVReader(new FileReader(targetPath));
        titles = reader.readNext(); // gets the column titles
        for(int i=0; i<startOffset; i++) // skips offset
        	reader.readNext();
        count = 0;
        while ((nextLine = reader.readNext()) != null) {
            Resource r = new Resource(nextLine[0]);
            for(int i=0; i<nextLine.length; i++)
                if(!ignoredList.contains( titles[i].toLowerCase() )) {
                    if(nextLine[i] != null)
                        r.setPropertyValue(titles[i], nextLine[i]);
                    else
                        r.setPropertyValue(titles[i], "");
                }
            targets.add(r);
            if(++count >= endOffset)
            	break;
        }
        
        reader.close();
    }

	private static ArrayList<String> oraclesAnswers = new ArrayList<String>();
	
    public static ArrayList<String> getOraclesAnswers() {
		return oraclesAnswers;
	}

	private static void loadMappings(String mappingPath) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(mappingPath));
        reader.readNext(); // skips the column titles
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            oraclesAnswers.add(nextLine[0] + "#" + nextLine[1]);
        }
        reader.close();
    }
    
	public static boolean askOracle(String ids) {
		return oraclesAnswers.contains(ids);
	}
	
	@SuppressWarnings("unused")
	private static void buildTinyDataset(String originpath, String aimpath) throws IOException {
		int N = 200;
		loadMappings(originpath + "mapping.csv");
		
		HashMap<String, String> srcRows = new HashMap<String, String>();
		HashMap<String, String> tgtRows = new HashMap<String, String>();
		
        CSVReader reader = new CSVReader(new FileReader(originpath + "sources.csv"));
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
        	String line = nextLine[0];
            for(int i=1; i<nextLine.length; i++) 
            	line += "\",\"" + nextLine[i];
            line = "\"" + line + "\"";
            srcRows.put(nextLine[0], line);
        }
        reader.close();
        
        reader = new CSVReader(new FileReader(originpath + "targets.csv"));
        while ((nextLine = reader.readNext()) != null) {
        	String line = nextLine[0];
            for(int i=1; i<nextLine.length; i++) 
            	line += "\",\"" + nextLine[i];
            line = "\"" + line + "\"";
            tgtRows.put(nextLine[0], line);
        }
        reader.close();
		
        String output1 = "", output2 = "", output3 = "";
        TreeSet<Integer> a = new TreeSet<Integer>();
		for(int i=0; i<N; i++) {
			int j = (int) (Math.random() * oraclesAnswers.size());
			String mapping;
			if(!a.contains(j)) {
				mapping = oraclesAnswers.get(j);
				a.add(j);
			} else {
				i--;
				continue;
			}
			String[] maps = mapping.split("#");
			String s = maps[0], t = maps[1];
			output1 += srcRows.get(s) + "\n";
			output2 += tgtRows.get(t) + "\n";
			output3 += "\""+s+"\",\""+t+"\"\n"; 
		}
		
		FileWriter fstream = new FileWriter(aimpath + "sources.csv");
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(output1);
		out.close();

		fstream = new FileWriter(aimpath + "targets.csv");
		out = new BufferedWriter(fstream);
		out.write(output2);
		out.close();

		fstream = new FileWriter(aimpath + "mapping.csv");
		out = new BufferedWriter(fstream);
		out.write(output3);
		out.close();
	}
	

}
