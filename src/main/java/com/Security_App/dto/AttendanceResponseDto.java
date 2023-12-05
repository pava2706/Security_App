package com.Security_App.dto;

import java.util.ArrayList;
import java.util.List;

import com.Security_App.entity.Attendance;
import com.Security_App.repository.AttendanceDetails;

import lombok.Data;

@Data
public class AttendanceResponseDto extends CommonApiResponse {

	private List<Attendance> attendances = new ArrayList<>();

	private List<AttendanceDetails> lst = new ArrayList<>();

}
