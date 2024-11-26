package com.MyBackEnd.models;

import jakarta.persistence.*;
@Entity
@Table(name = "task_assignment")
public class TaskAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "role")
    private final String role="Contributor";
}
