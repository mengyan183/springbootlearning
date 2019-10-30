package com.tensquare.friend.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 交友表实体类
 */
@Entity
@Table(name="tb_friend")
@IdClass(Friend.class) // 联合主键
public class Friend implements Serializable{

    @Id
    private String userid;//用户ID

    @Id
    private String friendid;//好友ID
    private String islike;//是否互粉

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }
}
