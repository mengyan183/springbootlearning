package com.tensquare.friend.service.impl;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private FriendDao friendDao;

    @Transactional
    public int addFriend(String userid,String friendid){
        //向交友表中插入记录
        Friend friend=new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);
        return 1;//表示成功
    }
}
