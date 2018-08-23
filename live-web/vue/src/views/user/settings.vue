<template>
	<el-form :model="settingForm" :rules="settingFormValidator" ref="settingForm" label-width="120px" style="margin:20px;width:60%;min-width:600px;">
		<h3 class="title">修改密码</h3>
		<el-form-item prop="account" label="账户名称">
			<el-input type="text" v-model="settingForm.account" auto-complete="off" placeholder="账号" :disabled="true"></el-input>
		</el-form-item>
		<el-form-item prop="oldPwd" label="输入旧密码">
			<el-input type="password" v-model="settingForm.oldPwd" auto-complete="off" placeholder="请输入原始密码"></el-input>
		</el-form-item>
		<el-form-item prop="newPwd" label="输入新密码">
			<el-input type="password" v-model="settingForm.newPwd" auto-complete="off" placeholder="请输入新密码"></el-input>
		</el-form-item>
		<el-form-item prop="reNewPwd" label="再次输入新密码">
			<el-input type="password" v-model="settingForm.reNewPwd" auto-complete="off" placeholder="请再次输入新密码"></el-input>
		</el-form-item>
		<el-form-item style="width:100%;">
			<el-button type="primary" style="width:100%;" @click.native.prevent="submitFun" :loading="logining">提交</el-button>
			<!--<el-button @click.native.prevent="resetFun">重置</el-button>-->
		</el-form-item>
	</el-form>
</template>

<script>
    import {setting} from '../../api/api';
    import util from '../../common/js/util';
    import NProgress from 'nprogress';

    export default {
        name:"setting",
        data:function() {
            return {
                logining: false,
                settingForm: {
                    account:null,
                    oldPwd: null,
                    newPwd: null,
                    reNewPwd: null
                },
                settingFormValidator: {
                    oldPwd: [
                        { required: true, message: '请输入原始密码', trigger: 'blur' }
                    ],
                    newPwd: [
                        { required: true, message: '请输入新密码', trigger: 'blur' }
                    ],
                    reNewPwd: [
                        { required: true, message: '请再次输入新密码', trigger: 'blur' }
                    ]
                },
                checked: true
            };
        },
        methods: {
            resetFun:function() {
                this.$refs.settingForm.resetFields();
            },
            submitFun:function(ev) {
                var _this = this;
                this.$refs.settingForm.validate((valid) => {
                    if (valid) {

                        if (this.settingForm.newPwd != this.settingForm.reNewPwd){
                            util.Msg.error(_this , "两次密码不一致");
                            return;
						}


                        this.logining = true;
                        NProgress.start();
                        var loginParams = { userName: this.settingForm.account,
                            oldPwd: util.Encryption.RSA.encrypt(this.settingForm.oldPwd) ,
                            newPwd: util.Encryption.RSA.encrypt(this.settingForm.newPwd) ,
                            reNewPwd: util.Encryption.RSA.encrypt(this.settingForm.reNewPwd)
                        };
                        setting(loginParams , _this).then(resp => {
                            this.logining = false;
                            NProgress.done();
                            if(!resp){
                                return;
                            }

                            let { msg, flag, data } = resp;
                            if (flag !== "success") {
                                util.Msg.error(_this , msg);
                            } else {
                                sessionStorage.setItem('user', JSON.stringify(data));
                                this.$router.push({ path: '/content/list' });
                            }
                        });
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        },
        mounted() {
            var user = sessionStorage.getItem('user');
            if (user) {
                user = JSON.parse(user);
                this.settingForm.account = user.userName || '';
            }

        }
    }

</script>