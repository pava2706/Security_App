package com.Security_App.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Security_App.dto.AttendanceResponseDto;
import com.Security_App.dto.CommonApiResponse;
import com.Security_App.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

	@Autowired
	AttendanceService attendanceService;

	@PostMapping("/login/{userid}")
	public ResponseEntity<CommonApiResponse> login(@PathVariable("userid") Long userId,
			@RequestParam("loginlocation") String loginlocation,
			@RequestParam("loginselfie") MultipartFile loginselfie) {

		return attendanceService.login(userId, loginlocation, loginselfie);

	}

	@PostMapping("/logout/{userid}")
	public ResponseEntity<CommonApiResponse> logOut(@PathVariable("userid") Long userId,
			@RequestParam("logoutlocation") String logoutlocation,
			@RequestParam("logoutselfie") MultipartFile logoutselfie) {

		return attendanceService.logOut(userId, logoutlocation, logoutselfie);
	}

	// Method to get Monthly Details of Particular user by passing userId,year and
	// month

	@GetMapping("get/monthlydetails")

	public ResponseEntity<AttendanceResponseDto> getAttendanceForUserByMonth(@RequestParam("userid") Long userId,
			@RequestParam("month") int month, @RequestParam("year") int year) {
		return attendanceService.findAttendanceForUserByMonth(userId, month, year);
	}

	// Method to get Monthly Attendance Details of All Users

	@GetMapping("get/monthlydetails/all")

	public ResponseEntity<AttendanceResponseDto> getMonthlyAttendanceForAllUsers(@RequestParam("month") int month,
			@RequestParam("year") int year) {
		return attendanceService.getMonthlyAttendanceForAllUsers(month, year);
	}

	// Method to get the Attendance Details All the user of Particular Date
	// Format to pass Date[YYYY-MM-DD]

	@GetMapping("get/dailydetails/all")
	public ResponseEntity<AttendanceResponseDto> getAttendanceForDate(@RequestParam("date") String dateString) {

		LocalDate date = LocalDate.parse(dateString);
		return attendanceService.getAllAttendanceForDate(date);
	}

	// Method to get the Attendance Details of Particular user of Particular Date
	// Format to pass Date[YYYY-MM-DD]

	@GetMapping("get/dailydetails")
	public ResponseEntity<AttendanceResponseDto> getAttendanceForUserOnDate(@RequestParam("userId") Long userId,
			@RequestParam("date") String date) {
			LocalDate loginDate = LocalDate.parse(date);
			return attendanceService.getAttendanceForUserOnDate(userId, loginDate);
}
}