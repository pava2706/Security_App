package com.Security_App.dto;

import java.util.ArrayList;
import java.util.List;

import com.Security_App.entity.User;

import lombok.Data;

@Data
public class UserLoginResponse extends CommonApiResponse {

	private User user;

	private String jwtToken;

	private List<User> users = new ArrayList<>();
}
