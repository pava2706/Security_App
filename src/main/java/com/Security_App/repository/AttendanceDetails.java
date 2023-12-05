package com.Security_App.repository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AttendanceDetails {
	Long getUserId();

	LocalDate getLoginDate();

	LocalTime getLoginTime();

	String getLoginSelfie();

	String getLoginLocation();

	LocalDate getLogoutDate();

	LocalTime getLogoutTime();

	String getLogoutSelfie();

	String getLogoutLocation();
	// Other getters for the selected fields
}