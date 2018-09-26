<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.roomName" placeholder="账户名称"></el-input>
				</el-form-item>
				
				<el-form-item>
					<el-button type="primary" v-on:click="getRooms">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleAdd">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="rooms" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60">
			</el-table-column>
			<el-table-column prop="userName" label="用户名" width="auto">
			</el-table-column>
			<el-table-column prop="mobile" label="手机号码" width="auto">
			</el-table-column>
			<el-table-column prop="startHour" label="开始时间" width="auto">
			</el-table-column>
			<el-table-column prop="endHour" label="结束时间" width="auto">
			</el-table-column>

			<el-table-column prop="way" label="交流方式" :formatter="getWayDesc"  width="auto">
			</el-table-column>
			<el-table-column prop="defaultContent" label="默认话术"   width="auto">
			</el-table-column>

			<el-table-column prop="roomName" label="房间名称" width="auto">
			</el-table-column>

			<el-table-column prop="intervalTime" label="间隔时间(秒)" width="auto">
			</el-table-column>

			<el-table-column prop="url" label="房间直播地址" width="auto">
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
				<el-form-item prop="roomName" label="房间名称">
					<el-input v-model="editForm.roomName" placeholder="房间名称"></el-input>
				</el-form-item>
				<el-form-item prop="mobile" label="手机号码">
					<el-input v-model="editForm.mobile" placeholder="请输入11位手机号码"></el-input>
				</el-form-item>
				<el-form-item prop="startHour" label="开播时间">
					<el-select v-model="editForm.startHour" placeholder="开播时间">
						<el-option v-for="item in hours" :key="item.id" :value="item.id" :label="item.desc"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item prop="endHour" label="结束时间">
					<el-select  v-model="editForm.endHour" placeholder="结束时间">
						<el-option v-for="item in hours" :key="item.id" :value="item.id" :label="item.desc"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item prop="way" label="交流方式">
					<el-select v-model="editForm.way" placeholder="请选择交流方式">
						<el-option v-for="item in ways" :key="item.id" :value="item.id" :label="item.desc"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item prop="intervalTime" label="间隔时间">
					<el-input v-model="editForm.intervalTime" placeholder="间隔时间(秒)"></el-input>
				</el-form-item>
				<el-form-item prop="url" label="房间直播地址">
					<el-input type="textarea" v-model="editForm.url" placeholder="房间直播地址链接"></el-input>
				</el-form-item>
				<el-form-item prop="defaultContent" label="默认话术">
					<el-input type="textarea" v-model="editForm.defaultContent" placeholder="默认话术多个话术之间用英文逗号隔开"></el-input>
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
				<el-form-item prop="roomName" label="房间名称">
					<el-input v-model="addForm.roomName" placeholder="房间名称"></el-input>
				</el-form-item>
				<el-form-item prop="mobile" label="手机号码">
					<el-input v-model="addForm.mobile" placeholder="请输入11位手机号码"></el-input>
				</el-form-item>
				<el-form-item prop="startHour" label="开播时间">
					<el-select v-model="addForm.startHour" placeholder="开播时间">
						<el-option v-for="item in hours" :key="item.id" :value="item.id" :label="item.desc"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item prop="endHour" label="结束时间">
					<el-select v-model="addForm.endHour" placeholder="结束时间">
						<el-option v-for="item in hours" :key="item.id" :value="item.id" :label="item.desc"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item prop="way" label="交流方式">
					<el-select v-model="addForm.way" placeholder="请选择交流方式">
						<el-option v-for="item in ways" :key="item.id" :value="item.id" :label="item.desc"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item prop="intervalTime" label="间隔时间">
					<el-input v-model="addForm.intervalTime" placeholder="间隔时间(秒)"></el-input>
				</el-form-item>
				<el-form-item prop="url" label="房间直播地址">
					<el-input type="textarea" v-model="addForm.url" placeholder="房间直播地址链接"></el-input>
				</el-form-item>
				<el-form-item prop="defaultContent" label="默认话术">
					<el-input type="textarea" v-model="addForm.defaultContent" placeholder="默认话术多个话术之间用英文逗号隔开"></el-input>
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
    import {addRoom, delRoom, editRoom, listRoom} from '../../api/api';

    export default {
		data:function() {
			return {
				filters: {
                    roomName:null
				},
                rooms: [],
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
                    roomName: [
                        { required: true, message: '请输入房间名', trigger: 'blur' }
                    ],
                    mobile: [
                        { required: true, message: '请输入11位手机号码', trigger: 'blur' },
                        {
                            validator: function (rule, val, callback) {
                                if (val.length != 11 || !Number.isInteger(val/1)){
                                    callback(new Error());
                                    return;
                                }
                                callback();
                            },
                            message: '请输入11位手机号码',
                            trigger: 'blur'
                        }
                    ],
                    url: [
                        { required: true, message: '请输入房间直播地址', trigger: 'blur' }
                    ],
                    way: [
                        { required: true, message: '请选择交流方式', trigger: 'blur',type: 'number' }
                    ]
				},
				//编辑界面数据
				editForm: {
					id: null,
                    roomName: '',
					way:null,
                    mobile:null,
                    startHour:0,
                    endHour:23,
                    intervalTime:null,
					url:null,
                    defaultContent:null
				},

				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
                    roomName: [
                        { required: true, message: '请输入房间名', trigger: 'blur' }
                    ],
                    url: [
                        { required: true, message: '请输入房间直播地址', trigger: 'blur' }
                    ],
                    mobile: [
                        { required: true, message: '请输入11位手机号码', trigger: 'blur' },
                        {
                            validator: function (rule, val, callback) {
                                if (val.length != 11 || !Number.isInteger(val/1)){
                                    callback(new Error());
                                    return;
                                }
                                callback();
                            },
                            message: '请输入11位手机号码',
                            trigger: 'blur'
                        }
                    ],
                    way: [
                        { required: true, message: '请选择交流方式', trigger: 'blur',type: 'number' }
                    ]
				},
				//新增界面数据
				addForm: {
                    roomName: '',
                    way:null,
                    intervalTime:null,
                    mobile:null,
                    startHour:0,
                    endHour:23,
                    url:null,
                    defaultContent:null
				},
				ways:[
					{key:"NORMAL",id:1,desc:"正常"} , {key:"AUTO",id:2,desc:"自动"},{key:"STOP",id:3,desc:"停止"},{key:"STOP",id:4,desc:"循环"}
				],
				hours:[{desc:0,id:0},{desc:1,id:1},{desc:2,id:2},{desc:3,id:3},{desc:4,id:4},{desc:5,id:5},
					{desc:6,id:6},{desc:7,id:7},{desc:8,id:8},{desc:9,id:9},{desc:10,id:10},{desc:11,id:11},
					{desc:12,id:12},{desc:13,id:13},{desc:14,id:14},{desc:15,id:15},{desc:16,id:16},{desc:17,id:17},
					{desc:18,id:18},{desc:19,id:19},{desc:20,id:20},{desc:21,id:21},{desc:22,id:22},{desc:23,id:23}
				]

			}
		},
		methods: {
            getWayDesc:function (row, column) {
                for(var i=0 , len = this.ways.length; i < len ; i++){
                    if(row.way == this.ways[i].id){
                        return this.ways[i].desc;
					}
				}
                return '未知';
            },
			handleCurrentChange:function(val) {
				this.page = val;
				this.getRooms();
			},
			//获取内容列表
			getRooms:function() {
				let params = {
					pageNum: this.page,
                    pageSize:10,
                    roomName: this.filters.roomName
				};
				this.listLoading = true;
				NProgress.start();
                listRoom(params , this).then((res) => {
                    if(!res){
                        return;
					}
					this.total = res.total;
					this.rooms = res.data.data;
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
                    delRoom(para).then(function() {
                        _this.listLoading = false;
                        NProgress.done();
                        util.Msg.success(_this);
                        _this.getRooms();
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
			},
			//编辑
			editSubmit: function () {
                var _this = this;
				this.$refs.editForm.validate((valid) => {
					if (valid) {
					    util.Msg.confirm(_this , null ,function () {
                            _this.editLoading = true;
                            NProgress.start();
                            let para = Object.assign({}, _this.editForm);
                            editRoom(para).then(function() {
                                _this.editLoading = false;
                                NProgress.done();
                                util.Msg.success(_this);
                                _this.$refs['editForm'].resetFields();
                                _this.editFormVisible = false;
                                _this.getRooms();
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
                            addRoom(para).then(function() {
                                _this.addLoading = false;
                                NProgress.done();
                                util.Msg.success(_this);
                                _this.$refs['addForm'].resetFields();
                                _this.addFormVisible = false;
                                _this.getRooms();
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
                util.Msg.warning(_this , null ,function () {
                    _this.listLoading = true;
                    NProgress.start();
                    let para = { ids: ids };
                    delRoom(para).then(function() {
                        _this.listLoading = false;
                        NProgress.done();
                        util.Msg.success(_this);
                        _this.getRooms();
                    });
                });

			}
		},
		mounted() {
			this.getRooms();
		}
	}

</script>

<style scoped>

</style>