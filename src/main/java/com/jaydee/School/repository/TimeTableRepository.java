package com.jaydee.School.repository;

import com.jaydee.School.entity.ClassEntity;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    List<TimeTable> findByClassEntity(ClassEntity classEntity);
    List<TimeTable> findByTeacher(Teacher teacher);
    List<TimeTable> findByClassEntityAndDayOfWeek(ClassEntity classEntity, DayOfWeek dayOfWeek);
    List<TimeTable> findByTeacherAndDayOfWeek(Teacher teacher, DayOfWeek dayOfWeek);
    List<TimeTable> findBySubjectId(Long subjectId);
    List<TimeTable> findByAcademicYearAndTerm(String academicYear, String term);
    List<TimeTable> findByClassEntityAndAcademicYearAndTerm(ClassEntity classEntity, String academicYear, String term);
    List<TimeTable> findByTeacherAndAcademicYearAndTerm(Teacher teacher, String academicYear, String term);
} 