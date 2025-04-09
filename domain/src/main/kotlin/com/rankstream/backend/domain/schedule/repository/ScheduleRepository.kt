package com.rankstream.backend.domain.schedule.repository

import com.rankstream.backend.domain.schedule.entity.Schedule
import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleRepository : JpaRepository<Schedule, Long> 