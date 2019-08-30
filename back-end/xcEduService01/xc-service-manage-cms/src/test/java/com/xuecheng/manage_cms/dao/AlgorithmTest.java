/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
        // 入度为N-1;出度为0的 为法官
        // 在 二维数组 的 第二个 值存在 , 第一个 值 不存在 的为 法官 候选人
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
        // 获取小镇上 所有 人
        HashSet<Integer> allTownPeople = new HashSet<>();
        allTownPeople.addAll(yList);
        allTownPeople.addAll(xList);
        for (Integer candidate : yList){
            // 如果入度为N-1代表 除 本身外 , 信任关系中 包含其余人 的全部二维数组
            if(candidate != null){
                //相信 该候选人 的 人
                HashSet<Integer> candidateTrusted = new HashSet<>();
                for (int[] ints : trust) {
                    int x = ints[0];
                    int y = ints[1];
                    if (candidate.equals(y)) {
                        candidateTrusted.add(x);
                    }
                }
                HashSet<Integer> copyAllTownPeople = new HashSet<>(allTownPeople);
                //小镇所有人减去所有相信该候选人的人 ;如果有剩余 且 剩余人数 为 1 且 剩余的人 为 该候选人 则 候选人 为 法官
                copyAllTownPeople.removeAll(candidateTrusted);
                if (!CollectionUtils.isEmpty(copyAllTownPeople) && copyAllTownPeople.size() == 1 && copyAllTownPeople.contains(candidate)) {
                    return candidate;
                }
            }
        }
        return -1;
    }

    /**
     * 入度为N-1;出度为0的 为法官
     *
     * @param N     人数
     * @param trust 信任关系
     * @return
     */
    public Integer findJudgeV2(int N, int[][] trust) {
        if (N < 2) {
            return N;
        }
        // 入度
        HashMap<Integer, Integer> incomingDegree = new HashMap<>();
        // 出度
        HashMap<Integer, Integer> outDegree = new HashMap<>();
        // 入度 - 出度 = N-1
        for (int[] ints : trust) {
            incomingDegree.put(ints[1], incomingDegree.getOrDefault(ints[1], 0) + 1);
            outDegree.put(ints[0], outDegree.getOrDefault(ints[0], 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : incomingDegree.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(N - 1) && !outDegree.containsKey(entry.getKey())) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * 入度为N-1;出度为0的 为法官
     *
     * @param N     人数
     * @param trust 信任关系
     * @return
     */
    public Integer findJudgeV3(int N, int[][] trust) {
        if (N < 2) {
            return N;
        }
        int[] dValue = new int[N];
        // 入度 - 出度 = N-1
        for (int[] ints : trust) {
            // 出度
            dValue[ints[0]]--;
            // 入度
            dValue[ints[1]]++;
        }
        for (int i : dValue) {
            if (i == N - 1) {
                return i;
            }
        }
        return -1;
    }


    public int numRabbits(int[] answers) {
        // 回答的值,相同回答值集合
        HashMap<Integer, List<Integer>> integerIntegerHashMap = new HashMap<>();
        int minTotalNum = 0;
        for (int i : answers) {
            // 如果 其他 的数量 为 0;代表当前颜色 只有 一个 数据
            if (i == 0) {
                minTotalNum += 1;
                continue;
            }
            List<Integer> orDefault = integerIntegerHashMap.getOrDefault(i, new ArrayList<>());
            orDefault.add(i);
            integerIntegerHashMap.put(i, orDefault);
        }
        for (Map.Entry<Integer, List<Integer>> entry : integerIntegerHashMap.entrySet()) {
            List<Integer> value = entry.getValue();
            Integer key = entry.getKey();
            if (value != null && value.size() > 0) {
                // 相同的回答 可能存在 不同颜色的 兔子;如果  回答的数量 <= 回答的值 + 1 ,则该颜色的数量 为 回答的值 + 1; 反之,则 相同回答中存在 不同颜色的数据
                int size = value.size();
                if (size <= (key + 1)) {
                    minTotalNum += key + 1;
                } else {
                    minTotalNum += (key + 1) * (size / (key + 1))+ (size % (key + 1) > 0 ? 1 : 0) * (key + 1);
                }
            }
        }
        return minTotalNum;
    }



    @Test
    public void test() {
//        int n = 3;
//        int[][] trust = {{1, 3}, {2, 3}, {3, 1}};
//        int judge = findJudge(n, trust);
//        log.info("{}", judge);
        int[] answers = {0, 0, 1, 1, 1};
        log.info(numRabbits(answers) + "");
    }
}
