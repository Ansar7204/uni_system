package university.users;


public abstract class Employee extends User {

	public DepartmentsOfEmployees department;
	private int salary;

	public Employee(String id, String name, String email, String password, DepartmentsOfEmployees department, int salary){
		super(id, name, email, password);
		this.department = department;
		this.salary = salary;
	}


	public String viewRequests() {
		return "";
	}
	
}

