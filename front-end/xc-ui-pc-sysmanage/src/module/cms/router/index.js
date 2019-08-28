import Home from '@/module/home/page/home.vue'
// eslint-disable-next-line camelcase
import page_list from '@/module/cms/page/page_list.vue'
// eslint-disable-next-line camelcase
import page_add from '@/module/cms/page/page_add.vue'

export default [{
  path: '/',
  component: Home,
  name: 'CMS', // 菜单名称
  hidden: false,
  children: [
    {path: '/cms/page/list', name: '页面列表', component: page_list, hidden: false},
    {path: '/cms/page/add', name: '新增页面', component: page_add, hidden: true}
  ]
}
]
