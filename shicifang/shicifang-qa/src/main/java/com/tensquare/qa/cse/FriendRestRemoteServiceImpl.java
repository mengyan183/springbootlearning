package com.tensquare.qa.cse;

import com.tensquare.friend.service.FriendService;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FriendRestRemoteServiceImpl implements FriendService {
    @Autowired
    private  RestTemplate restTemplate = RestTemplateBuilder.create();

    @Override
    public int addFriend(String userid, String friendid) {
        //service url is : cse://serviceName/operation
        //shicifang-friend项目中的microservice.yaml 里面的name 微服务名称
        String serviceName = "shicifang-friend";
        String url  = "cse://" + serviceName + "/friend/like?userid="+userid+"&friendid="+friendid+"";

        String  value =  restTemplate.getForObject(url, String.class);
        if(value.equals("1")){
            return 1;
        }
        return 0;

    }
}
