package com.Security_App.controller;

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

	// Method to get Monthly Details of Perticular user by passing userId,year and
	// month
	// Ex:-(1/2023/12)

	@GetMapping("get/monthlydetails/{userid}/{year}/{month}")

	public ResponseEntity<AttendanceResponseDto> getAttendanceForUserByMonth(@PathVariable("userid") Long userId,
			@PathVariable("year") int year, @PathVariable("month") int month) {
		return attendanceService.findAttendanceForUserByMonth(userId, year, month);
	}
}
