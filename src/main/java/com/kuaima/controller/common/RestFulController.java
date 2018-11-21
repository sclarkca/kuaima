package com.kuaima.controller.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.kuaima.entity.Commodity;
import com.kuaima.service.opencity.OpenCityService;

@RequestMapping
@Controller
public class RestFulController {
	@Autowired
	OpenCityService openCityService;

	@RequestMapping("/bu")
	@ResponseBody
	public void query(){
		List<Commodity> list = openCityService.queryCmmdityListByMerCodeAndBuCode("BU000001");
		System.err.println(JSON.toJSONString(list));
	}
}
