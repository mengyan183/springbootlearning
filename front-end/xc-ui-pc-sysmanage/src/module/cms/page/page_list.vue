<template>
  <div>
    <!--查询表单-->
    <el-form :model="params">
      <el-select v-model="params.siteId" placeholder="请选择站点">
        <el-option
          v-for="item in siteList"
          :key="item.siteId"
          :label="item.siteName"
          :value="item.siteId">
        </el-option>
      </el-select>
      页面别名：
      <el-input v-model="params.pageAliase" style="width: 100px"></el-input>
      <el-button type="primary" v-on:click="query" size="small">查询</el-button>
      <router-link class="mui‐tab‐item" :to="{path:'/cms/page/add/',query:{
        page: this.params.page,
        siteId: this.params.siteId}}">
        <el-button type="primary" size="small">新增页面</el-button>
      </router-link>
    </el-form>
    <el-table
      :data="list"
      stripe
      style="width: 100%">
      <el-table-column type="index" width="60">
      </el-table-column>
      <el-table-column prop="pageName" label="页面名称" width="120">
      </el-table-column>
      <el-table-column prop="pageAliase" label="别名" width="120">
      </el-table-column>
      <el-table-column prop="pageType" label="页面类型" width="150">
      </el-table-column>
      <el-table-column prop="pageWebPath" label="访问路径" width="250">
      </el-table-column>
      <el-table-column prop="pagePhysicalPath" label="物理路径" width="250">
      </el-table-column>
      <el-table-column prop="pageCreateTime" label="创建时间" width="180" :formatter="formatCreatetime">
      </el-table-column>
      <el-table-column label="编辑" width="80">
        <template slot-scope="scope">
          <el-button
            size="small" type="primary"
            @click="edit(scope.row.pageId)">编辑
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="删除" width="80">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="danger"
            @click="del(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="页面预览" width="80">
        <template slot-scope="scope">
          <el-button
            size="small" type="primary"
            @click="preview(scope.row.pageId)">页面预览
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="静态化" width="80">
        <template slot-scope="scope">
          <el-button
            size="small" type="primary" plain @click="generateHtml(scope.row.pageId)">静态化
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="发布" width="80">
        <template slot-scope="scope">
          <el-button
            size="small" type="primary" plain @click="postPage(scope.row.pageId)">发布
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      layout="prev, pager, next"
      :total="total"
      :page-size="params.size"
      :current-page="params.page"
      v-on:current-change="changePage"
      style="float:right">
    </el-pagination>
  </div>
</template>
<script>
    /*编写页面静态部分，即model及vm部分。*/
    import * as cmsApi from '../api/cms'
    import utilApi from '@/common/utils';
    export default {
    data() {
      return {
          siteList: [],//站点列表
        list: [],
        total:0,
        params:{
          page:1,
          size: 10,
          siteId: '',
          pageAliase: ''
        }
      }
    },
    methods:{
        formatCreatetime(row, column) {
            var createTime = new Date(row.pageCreateTime);
            if (createTime) {
                return utilApi.formatDate(createTime, 'yyyy-MM-dd hh:mm:ss');
            }
        },
        generateHtml(id) {
            this.$router.push({
                path: '/cms/page/html/' + id, query: {
                    page: this.params.page,
                    siteId: this.params.siteId
                }
            })
        },
        postPage(id) {
            this.$confirm('确认发布该页面吗?', '提示', {}).then(() => {
                this.listLoading = true;
                cmsApi.page_postPage(id).then((res) => {
                    if (res.success) {
                        console.log('发布页面id=' + id);
                        this.listLoading = false;
                        this.$message.success('发布成功，请稍后查看结果');
                    } else {
                        this.$message.error('发布失败');
                    }
                });
            }).catch(() => {

            });
        },
        edit(pageId) {
            // 改变路由 地址
            this.$router.push({
                path: '/cms/page/edit/' + pageId, query: {
                    page: this.params.page,
                    siteId: this.params.siteId
                }
            })
        },
        //预览页面
        preview(pageId){
            // 打开nginx代理 访问
            window.open("http://localhost:31000/cms/preview/"+pageId)
        },
        //删除
        del(index, row) {
            this.$confirm('确认删除该记录吗?', '提示', {
                type: 'warning'
            }).then(() => {
                this.listLoading = true;
                let pageId = row.pageId;
                cmsApi.page_del(pageId).then((res) => {
                    this.listLoading = false;
                    if (res.success) {
                        this.$message.success("删除成功")
                        this.query();
                    } else {
                        this.$message.error('删除失败');
                    }

                });
            }).catch(() => {

            });
        },
      query:function(){
        //调用服务端的接口
        cmsApi.page_list(this.params.page,this.params.size,this.params).then((res)=>{
          //将res结果数据赋值给数据模型对象
          this.list = res.queryResult.list;
          this.total = res.queryResult.total;
        })
      },
      changePage:function(page){//形参就是当前页码
        //调用query方法
        // alert(page)
        this.params.page = page;
        this.query()
      },
        querySiteList: function () {
            //调用服务端的接口
            cmsApi.site_list().then((res) => {
                //将res结果数据赋值给数据模型对象
                // 查询站点列表接口
                this.siteList = res;
            })

        }
    },
    mounted(){
      //当DOM元素渲染完成后调用query
        this.query();
        this.querySiteList();
    },
    created() {
        // 页面渲染之前
        //从路由上获取参数
        this.params.page = Number.parseInt(this.$route.query.page||1);
        this.params.siteId = this.$route.query.siteId||'';
    }
  }
</script>
<style>
  /*编写页面样式，不是必须*/
</style>
