package com.eduportal.service;

import java.util.Map;

public interface StatisticsService {
    Map<String, Object> getInstructorStatistics(Long instructorId);
    Map<String, Object> getCourseStatistics(Long courseId);
    Map<String, Object> getStudentStatistics(Long studentId);
    Map<String, Object> getGeneralStatistics();
}