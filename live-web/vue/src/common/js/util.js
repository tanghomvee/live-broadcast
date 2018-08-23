import {JSEncrypt} from 'jsencrypt';

var SIGN_REGEXP = /([yMdhsm])(\1*)/g;
var DEFAULT_PATTERN = 'yyyy-MM-dd';
var public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOf+929MZBj/PvUtIGh0sCbe+BnVjkkBJbhuN+4DqTWpRAlZGpN0VPH8qOyzrKnRwu46oB8FFVAh3dDXgXELx0MC1I/dNyLaG26NTVDfZh2fnSRjcpxTf82LQZbfxxNoYgmRIDgg6thwAvZqTCSy8JjRkRDDJEthLko+Zli/b1TQIDAQAB";
function padding(s, len) {
    var len = len - (s + '').length;
    for (var i = 0; i < len; i++) { s = '0' + s; }
    return s;
};

export default {
    getQueryStringByName: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        var context = "";
        if (r != null)
            context = r[2];
        reg = null;
        r = null;
        return context == null || context == "" || context == "undefined" ? "" : context;
    },
    formatDate: {


        format: function (date, pattern) {
            pattern = pattern || DEFAULT_PATTERN;
            return pattern.replace(SIGN_REGEXP, function ($0) {
                switch ($0.charAt(0)) {
                    case 'y': return padding(date.getFullYear(), $0.length);
                    case 'M': return padding(date.getMonth() + 1, $0.length);
                    case 'd': return padding(date.getDate(), $0.length);
                    case 'w': return date.getDay() + 1;
                    case 'h': return padding(date.getHours(), $0.length);
                    case 'm': return padding(date.getMinutes(), $0.length);
                    case 's': return padding(date.getSeconds(), $0.length);
                }
            });
        },
        parse: function (dateString, pattern) {
            var matchs1 = pattern.match(SIGN_REGEXP);
            var matchs2 = dateString.match(/(\d)+/g);
            if (matchs1.length == matchs2.length) {
                var _date = new Date(1970, 0, 1);
                for (var i = 0; i < matchs1.length; i++) {
                    var _int = parseInt(matchs2[i]);
                    var sign = matchs1[i];
                    switch (sign.charAt(0)) {
                        case 'y': _date.setFullYear(_int); break;
                        case 'M': _date.setMonth(_int - 1); break;
                        case 'd': _date.setDate(_int); break;
                        case 'h': _date.setHours(_int); break;
                        case 'm': _date.setMinutes(_int); break;
                        case 's': _date.setSeconds(_int); break;
                    }
                }
                return _date;
            }
            return null;
        }

    },
    Msg: {
        confirm:function (caller , msg , callback) {
            caller.$confirm(msg || "确认此操作吗?" , "提示消息" , {}).then(function (res) {
                if(callback){
                    callback(res);
                }
            }).catch(function (res) {
                console.warn(res);
            });
        },
        success:function (caller , msg ) {
            caller.$message({
                message: msg || "操作成功",
                type: "success"
            });
        },
        error:function (caller , msg ) {
            caller.$message({
                message: msg || "操作失败",
                type: "error"
            });
        },
        warning:function (caller , msg , callback) {
            caller.$confirm(msg || "确认此操作吗?" , "提示消息" , {type: "warning"}).then(function (res) {
                if(callback){
                    callback(res);
                }
            }).catch(function (res) {
                console.warn(res);
            });
        }
    },
    Encryption:{
        RSA:{
            encrypt:function (data) {
                var encrypt = new JSEncrypt();
                encrypt.setPublicKey(public_key);
                return encrypt.encrypt(data);
            }
        }
    }

};
