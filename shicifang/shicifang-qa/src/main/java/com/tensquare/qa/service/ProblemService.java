package com.tensquare.qa.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.tensquare.qa.dao.ProblemDao;
import com.tensquare.qa.pojo.Problem;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class ProblemService {

	@Autowired
	private ProblemDao problemDao;
	
	@Autowired
	private IdWorker idWorker;

	public List<Problem> findAll() {

		return problemDao.findAll();
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Problem> findPage(int page, int size) {
		PageRequest pageRequest = new PageRequest(page-1, size);
		return problemDao.findAll(pageRequest);
	}

	private Specification<Problem> where(Map searchMap) {

		return (Specification<Problem>) (root, query, cb) -> {
			List<Predicate> predicateList = new ArrayList<Predicate>();
// ID
			if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
				predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
			}
// 标题
			if (searchMap.get("title") != null && !"".equals(searchMap.get("title"))) {
				predicateList.add(cb.like(root.get("title").as(String.class), "%" + (String) searchMap.get("title") + "%"));
			}
// 内容
			if (searchMap.get("content") != null && !"".equals(searchMap.get("content"))) {
				predicateList.add(cb.like(root.get("content").as(String.class), "%" + (String) searchMap.get("content") + "%"));
			}
// 用户ID
			if (searchMap.get("userid") != null && !"".equals(searchMap.get("userid"))) {
				predicateList.add(cb.like(root.get("userid").as(String.class), "%" + (String) searchMap.get("userid") + "%"));
			}
// 昵称
			if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
				predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
			}
// 是否解决
			if (searchMap.get("solve") != null && !"".equals(searchMap.get("solve"))) {
				predicateList.add(cb.like(root.get("solve").as(String.class), "%" + (String) searchMap.get("solve") + "%"));
			}
// 回复人昵称
			if (searchMap.get("replyname") != null && !"".equals(searchMap.get("replyname"))) {
				predicateList.add(cb.like(root.get("replyname").as(String.class), "%" + (String) searchMap.get("replyname") + "%"));
			}

			return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

		};

	}

	public Page<Problem> findSearch(Map whereMap, int page, int size) {
		Specification<Problem> specification = where(whereMap);
		PageRequest pageRequest = new PageRequest(page-1, size);
		return problemDao.findAll(specification, pageRequest);
	}

	public Problem findOne(String id) {
		return problemDao.findOne(id);
	}

	public void add(Problem problem) {
		problem.setId(idWorker.nextId()+""); //主键值
		problemDao.save(problem);
	}
	
	public void update(Problem problem) {
		problemDao.save(problem);
	}

	public void delete(String id) {
		problemDao.delete(id);
	}

	public void deleteList(String[] ids) {
		for (String id : ids) {
			problemDao.delete(id);
		}
	}

}
