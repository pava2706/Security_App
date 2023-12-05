package com.Security_App.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Security_App.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	@Query("SELECT a FROM Attendance a " + "WHERE a.user.id = :userId " + "AND MONTH(a.logindate) = :month "
			+ "AND YEAR(a.logindate) = :year ")
	List<Attendance> findAttendanceForUserByMonth(@Param("userId") Long userId, @Param("month") int month,
			@Param("year") int year);

	@Query("SELECT a.user.id as userId, " + "a.logindate as loginDate, " + "a.logintime as loginTime, "
			+ "a.loginselfie as loginSelfie, " + "a.loginlocation as loginLocation, " + "a.logoutdate as logoutDate, "
			+ "a.logouttime as logoutTime, " + "a.logoutselfie as logoutSelfie, "
			+ "a.logoutlocation as logoutLocation " + "FROM Attendance a " + "WHERE MONTH(a.logindate) = :month "
			+ "AND YEAR(a.logindate) = :year")
	List<AttendanceDetails> findAttendanceDetailsByMonthAndYear(@Param("month") int month, @Param("year") int year);

	Attendance findTopByUserIdOrderByLogindateDesc(Long userId);

	Attendance findTopByUserIdOrderByLogoutdateDesc(Long userId);

	List<Attendance> findAllByLogindate(LocalDate date);

	List<Attendance> findAllByUserIdAndLogindate(Long userId, LocalDate loginDate);

}
