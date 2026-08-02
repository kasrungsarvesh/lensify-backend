package com.lensify.service;

import com.lensify.dto.DashboardDto;
import com.lensify.response.ApiResponse;

public interface DashboardService {

    ApiResponse<DashboardDto> getDashboard();

}
