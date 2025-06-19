package com.jaydee.School.DTO;

import java.util.List;

import lombok.Data;

@Data
public class ClassDTO {
	
    private Long id;
    private String className;
    private Long teacherId;
    private List<Long> studentIds;
    private String academicYear;
    private String term;
    private Integer capacity;
    private String roomNumber;
    private String description;
    private String schedule;
    private String startDate;
    private String endDate;
}
