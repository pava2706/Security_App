package com.Security_App.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Security_App.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	@Query("SELECT a FROM Attendance a " + "WHERE a.user.id = :userId " + "AND YEAR(a.logindate) = :year "
			+ "AND MONTH(a.logindate) = :month")
	List<Attendance> findAttendanceForUserByMonth(@Param("userId") Long userId, @Param("year") int year,
			@Param("month") int month);

	Attendance findTopByUserIdOrderByLogindateDesc(Long userId);

	Attendance findTopByUserIdOrderByLogoutdateDesc(Long userId);

}
