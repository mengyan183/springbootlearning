import Home from '@/module/home/page/home.vue'
// eslint-disable-next-line camelcase
import page_list from '@/module/cms/page/page_list.vue'
// eslint-disable-next-line camelcase
import page_add from '@/module/cms/page/page_add.vue'
// eslint-disable-next-line camelcase
import page_edit from '@/module/cms/page/page_edit.vue'
// eslint-disable-next-line camelcase
import page_html from '@/module/cms/page/page_html.vue'

export default [{
  path: '/',
  component: Home,
  name: 'CMS', // 菜单名称
  hidden: false,
  children: [
    {path: '/cms/page/list', name: '页面列表', component: page_list, hidden: false},
    {path: '/cms/page/add', name: '新增页面', component: page_add, hidden: true},
    // 定义路由 使用":"表示该路由需要在地址栏进行传参
    {path: '/cms/page/edit/:pageId', name: '修改页面', component: page_edit, hidden: true},
    {path: '/cms/page/html/:pageId', name: '生成html', component: page_html, hidden: true}
  ]
}
]
