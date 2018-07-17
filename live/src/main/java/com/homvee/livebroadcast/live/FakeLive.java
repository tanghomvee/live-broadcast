package com.homvee.livebroadcast.live;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-07-17 09:48
 */
public class FakeLive {

    public static void main(String[] args) {

        WebClient webClient=new WebClient(BrowserVersion.CHROME); // 实例化Web客户端
        try {
            HtmlPage page=webClient.getPage("http://blog.java1234.com/index.html"); // 解析获取页面
            HtmlForm form=page.getFormByName("myform"); // 得到搜索Form
            HtmlTextInput textField=form.getInputByName("q"); // 获取查询文本框
            HtmlSubmitInput button=form.getInputByName("submitButton"); // 获取提交按钮
            textField.setValueAttribute("java"); // 文本框“填入”数据
            HtmlPage page2=button.click(); // 模拟点击
            System.out.println(page2.asXml());
        } catch (FailingHttpStatusCodeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            webClient.close(); // 关闭客户端，释放内存
        }
    }
}
