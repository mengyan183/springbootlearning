import http from './../../../base/api/public'
// eslint-disable-next-line no-unused-vars
import querystring from 'querystring'

let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.xcApiUrlPre

// eslint-disable-next-line camelcase
export const page_get = (pageId) => {
  return http.requestQuickGet(apiUrl + '/cms/page/get/' + pageId)
}

// eslint-disable-next-line camelcase
export const page_edit = (pageId, params) => {
  return http.requestPut(apiUrl + '/cms/page/edit/' + pageId, params)
}
// 页面查询
// eslint-disable-next-line camelcase
export const page_list = (page, size, params) => {
  let param = querystring.stringify(params)
  // 请求服务端的页面查询接口
  return http.requestQuickGet(apiUrl + '/cms/page/list/' + page + '/' + size + '?' + param)
}

//  新增页面
// eslint-disable-next-line camelcase
export const page_add = (params) => {
  // 请求服务端的页面查询接口
  return http.requestPost(apiUrl + '/cms/page/add/', params)
}

// eslint-disable-next-line camelcase
export const site_list = () => {
  // 请求服务端的页面查询接口
  return http.requestQuickGet(apiUrl + '/cms/site/listall')
}
// 删除页面
// eslint-disable-next-line camelcase
export const page_del = id => {
  return http.requestDelete(apiUrl + '/cms/page/del/' + id)
}

/* 生成静态文件 */
// eslint-disable-next-line camelcase
export const page_generateHtml = id => {
  return http.requestPost(apiUrl + '/cms/page/generateHtml/' + id)
}
/* 取出静态文件 */
// eslint-disable-next-line camelcase
export const page_getHtml = id => {
  return http.requestQuickGet(apiUrl + '/cms/page/getHtml/' + id)
}
/* 发布页面 */
// eslint-disable-next-line camelcase
export const page_postPage = id => {
  return http.requestPost(apiUrl + '/cms/page/postPage/' + id)
}
