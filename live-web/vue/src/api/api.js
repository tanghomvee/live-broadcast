import axios from 'axios';

let base = 'http://localhost';

export const requestLogin = params => {
    return axios({
        method:"post",
        url:"http://localhost/user/login",
        data:params,
        headers:{
            'Content-Type':'application/x-www-form-urlencoded'
        }
    }).then(res =>{
        console.info(res);
    });
};

export const getUserList = params => { return axios.get(`${base}/user/list`, { params: params }); };

export const getUserListPage = params => { return axios.get(`${base}/user/listpage`, { params: params }); };

export const removeUser = params => { return axios.get(`${base}/user/remove`, { params: params }); };

export const batchRemoveUser = params => { return axios.get(`${base}/user/batchremove`, { params: params }); };

export const editUser = params => { return axios.get(`${base}/user/edit`, { params: params }); };

export const addUser = params => { return axios.get(`${base}/user/add`, { params: params }); };