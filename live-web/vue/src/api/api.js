import axios from 'axios';
import util from '../common/js/util';


let base = 'http://localhost';

export const ajax = (url , method  , headers , params , calller) => {
    console.info(params);
    return axios({
        "method":method || "post",
        "url":url,
        "params":params,
        "data":params,
        "headers" :headers || {
            'Content-Type':'application/x-www-form-urlencoded'
        }
    }).then(function(res){
        console.info(res);
        var msg = null;
        if(!res.data){
            msg = "系统错误";

        }else if(res.data.flag != "success"){
            if(res.data.flag == "login"){
                calller.$router.push({ path: '/login' });
                return;
            }
            msg = res.data.msg || "未知错误";
        }

        if (msg){
            if(calller){

                util.Msg.error(calller  ,msg);
            }
            return;
        }


        return res.data;
    }).catch(function (error) {
        console.log(error);
        if(calller){

            util.Msg.error(calller ,"系统错误");
        }
    });
};


export const requestLogin = (params,caller) => {
    return ajax("/user/login" , null ,null ,params , caller);
};
export const setting = (params,caller) => {
    return ajax("/user/setting" , null ,null ,params , caller);
};


export const listContent = (params,caller) => {
    return ajax("/content/list" , null ,null ,params , caller);
};
export const addContent = (params,caller) => {
    return ajax("/content/add" , null ,null ,params , caller);
};
export const oneContent = (params,caller) => {
    return ajax("/content/one" , null ,null ,params , caller);
};
export const editContent = (params,caller) => {
    return ajax("/content/edit" , null ,null ,params , caller);
};
export const delContent = (params,caller) => {
    return ajax("/content/del" , null ,null ,params , caller);
};


export const allRoom = (params,caller) => {
    return ajax("/room/all" , null ,null ,params , caller);
};
export const listRoom = (params,caller) => {
    return ajax("/room/list" , null ,null ,params , caller);
};
export const oneRoom = (params,caller) => {
    return ajax("/room/one" , null ,null ,params , caller);
};
export const addRoom = (params,caller) => {
    return ajax("/room/add" , null ,null ,params , caller);
};
export const editRoom = (params,caller) => {
    return ajax("/room/edit" , null ,null ,params , caller);
};
export const delRoom = (params,caller) => {
    return ajax("/room/del" , null ,null ,params , caller);
};



export const allAcct = (params,caller) => {
    return ajax("/acct/all" , null ,null ,params , caller);
};
export const addAcct = (params,caller) => {
    return ajax("/acct/add" , null ,null ,params , caller);
};
export const oneAcct = (params,caller) => {
    return ajax("/acct/one" , null ,null ,params , caller);
};
export const editAcct = (params,caller) => {
    return ajax("/acct/edit" , null ,null ,params , caller);
};
export const listAcct = (params,caller) => {
    return ajax("/acct/list" , null ,null ,params , caller);
};
export const delAcct = (params,caller) => {
    return ajax("/acct/del" , null ,null ,params , caller);
};


export const parentCatg = (params,caller) => {
    return ajax("/catg/parent" , null ,null ,params , caller);
};
export const childrenCatg = (params,caller) => {
    return ajax("/catg/children" , null ,null ,params , caller);
};
export const listCatg = (params,caller) => {
    return ajax("/catg/list" , null ,null ,params , caller);
};
export const oneCatg = (params,caller) => {
    return ajax("/catg/one" , null ,null ,params , caller);
};
export const addCatg = (params,caller) => {
    return ajax("/catg/add" , null ,null ,params , caller);
};
export const editCatg = (params,caller) => {
    return ajax("/catg/edit" , null ,null ,params , caller);
};
export const delCatg = (params,caller) => {
    return ajax("/catg/del" , null ,null ,params , caller);
};

export const getUserList = params => { return axios.get(`${base}/user/list`, { params: params }); };
