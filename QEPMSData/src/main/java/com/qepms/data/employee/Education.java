package com.qepms.data.employee;
// default package
// Generated Mar 13, 2014 3:13:19 PM by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;

/**
 * Education generated by hbm2java
 */
public class Education implements java.io.Serializable {

	private Integer educationId;
	private Resume resume;
	private String degree;
	private BigDecimal aggregate;
	
	private String college;
	private Integer yearOfPassing;
	private Date createdDate;
	private Date updatedDate;
	private String specialization;
	

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public Education() {
	}

	public Education(Date createdDate, Date updatedDate) {
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public Education(Resume resume, String degree, BigDecimal aggregate,String college, Integer yearOfPassing,
			Date createdDate, Date updatedDate,String specialization ) {
		this.resume = resume;
		this.degree = degree;
		this.aggregate = aggregate;
		
		this.college = college;
		this.yearOfPassing = yearOfPassing;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.specialization = specialization;
	}

	public Integer getEducationId() {
		return this.educationId;
	}

	public void setEducationId(Integer educationId) {
		this.educationId = educationId;
	}

	public Resume getResume() {
		return this.resume;
	}

	public void setResume(Resume resume) {
		this.resume = resume;
	}

	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public BigDecimal getAggregate() {
		return this.aggregate;
	}

	public void setAggregate(BigDecimal aggregate) {
		this.aggregate = aggregate;
	}



	public String getCollege() {
		return this.college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public Integer getYearOfPassing() {
		return this.yearOfPassing;
	}

	public void setYearOfPassing(Integer yearOfPassing) {
		this.yearOfPassing = yearOfPassing;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}