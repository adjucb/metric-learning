package acids2.multisim;

public abstract class MultiSimSimilarity {

	protected MultiSimProperty property;
	
	protected int index;

	private boolean computed = false;
	
	private double estimatedThreshold;
	
	public MultiSimSimilarity(MultiSimProperty property, int index) {
		this.property = property;
		this.index = index;
	}

	public MultiSimProperty getProperty() {
		return property;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean isComputed() {
		return computed ;
	}

	public void setComputed(boolean computed) {
		this.computed = computed;
	}

	public abstract MultiSimFilter getFilter();

	public abstract String getName();
	
	public abstract int getDatatype();
	
	public double getEstimatedThreshold() {
		return estimatedThreshold;
	}

	public void setEstimatedMeanValue(double estimatedThreshold) {
		this.estimatedThreshold = estimatedThreshold;
	}

	public abstract double getSimilarity(String a, String b);
	

}
