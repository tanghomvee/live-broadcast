package com.homvee.livebroadcast.live;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

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

        // 实例化Web客户端
        WebClient webClient=new WebClient(BrowserVersion.CHROME);
        try {
            // 解析获取页面
            HtmlPage page=webClient.getPage("https://www.douyu.com/t/FACEITLONDONMAJOR?roomIndex=0");
            // 得到搜索Form
//            HtmlForm form=page.querySelector("div#");
//            // 获取查询文本框
//            HtmlTextInput textField=form.getInputByName("q");
//            // 获取提交按钮
//            HtmlSubmitInput button=form.getInputByName("submitButton");
//            // 文本框“填入”数据
//            textField.setValueAttribute("java");
//            // 模拟点击
//            HtmlPage page2=button.click();
//            System.out.println(page2.asXml());

            System.out.println(URLDecoder.decode("\\u65ed\\u65ed\\u5b9d\\u5b9d"));
            BasicCookieStore cookieStore = new BasicCookieStore();
            Set<Cookie> cookies2 =webClient.getCookieManager().getCookies();
            for (Cookie cookie : cookies2) {
                cookieStore.addCookie(cookie.toHttpClient());
            }


           DomElement chatDiv = page.getElementById("js-send-msg");
          List as =  chatDiv.getByXPath("//a[@]data-type='login'");;
            DomNodeList<HtmlElement> lis =  chatDiv.getElementsByTagName("li");
            List<HtmlDivision> divList=page.getByXPath("//li[@class='jschartli']");
            for (HtmlDivision htmlDivision : divList) {
                System.out.println("***********************************************8");
                System.out.println(htmlDivision.asXml());
            }

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

    /**
     *
     * @param v1
     * @param v2
     * @return v1=v2 返回 0；v1 > v2 返回 1；v1 < v2 返回 -1
     */
//    static  int compare(String v1 , String v2){
//
//        v1 = v1.replace("." , "");
//        v2 = v2.replace("." , "");
//        Long v1L = 0L, v2L = 0L;
//        if(v1.length() == v2.length()){
//
//        }else if(v1.length() > v2.length()){
//            for (int i =0 , len = v1.length() - v2.length(); i < len ; i++){
//                v2 = v2+"0";
//            }
//
//        }else {
//            for (int i =0 , len = v2.length() - v1.length(); i < len ; i++){
//                v1 = v1+"0";
//            }
//        }
//        v1L = Long.parseLong(v1);
//        v2L = Long.parseLong(v2);
//
//        return Long.compare(v1L ,v2L);
//    }
//
//
//    static  void merge(int[] a , int[] b){
//
//        for (int i : a) {
//            System.out.print(i + ",");
//        }
//        System.out.println();
//        for (int j : b) {
//            System.out.print(j + ",");
//        }
//        // 获取数组长度
//        int len_a = a.length;
//        int len_b = b.length;
//        System.out.println("len_a:" + len_a + ",len_b:" + len_b);
//        int[] result = new int[len_a + len_b];
//        int i, j, k;
//        i = j = k = 0;
//        while (i < len_a && j < len_b) {
//            if (a[i] <= b[j]){
//
//                result[k++] = a[i++];
//            }
//            else {
//                result[k++] = b[j++];
//            }
//        }
//        // 处理数组剩余的值，插入结果数组中
//        while (i < len_a){
//
//            result[k++] = a[i++];
//        }
//        while (j < len_b){
//
//            result[k++] = b[j++];
//        }
//
//        for (int p : result) {
//            System.out.print(p + ",");
//        }
//
//
//    }
}
