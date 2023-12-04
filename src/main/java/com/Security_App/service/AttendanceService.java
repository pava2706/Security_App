package com.Security_App.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Security_App.dto.AttendanceResponseDto;
import com.Security_App.dto.CommonApiResponse;
import com.Security_App.entity.Attendance;
import com.Security_App.entity.User;
import com.Security_App.repository.AttendanceRepository;
import com.Security_App.repository.UserRepository;
import com.Security_App.utils.Constants.UserStatus;

@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StorageService storageService;

	public ResponseEntity<CommonApiResponse> login(Long userId, String loginlocation, MultipartFile loginselfie) {
		CommonApiResponse response = new CommonApiResponse();

		try {
			User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE.value());
			if (user == null) {
				response.setResponseMessage("No User Found");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			Attendance ate = attendanceRepository.findTopByUserIdOrderByLogindateDesc(userId);
			if (!ate.getLogindate().equals(LocalDate.now())) {
				Attendance attendance = new Attendance();

				attendance.setLogindate(LocalDate.now());
				attendance.setLogintime(LocalTime.now());
				attendance.setLoginlocation(loginlocation);
				String selfie = storageService.store(loginselfie);
				attendance.setLoginselfie(selfie);
				attendance.setUser(user);

				Attendance data = attendanceRepository.save(attendance);

				if (data == null) {
					response.setResponseMessage("Failed to add login Attendance");
					response.setSuccess(false);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
				response.setResponseMessage(user.getName() + " login Attadance Added successfully");
				response.setSuccess(true);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setResponseMessage("Already Logged IN Attandance is Added....");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setResponseMessage("SOMETHING_WENT_WRONG");
		response.setSuccess(false);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<CommonApiResponse> logOut(Long userId, String logoutlocation, MultipartFile logoutselfie) {
		CommonApiResponse response = new CommonApiResponse();

		try {
			User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE.value());
			if (user == null) {
				response.setResponseMessage("No User Found");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			Attendance ate = attendanceRepository.findTopByUserIdOrderByLogindateDesc(userId);
			if (ate.getLogindate().equals(LocalDate.now())) {

				Attendance attendance = attendanceRepository.findTopByUserIdOrderByLogoutdateDesc(userId);
				if (attendance.getLogouttime() == null) {
					attendance.setLogoutdate(LocalDate.now());
					attendance.setLogouttime(LocalTime.now());
					attendance.setLogoutlocation(logoutlocation);
					String selfie = storageService.store(logoutselfie);
					attendance.setLogoutselfie(selfie);
					attendance.setUser(user);

					Attendance data = attendanceRepository.save(attendance);

					if (data == null) {
						response.setResponseMessage("Failed to add login Attendance");
						response.setSuccess(false);
						return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
					}
					response.setResponseMessage(user.getName() + " LogOut Attadance Added successfully");
					response.setSuccess(true);
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					response.setResponseMessage("LogOut Attendance Already Added");
					response.setSuccess(false);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.setResponseMessage("Cannot add Logged Out Details,U are Not Login Today.....");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setResponseMessage("SOMETHING_WENT_WRONG");
		response.setSuccess(false);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<AttendanceResponseDto> findAttendanceForUserByMonth(Long userId, int year, int month) {
		AttendanceResponseDto response = new AttendanceResponseDto();
		try {
			if (userId == 0 || year == 0 || month == 0) {
				response.setResponseMessage("Missing Inputs");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			List<Attendance> lst = attendanceRepository.findAttendanceForUserByMonth(userId, year, month);

			if (lst.isEmpty()) {
				response.setResponseMessage("No Data Found...");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setResponseMessage(" Attadance Details Fetched successfully");
			response.setAttendances(lst);
			response.setSuccess(true);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setResponseMessage("SOMETHING_WENT_WRONG");
		response.setSuccess(false);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
