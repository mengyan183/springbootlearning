package com.tensquare.qa.controller;
import com.tensquare.friend.service.FriendService;
import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;
import com.tensquare.qa.vo.UserVO;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * 微服务
 * @author Administrator
 *
 */
@RestSchema(schemaId = "problem-qa")
@RequestMapping(path = "/problem-qa")
public class ProblemController {
	@Autowired
	private FriendService friendService;

	@Autowired
	private ProblemService problemService;
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(path="/", method =RequestMethod.GET,produces = APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List> findAll(String userid,String friendid){
		System.out.println("微服务2调用");
		return 	new ResponseEntity<>(problemService.findAll(),null, OK);
	}

	/**
	 * 新增用户关注
	 * @return
	 */
	@RequestMapping(path="/",method =RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> addFridend(@RequestBody UserVO userVO){
		//调用微服务关注
		java.util.Random r=new java.util.Random();
		int  returnState  = friendService.addFriend(r.nextInt()+"",userVO.getFriendid());

        //新增一个问题
		Problem problem  = new Problem();
		problem.setContent("哈哈啊");
		problemService.add(problem);

		return new ResponseEntity<String>(String.valueOf(returnState), null, OK);
	}

}
