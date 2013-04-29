/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acids2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author tom
 */
public class Couple implements Comparable<Couple> {
    
    private Resource source;
    private Resource target;
    
    private HashMap<Integer, Double> distances;
    
    private double gamma;
    
//    private int[][][] count;
    private ArrayList<Operation> ops;

    public static final int TP = 1;
    public static final int FP = 2;
    public static final int TN = 3;
    public static final int FN = 4;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    private boolean positive;
    
    public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}

	public void resetCount() {
//        for(int i=0; i<count.length; i++)
//            for(int j=0; j<count[i].length; j++)
//                for(int k=0; k<count[i][j].length; k++)
//                    count[i][j][k] = 0;
    	ops.clear();
    }
    
    public void count(int i, int j, int k) {
//        count[i][j][k] ++;
    	ops.add(new Operation(i, j, k));
    }
    
    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getMeanDist() {
        double sum = 0.0;
        for(Double sim : distances.values())
            sum += sim;
        return sum/distances.size();
    }

    public Resource getSource() {
        return source;
    }

    public Resource getTarget() {
        return target;
    }
    
	public ArrayList<Double> getDistances() {
		ArrayList<Integer> keys = new ArrayList<Integer>(distances.keySet());
		ArrayList<Double> values = new ArrayList<Double>();
		Collections.sort(keys);
		for(Integer k : keys)
			values.add(distances.get(k));
		return values;
	}

    public double getDistanceAt(int index) {
    	return distances.get(index);
    }
    
    public void setDistance(double d, int index) {
    	distances.put(index, d);
    }

    public Couple(Resource source, Resource target) {
        this.source = source;
        this.target = target;
        distances = new HashMap<Integer, Double>();
        ops = new ArrayList<Operation>();
    }

    public int[] getCountMatrixAsArray(int k) {
        int[] cArr = new int[4096];
        for(Operation op : ops) {
        	int n = op.getN();
        	if(k == n) {
	        	int arg1 = op.getArg1();
	        	int arg2 = op.getArg2();
	        	cArr[arg1*64+arg2]++;
        	}
        }        
//        int h = 0;
//        for(int i=0; i<count.length; i++)
//            for(int j=0; j<count[i].length; j++)
//                if(i != j) {
//                    cArr[h] = count[i][j][k];
//                    h++;
//                }
        return cArr;
    }
    
    public void info() {
		for(double d : this.getDistances())
			System.out.print(d+", ");
		System.out.println("\t"+this+"\t"+this.getGamma());
    }

    @Override
    public int compareTo(Couple c) {
    	// maybe we should replace '#' with another symbol...
        String c1 = this.getSource().getID()+"#"+this.getTarget().getID();
        String c2 = c.getSource().getID()+"#"+c.getTarget().getID();
        return c1.compareTo(c2);
    }
    
    @Override
    public String toString() {
		return this.getSource().getID()+"#"+this.getTarget().getID();
    }

    @Override
    public boolean equals(Object o) {
    	if(!(o instanceof Couple))
    		return false;
    	else {
    		Couple c = (Couple) o;
            String c1 = this.getSource().getID()+"#"+this.getTarget().getID();
            String c2 = c.getSource().getID()+"#"+c.getTarget().getID();
            return c1.equals(c2);
    	}
    }

	public double getFirstDistance() {
		int k_min = Integer.MAX_VALUE;
		for(int k : distances.keySet())
			if(k < k_min)
				k_min = k;
		return distances.get(k_min);
	}
}
