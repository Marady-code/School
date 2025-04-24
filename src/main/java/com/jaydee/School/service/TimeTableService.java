package com.jaydee.School.service;

import java.time.DayOfWeek;
import java.util.List;

import com.jaydee.School.DTO.TimeTableDTO;
import com.jaydee.School.entity.ClassEntity;
import com.jaydee.School.entity.Teacher;

public interface TimeTableService {
    TimeTableDTO createTimeTable(TimeTableDTO timeTableDTO);
    TimeTableDTO updateTimeTable(Long id, TimeTableDTO timeTableDTO);
    List<TimeTableDTO> getClassTimeTable(ClassEntity classEntity);
    List<TimeTableDTO> getTeacherTimeTable(Teacher teacher);
    List<TimeTableDTO> getClassDayTimeTable(ClassEntity classEntity, DayOfWeek dayOfWeek);
    List<TimeTableDTO> getTimeTableBySubject(Long subjectId);
    List<TimeTableDTO> getTimeTableByAcademicYearAndTerm(String academicYear, String term);
    List<TimeTableDTO> createBulkTimeTable(List<TimeTableDTO> timeTableDTOs);
    void deleteTimeTable(Long id);
//    void validateTimeSlot(TimeTable timeTable);
//    boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2);
}
