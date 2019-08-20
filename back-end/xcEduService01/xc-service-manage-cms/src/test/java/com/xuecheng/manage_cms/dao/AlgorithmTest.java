/*
 * Copyright (c) 2019, Jiehun.com.cn Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.dao;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * AlgorithmTest
 *
 * @author guoxing
 * @date 8/15/2019 9:48 AM
 * @since 2.0.0
 **/
//@SpringBootTest
//@RunWith(SpringRunner.class)
@Slf4j
public class AlgorithmTest {
    /**
     * 入度为N-1;出度为0的 为法官
     *
     * @param N     人数
     * @param trust 信任关系
     * @return
     */
    public int findJudge(int N, int[][] trust) {
        if (N < 2) {
            return N;
        }
        // 在 二维数组 的 第二个 值存在 , 第一个 值 不存在 的为 法官
        Set<Integer> xList = new HashSet<>(trust.length);
        Set<Integer> yList = new HashSet<>(trust.length);
        for (int[] ints : trust) {
            int x = ints[0];
            int y = ints[1];
            yList.add(y);
            xList.add(x);
        }
        // 获取在yList中存在 , 在 xList中不存在 的 数据
        // 法官候选人
        yList.removeAll(xList);
        if (yList.size() == 0) {
            return -1;
        }
        for (Integer candidate : yList){
            // 如果入度为N-1代表 除 本身外 , 信任关系中 包含其余人 的全部二维数组
            if(candidate != null){

            }
        }

        return -1;
    }

    @Test
    public void test() {
        int n = 3;
        int[][] trust = {{1, 3}, {2, 3}, {3, 1}};
        int judge = findJudge(n, trust);
        log.info("{}", judge);
    }
}
