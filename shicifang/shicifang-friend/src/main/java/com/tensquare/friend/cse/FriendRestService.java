package com.tensquare.friend.cse;

import com.tensquare.friend.service.FriendService;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestSchema(schemaId="friend")
@RequestMapping("/friend")
public class FriendRestService {
    @Autowired
    private FriendService friendService;
    @GetMapping(path = "/like")
    public String addFriend(String friendid, String userid){
        System.out.println(22222);
          friendService.addFriend(userid, friendid);
          return "1";
    }
}
