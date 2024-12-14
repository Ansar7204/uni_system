package university.users;


import university.communication.News;

import java.util.ArrayList;
import java.util.List;

public abstract class Employee extends User {

	public DepartmentsOfEmployees department;
	private int salary;

	public Employee(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary){
		super(id, name, surName, email, password);
		this.department = department;
		this.salary = salary;
	}


	public String viewRequests() {
		return "";
	}
	
}

