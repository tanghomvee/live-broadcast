package com.homvee.livebroadcast.common.vos;

import java.io.Serializable;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-29 15:31
 */
public class ReqBody implements Serializable {

    private String operate;
    private String acctName;
    private CheckSMS checkSMS;
    private CheckRoom checkRoom;


   public class CheckSMS implements Serializable{
        private String content;
        private String toPhone;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getToPhone() {
            return toPhone;
        }

        public void setToPhone(String toPhone) {
            this.toPhone = toPhone;
        }
    }

    public class CheckRoom implements Serializable{
        private String roomUrl;

        public String getRoomUrl() {
            return roomUrl;
        }

        public void setRoomUrl(String roomUrl) {
            this.roomUrl = roomUrl;
        }
    }


    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public CheckSMS getCheckSMS() {
        return checkSMS;
    }

    public void setCheckSMS(CheckSMS checkSMS) {
        this.checkSMS = checkSMS;
    }

    public CheckRoom getCheckRoom() {
        return checkRoom;
    }

    public void setCheckRoom(CheckRoom checkRoom) {
        this.checkRoom = checkRoom;
    }
}
