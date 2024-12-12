package university.courses;

public enum Marks {
	A(4.0), B(3.0), C(2.0), D(1.0), F(0.0);
	private final double gpaValue;

	Marks(double gpaValue) {
		this.gpaValue = gpaValue;
	}

	public double getGpaValue() {
		return gpaValue;
	}
}
