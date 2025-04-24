package com.jaydee.School.service.impl;

import com.jaydee.School.DTO.TimeTableDTO;
import com.jaydee.School.entity.ClassEntity;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.entity.TimeTable;
import com.jaydee.School.mapper.TimeTableMapper;
import com.jaydee.School.repository.TimeTableRepository;
import com.jaydee.School.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeTableServiceImpl implements TimeTableService {
    
    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private TimeTableMapper timeTableMapper;

    @Override
    @Transactional
    public TimeTableDTO createTimeTable(TimeTableDTO timeTableDTO) {
        TimeTable timeTable = timeTableMapper.toEntity(timeTableDTO);
        validateTimeSlot(timeTable);
        TimeTable savedTimeTable = timeTableRepository.save(timeTable);
        return timeTableMapper.toDTO(savedTimeTable);
    }

    @Override
    @Transactional
    public TimeTableDTO updateTimeTable(Long id, TimeTableDTO timeTableDTO) {
        TimeTable existingTimeTable = timeTableRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Time table not found"));
        
        TimeTable updatedTimeTable = timeTableMapper.toEntity(timeTableDTO);
        validateTimeSlot(updatedTimeTable);
        
        existingTimeTable.setStartTime(updatedTimeTable.getStartTime());
        existingTimeTable.setEndTime(updatedTimeTable.getEndTime());
        existingTimeTable.setRoomNumber(updatedTimeTable.getRoomNumber());
        existingTimeTable.setDayOfWeek(updatedTimeTable.getDayOfWeek());
        existingTimeTable.setAcademicYear(updatedTimeTable.getAcademicYear());
        existingTimeTable.setTerm(updatedTimeTable.getTerm());
        
        TimeTable savedTimeTable = timeTableRepository.save(existingTimeTable);
        return timeTableMapper.toDTO(savedTimeTable);
    }

    @Override
    public List<TimeTableDTO> getClassTimeTable(ClassEntity classEntity) {
        List<TimeTable> timeTables = timeTableRepository.findByClassEntity(classEntity);
        return timeTableMapper.toDTOList(timeTables);
    }

    @Override
    public List<TimeTableDTO> getTeacherTimeTable(Teacher teacher) {
        List<TimeTable> timeTables = timeTableRepository.findByTeacher(teacher);
        return timeTableMapper.toDTOList(timeTables);
    }

    @Override
    public List<TimeTableDTO> getClassDayTimeTable(ClassEntity classEntity, DayOfWeek dayOfWeek) {
        List<TimeTable> timeTables = timeTableRepository.findByClassEntityAndDayOfWeek(classEntity, dayOfWeek);
        return timeTableMapper.toDTOList(timeTables);
    }

    @Override
    public List<TimeTableDTO> getTimeTableBySubject(Long subjectId) {
        List<TimeTable> timeTables = timeTableRepository.findBySubjectId(subjectId);
        return timeTableMapper.toDTOList(timeTables);
    }

    @Override
    public List<TimeTableDTO> getTimeTableByAcademicYearAndTerm(String academicYear, String term) {
        List<TimeTable> timeTables = timeTableRepository.findByAcademicYearAndTerm(academicYear, term);
        return timeTableMapper.toDTOList(timeTables);
    }

    @Override
    @Transactional
    public List<TimeTableDTO> createBulkTimeTable(List<TimeTableDTO> timeTableDTOs) {
        List<TimeTable> timeTables = timeTableDTOs.stream()
            .map(timeTableMapper::toEntity)
            .collect(Collectors.toList());
        
        timeTables.forEach(this::validateTimeSlot);
        List<TimeTable> savedTimeTables = timeTableRepository.saveAll(timeTables);
        return timeTableMapper.toDTOList(savedTimeTables);
    }

    @Override
    @Transactional
    public void deleteTimeTable(Long id) {
        timeTableRepository.deleteById(id);
    }

    private void validateTimeSlot(TimeTable timeTable) {
        // Check if start time is before end time
        if (timeTable.getStartTime().isAfter(timeTable.getEndTime())) {
            throw new RuntimeException("Start time must be before end time");
        }

        // Check for overlapping time slots for the same class
        List<TimeTable> existingTimeTables = timeTableRepository.findByClassEntityAndDayOfWeek(
            timeTable.getClassEntity(), timeTable.getDayOfWeek());
        
        for (TimeTable existing : existingTimeTables) {
            if (existing.getId().equals(timeTable.getId())) continue;
            
            if (isTimeOverlap(timeTable.getStartTime(), timeTable.getEndTime(),
                    existing.getStartTime(), existing.getEndTime())) {
                throw new RuntimeException("Time slot overlaps with existing schedule");
            }
        }

        // Check for teacher availability
        List<TimeTable> teacherTimeTables = timeTableRepository.findByTeacherAndDayOfWeek(
            timeTable.getTeacher(), timeTable.getDayOfWeek());
        
        for (TimeTable existing : teacherTimeTables) {
            if (existing.getId().equals(timeTable.getId())) continue;
            
            if (isTimeOverlap(timeTable.getStartTime(), timeTable.getEndTime(),
                    existing.getStartTime(), existing.getEndTime())) {
                throw new RuntimeException("Teacher is not available during this time slot");
            }
        }
    }

    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }
} 