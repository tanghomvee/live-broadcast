<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.content" placeholder="聊天内容"></el-input>
				</el-form-item>
				<el-form-item label="选择房间">
					<el-select v-model="filters.roomId" placeholder="请选择直播房间" :clearable="true">
						<el-option v-for="item in rooms" :key="item.id" :value="item.id" :label="item.roomName"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="选择账户">
					<el-select v-model="filters.acctId" placeholder="请选择账户" :clearable="true">
						<el-option v-for="item in accts" :key="item.id" :value="item.id" :label="item.acctName"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" v-on:click="getContents">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleAdd">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="contents" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60">
			</el-table-column>
			<el-table-column prop="userName" label="用户名" width="100">
			</el-table-column>
			<el-table-column prop="catgName" label="分类" width="100">
			</el-table-column>
			<el-table-column prop="roomName" label="房间名称" width="120" >
			</el-table-column>
			<el-table-column prop="acctName" label="账户名称" width="120">
			</el-table-column>

			<el-table-column prop="used" label="使用" width="100" :formatter="formatUsed">
			</el-table-column>
			<el-table-column prop="content" label="内容" min-width="180" >
			</el-table-column>
			<el-table-column prop="preContent" label="父级内容" min-width="180">
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
				<el-form-item label="选择房间">
					<el-select v-model="editForm.roomId" placeholder="请选择直播房间"  @change="getPreContents">
						<el-option v-for="item in rooms" :key="item.id" :value="item.id" :label="item.roomName"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="选择账户">
					<el-select v-model="editForm.acctId" placeholder="请选择账户">
						<el-option v-for="item in accts" :key="item.id" :value="item.id" :label="item.acctName"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="选择类目">
					<el-select v-model="editForm.parentCatgId" placeholder="请选择父级类目"  @change="getChildCatgs">
						<el-option v-for="item in parentCatgs" :key="item.id" :value="item.id" :label="item.catgName"></el-option>
					</el-select>
					<el-select v-model="editForm.catgId" placeholder="请选择子类目">
						<el-option v-for="item in childrenCatgs" :key="item.id" :value="item.id" :label="item.catgName"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="选择父级内容">
					<el-select v-model="editForm.preId" placeholder="选择父级内容">
						<el-option v-for="item in pres" :key="item.id" :value="item.id" :label="item.content"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="输入内容">
					<el-input type="textarea" v-model="editForm.content"></el-input>
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
				<el-form-item label="选择房间">
					<el-select v-model="addForm.roomId" placeholder="请选择直播房间" @change="getPreContents">
						<el-option v-for="item in rooms" :key="item.id" :value="item.id" :label="item.roomName"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="选择账户">
					<el-select v-model="addForm.acctId" placeholder="请选择账户">
						<el-option v-for="item in accts" :key="item.id" :value="item.id" :label="item.acctName"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="选择类目">
					<el-select v-model="addForm.parentCatgId" placeholder="请选择父级类目"  @change="getChildCatgs">
						<el-option v-for="item in parentCatgs" :key="item.id" :value="item.id" :label="item.catgName"></el-option>
					</el-select>
					<el-select v-model="addForm.catgId" placeholder="请选择子类目">
						<el-option v-for="item in childrenCatgs" :key="item.id" :value="item.id" :label="item.catgName"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="选择父级内容">
					<el-select v-model="addForm.preId" placeholder="选择父级内容">
						<el-option v-for="item in pres" :key="item.id" :value="item.id" :label="item.content"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="输入内容">
					<el-input type="textarea" v-model="addForm.content"></el-input>
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
    import {
        addContent,
        allAcct,
        allRoom,
        childrenCatg,
        delContent,
        editContent,
        listContent,
        parentCatg
    } from '../../api/api';

    export default {
		data:function() {
			return {
				filters: {
                    content:null,
                    roomId: null,
                    acctId: null,
                    catgId: null
				},
                contents: [],
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
                    roomId: [
                        { required: true, message: '请选择直播房间', trigger: 'blur' }
                    ],
                    acctId: [
                        { required: true, message: '请选择发言账户', trigger: 'blur' }
                    ],
                    catgId: [
                        { required: true, message: '请选择发言类型', trigger: 'blur' }
                    ],
                    content: [
                        { required: true, message: '请输入发言内容', trigger: 'blur' }
                    ],
				},
				//编辑界面数据
				editForm: {
					id: null,
                    roomId: null,
                    acctId: null,
                    catgId: null,
                    parentCatgId: null,
                    preId: null,
                    content: ''
				},

				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
                    roomId: [
						{ required: true, message: '请选择直播房间', trigger: 'blur' }
					],
                    acctId: [
						{ required: true, message: '请选择发言账户', trigger: 'blur' }
					],
                    catgId: [
						{ required: true, message: '请选择发言类型', trigger: 'blur' }
					],
                    content: [
						{ required: true, message: '请输入发言内容', trigger: 'blur' }
					],
				},
				//新增界面数据
				addForm: {
                    roomId: null,
                    acctId: null,
                    catgId: null,
                    parentCatgId: null,
                    preId: null,
                    content: ''
				},
				rooms:[{id:null,roomName:"请选择房间"}],
				accts:[{id:null,acctName:"请选择账户"}],
				parentCatgs:[{id:null,catgName:"请选择父类"}],
				childrenCatgs:[{id:null,catgName:"请选择子类"}],
				pres:[],

			}
		},
		methods: {
			//使用显示转换
            formatUsed: function (row, column) {
				return row.used == 1 ? '已使用' : row.used == 0 ? '未使用' : '未知';
			},
			handleCurrentChange:function(val) {
				this.page = val;
				this.getContents();
			},
			getRooms:function(){
                var _this = this;
                allRoom({},_this).then(function (res) {
                    if(!res){
                        return;
                    }
                    _this.rooms = res.data;
                });
			},
			getAccts:function(){
                var _this = this;
                allAcct({},_this).then(function (res) {
                    if(!res){
                        return;
                    }
                    _this.accts = res.data;
                });
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
			getChildCatgs:function(val){
                console.info(val);
                var _this = this;
                childrenCatg({parentId:val},_this).then(function (res) {
                    if(!res){
                        return;
                    }
                    _this.childrenCatgs = res.data;
                });
			},
            getPreContents:function(val){
                let params = {
                    pageNum: this.page,
                    roomId: val,
                    pageSize:1000000
                };
                var _this = this;
                listContent(params , this).then(function(res)  {
                    if(!res){
                        return;
                    }
                    _this.pres = res.data.data;
                });
			},
			//获取内容列表
			getContents:function() {
				let params = {
					pageNum: this.page,
                    pageSize:10,
					content: this.filters.content,
					roomId: this.filters.roomId,
					acctId: this.filters.acctId,
					catgId: this.filters.catgId
				};
				this.listLoading = true;
				NProgress.start();
                listContent(params , this).then((res) => {
                    if(!res){
                        return;
					}
					this.total = res.total;
					this.contents = res.data.data;
					this.listLoading = false;
					NProgress.done();
				});
			},
			//删除
			handleDel: function (index, row) {
                var _this = this;
                util.Msg.warning(_this , null , function(resp) {
                    _this.listLoading = true;
                    NProgress.start();
                    let para = { ids: row.id };
                    delContent(para).then(function(){
                        _this.listLoading = false;
                        NProgress.done();
                        util.Msg.success(_this , null);
                        _this.getContents();
                    });
                });

			},
			//显示编辑界面
			handleEdit: function (index, row) {
                this.getRooms();
                this.getAccts();
                this.getParentCatgs();
                this.getPreContents(row.roomId);
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			//显示新增界面
			handleAdd: function () {
                this.getRooms();
                this.getAccts();
                this.getParentCatgs();
                //this.getPreContents();
				this.addFormVisible = true;
				this.addForm = {
                    roomId: null,
                    acctId: null,
                    catgId: null,
                    preId: null,
                    content: ''
				};
			},
			//编辑
			editSubmit: function () {
                var _this = this;
				this.$refs.editForm.validate((valid) => {
					if (valid) {
                        util.Msg.confirm(_this , null , function(resp) {
                            _this.editLoading = true;
                            NProgress.start();
                            let para = Object.assign({}, _this.editForm);
                            if(!para.catgId){
                                para.catgId = para.parentCatgId;
                            }
                            editContent(para).then(function()  {
                                _this.editLoading = false;
                                NProgress.done();
                                util.Msg.success(_this);
                                _this.$refs['editForm'].resetFields();
                                _this.editFormVisible = false;
                                _this.getContents();
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
						util.Msg.confirm(_this , null ,function () {
                            _this.addLoading = true;
                            NProgress.start();
                            let para = Object.assign({}, _this.addForm);
                            if(!para.catgId){
                                para.catgId = para.parentCatgId;
                            }
                            addContent(para).then(function() {
                                _this.addLoading = false;
                                NProgress.done();
                                util.Msg.success(_this);
                                _this.$refs['addForm'].resetFields();
                                _this.addFormVisible = false;
                                _this.getContents();
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
                var _this = this;
                var ids = this.sels.map(item => item.id).toString();
                util.Msg.warning(_this , null ,function (){
                    _this.listLoading = true;
					NProgress.start();
					let para = { ids: ids };
					delContent(para).then(function() {
                        _this.listLoading = false;
						NProgress.done();
                        util.Msg.success(_this);
                        _this.getContents();
					});
                });
			}
		},
		mounted() {
		    this.getRooms();
		    this.getAccts();
			this.getContents();
		}
	}

</script>

<style scoped>

</style>