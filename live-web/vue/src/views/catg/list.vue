<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.catgName" placeholder="类目名称"></el-input>
				</el-form-item>

				<el-form-item label="父级类目">
					<el-select v-model="editForm.parentId" placeholder="请选择父级类目" :clearable="true">
						<el-option v-for="item in parentCatgs" :key="item.id" :value="item.id" :label="item.catgName"></el-option>
					</el-select>
				</el-form-item>


				<el-form-item>
					<el-button type="primary" v-on:click="getCategories">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleAdd">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="categories" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60">
			</el-table-column>
			<el-table-column prop="userName" label="用户名" width="auto">
			</el-table-column>
			
			<el-table-column prop="catgName" label="类目名称" width="auto">
			</el-table-column>

			<el-table-column prop="parentCatgName" label="父级类目名称" width="auto">
			</el-table-column>

			<el-table-column label="操作" width="150">
				<template scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					<el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
			<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">
			</el-pagination>
		</el-col>

		<!--编辑界面-->
		<el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
				<el-form-item label="类目名称">
					<el-input v-model="editForm.catgName" placeholder="类目名称"></el-input>
				</el-form-item>

				<el-form-item label="选择父级类目">
					<el-select v-model="editForm.parentId" placeholder="请选择父级类目" :clearable="true">
						<el-option v-for="item in parentCatgs" :key="item.id" :value="item.id" :label="item.catgName"></el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
			</div>
		</el-dialog>

		<!--新增界面-->
		<el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">
			<el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
				<el-form-item label="类目名称">
					<el-input v-model="addForm.catgName" placeholder="类目名称"></el-input>
				</el-form-item>

				<el-form-item label="选择父级类目">
					<el-select v-model="addForm.parentId" placeholder="请选择父级类目" :clearable="true">
						<el-option v-for="item in parentCatgs" :key="item.id" :value="item.id" :label="item.catgName"></el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="addFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
			</div>
		</el-dialog>
	</section>
</template>

<script>
    import util from '../../common/js/util';
    import NProgress from 'nprogress';
    import {addCatg, delCatg, editCatg, listCatg, parentCatg} from '../../api/api';

    export default {
		data:function() {
			return {
				filters: {
                    catgName:null,
					parentId:null
				},
                categories: [],
				total: 0,
				page: 1,
				listLoading: false,
				sels: [],//列表选中列

				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
                    id: [
                        { required: true, message: '请选择修改的内容', trigger: 'blur' }
                    ],
                    catgName: [
                        { required: true, message: '请输入分类名称', trigger: 'blur' }
                    ]
				},
				//编辑界面数据
				editForm: {
					id: null,
                    catgName: '',
                    parentId:null
				},

				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
                    catgName: [
                        { required: true, message: '请输入分类名称', trigger: 'blur' }
                    ]
				},
				//新增界面数据
				addForm: {
                    catgName: '',
                    parentId:null
				},
                parentCatgs:[{id:null,catgName:"请选择父类"}]

			}
		},
		methods: {
			
			handleCurrentChange:function(val) {
				this.page = val;
				this.getCategories();
			},
            getParentCatgs:function(){
                var _this = this;
                parentCatg({},_this).then(function (res) {
                    if(!res){
                        return;
                    }
                    _this.parentCatgs = res.data;
                });
            },
			//获取内容列表
			getCategories:function() {
				let params = {
					pageNum: this.page,
                    pageSize:10,
                    catgName: this.filters.catgName,
					parentId:this.filters.parentId
				};
				this.listLoading = true;
				NProgress.start();
                listCatg(params , this).then((res) => {
                    if(!res){
                        return;
					}
					this.total = res.total;
					this.categories = res.data.data;
					this.listLoading = false;
					NProgress.done();
				});
			},
			//删除
			handleDel: function (index, row) {
			    var _this = this;
			    util.Msg.warning(_this , null , function () {
                    _this.listLoading = true;
                    NProgress.start();
                    let para = { ids: row.id };
                    delCatg(para).then(function() {
                        _this.listLoading = false;
                        NProgress.done();
                        util.Msg.success(_this);
                        _this.getCategories();
                    });
                });
			},
			//显示编辑界面
			handleEdit: function (index, row) {
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			//显示新增界面
			handleAdd: function () {

				this.addFormVisible = true;
				this.addForm = {
                    catgName: ''
				};
			},
			//编辑
			editSubmit: function () {
                var _this = this;
				this.$refs.editForm.validate((valid) => {
					if (valid) {
                        util.Msg.confirm(_this , null , function () {
                            _this.editLoading = true;
                            NProgress.start();
                            let para = Object.assign({}, _this.editForm);
                            editCatg(para).then(function() {
                                _this.editLoading = false;
                                NProgress.done();
                                util.Msg.success(_this);
                                _this.$refs['editForm'].resetFields();
                                _this.editFormVisible = false;
                                _this.getCategories();
                            });
                        });
					}
				});
			},
			//新增
			addSubmit: function () {
                var _this = this;
				this.$refs.addForm.validate((valid) => {
					if (valid) {
                        util.Msg.confirm(_this , null , function () {
                            _this.addLoading = true;
                            NProgress.start();
                            let para = Object.assign({}, _this.addForm);
                            addCatg(para).then(function(){
                                _this.addLoading = false;
                                NProgress.done();
                                util.Msg.success(_this);
                                _this.$refs['addForm'].resetFields();
                                _this.addFormVisible = false;
                                _this.getCategories();
                            });
                        });
					}
				});
			},
			selsChange: function (sels) {
				this.sels = sels;
			},
			//批量删除
			batchRemove: function () {
				var ids = this.sels.map(item => item.id).toString();
                var _this = this;
                util.Msg.warning(_this , null , function () {
                    _this.listLoading = true;
                    NProgress.start();
                    let para = { ids: ids };
                    delCatg(para).then(function(){
                        _this.listLoading = false;
                        NProgress.done();
                        util.Msg.success(_this);
                        _this.getCategories();
                    });
                });
			}
		},
		mounted() {
            this.getParentCatgs();
			this.getCategories();
		}
	}

</script>

<style scoped>

</style>