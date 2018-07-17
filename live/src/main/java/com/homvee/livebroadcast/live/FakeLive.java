package com.homvee.livebroadcast.live;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;
import org.apache.http.impl.client.BasicCookieStore;

import javax.imageio.ImageReader;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
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

            //启用JS解释器，默认为true
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setActiveXNative(false);
            //禁用css支持
            webClient.getOptions().setCssEnabled(false);
            //js运行错误时，是否抛出异常
            webClient.getOptions().setThrowExceptionOnScriptError(false);

            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setTimeout(5000);
            //设置支持AJAX
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.setWebConnection(
                    new WebConnectionWrapper(webClient) {
                        @Override
                        public WebResponse getResponse(WebRequest request) throws IOException {
                            WebResponse response = super.getResponse(request);
                            if(request.getUrl().toExternalForm().contains("goalBf3.xml")){
                                String content = response.getContentAsString(Charset.forName("UTF-8"));

                                WebResponseData data = new WebResponseData(content.getBytes("UTF-8"),
                                        response.getStatusCode(), response.getStatusMessage(), response.getResponseHeaders());
                                response = new WebResponse(data, request, response.getLoadTime());
                            }
                            return response;
                        }
                    }

            );

            // 解析获取页面
            HtmlPage page=webClient.getPage("https://www.douyu.com/t/2018KPLAutumn");


            System.out.println(URLDecoder.decode("\\u65ed\\u65ed\\u5b9d\\u5b9d"));
            BasicCookieStore cookieStore = new BasicCookieStore();
            Set<Cookie> cookies2 =webClient.getCookieManager().getCookies();
            for (Cookie cookie : cookies2) {
                cookieStore.addCookie(cookie.toHttpClient());
            }
            //设置一个运行JavaScript的时间
            webClient.waitForBackgroundJavaScript(5000);

           DomElement chatDiv = page.getElementById("js-send-msg");

           List links = chatDiv.getByXPath("//a[@data-type='login']");
           HtmlAnchor link = (HtmlAnchor) links.get(0);
          HtmlPage loginPage = link.click();

            List spans = loginPage.getByXPath("//span[@data-type='login']");
            HtmlSpan span = (HtmlSpan) spans.get(0);
            HtmlPage pwdLoginPage = span.click();

            HtmlElement phoneNum = pwdLoginPage.getElementByName("phoneNum");
            phoneNum.click();
            phoneNum.type("19940886898");
            HtmlElement pwd = pwdLoginPage.getElementByName("password");
            pwd.click();
            pwd.type("sdh1693522049@");

            //验证码
            List divs = loginPage.getByXPath("//div[@class='geetest_radar_tip']");
            HtmlDivision div = (HtmlDivision) divs.get(0);
            HtmlPage checkPage = div.click();

            //HtmlImage valiCodeImg = (HtmlImage) checkPage.getByXPath("//img[@class='geetest_tip_img']");
            HtmlImage valiCodeImg = (HtmlImage) checkPage.getByXPath("//img[@class='geetest_item_img']");
            ImageReader imageReader = valiCodeImg.getImageReader();
            BufferedImage bufferedImage = imageReader.read(0);

            JFrame f2 = new JFrame();
            JLabel l = new JLabel();
            l.setIcon(new ImageIcon(bufferedImage));
            f2.getContentPane().add(l);
            f2.setSize(300, 300);
            f2.setTitle("验证码");
            f2.setVisible(true);

//           ScriptResult result = page.executeJavaScript("document.getElementById('pk_1248827').onmouseover(window.event)");
           ScriptResult result = page.executeJavaScript("$('#js-send-msg').find('a[data-type='login']').trigger('click')");
           HtmlPage jspage = (HtmlPage) result.getNewPage();

            System.out.println(jspage.asXml());
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
