package org.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Student {
    private final String name;
    private final String group;

    private Map<String, Double> grades = new HashMap<>();

    public Student(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public void addGrade(String examName, double score) {
        this.grades.put(examName, score);
    }

    public String getName() { return name; }
    public String getGroup() { return group; }
    public Map<String, Double> getGrades() { return Collections.unmodifiableMap(grades); }
}
