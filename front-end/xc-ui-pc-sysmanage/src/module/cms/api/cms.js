import http from './../../../base/api/public'
// eslint-disable-next-line no-unused-vars
import querystring from 'querystring'

let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.xcApiUrlPre

// 页面查询
// eslint-disable-next-line camelcase
export const page_list = (page, size, params) => {
  let param = querystring.stringify(params)
  // 请求服务端的页面查询接口
  return http.requestQuickGet(apiUrl + '/cms/page/list/' + page + '/' + size + '?' + param)
}

// eslint-disable-next-line camelcase
export const site_list = () => {
  // 请求服务端的页面查询接口
  return http.requestQuickGet(apiUrl + '/cms/site/listall')
}
