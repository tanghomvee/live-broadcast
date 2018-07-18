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
            HtmlPage page=webClient.getPage("https://www.douyu.com/4869407");

            //设置cookie
            BasicCookieStore cookieStore = new BasicCookieStore();
            Set<Cookie> cookies2 =webClient.getCookieManager().getCookies();
            for (Cookie cookie : cookies2) {
                cookieStore.addCookie(cookie.toHttpClient());
            }
            //设置一个运行JavaScript的时间
            webClient.waitForBackgroundJavaScript(5000);
            login(page);
            //发送消息的DIV
//           DomElement msgDiv = page.getElementById("js-send-msg");
//
//           //获取登陆
//           List loginBtns = msgDiv.getByXPath("//a[@data-type='login']");
//           HtmlAnchor loginBtn = (HtmlAnchor) loginBtns.get(0);
//           HtmlPage loginPage = loginBtn.click();
//
//            List spans = loginPage.getByXPath("//span[@data-type='login']");
//            HtmlSpan span = (HtmlSpan) spans.get(0);
//            HtmlPage pwdLoginPage = span.click();
//
//            HtmlElement phoneNum = pwdLoginPage.getElementByName("phoneNum");
//            phoneNum.click();
//            phoneNum.type("19940886898");
//            HtmlElement pwd = pwdLoginPage.getElementByName("password");
//            pwd.click();
//            pwd.type("sdh1693522049@");
//
//            //验证码
//            List divs = loginPage.getByXPath("//div[@class='geetest_radar_tip']");
//            HtmlDivision div = (HtmlDivision) divs.get(0);
//            HtmlPage checkPage = div.click();
//
//            //HtmlImage valiCodeImg = (HtmlImage) checkPage.getByXPath("//img[@class='geetest_tip_img']");
//            HtmlImage valiCodeImg = (HtmlImage) checkPage.getByXPath("//img[@class='geetest_item_img']");
//            ImageReader imageReader = valiCodeImg.getImageReader();
//            BufferedImage bufferedImage = imageReader.read(0);
//
//            JFrame f2 = new JFrame();
//            JLabel l = new JLabel();
//            l.setIcon(new ImageIcon(bufferedImage));
//            f2.getContentPane().add(l);
//            f2.setSize(300, 300);
//            f2.setTitle("验证码");
//            f2.setVisible(true);
//
////           ScriptResult result = page.executeJavaScript("document.getElementById('pk_1248827').onmouseover(window.event)");
//           ScriptResult result = page.executeJavaScript("$('#js-send-msg').find('a[data-type='login']').trigger('click')");
//           HtmlPage jspage = (HtmlPage) result.getNewPage();
//
//            System.out.println(jspage.asXml());
        } catch (FailingHttpStatusCodeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            webClient.close(); // 关闭客户端，释放内存
        }

    }


    /**
     * 解析登陆页
     * @param page
     */
    static void login(HtmlPage page) throws Exception {
        //1.触发主页的登陆页面
        List loginBtns = page.getByXPath("//a[@data-button-type='login']");
        HtmlAnchor loginBtn = (HtmlAnchor) loginBtns.get(0);
        HtmlPage loginPage = loginBtn.click();

        //2.触发主页的密码登陆按钮
        List pwdBtns = loginPage.getByXPath("//span[@data-type='login']");
        HtmlSpan pwdBtn = (HtmlSpan) pwdBtns.get(0);
        HtmlPage pwdLoginPage = pwdBtn.click();

        List phonePwdLoginSpans = pwdLoginPage.getByXPath("//span[@data-subtype='login-by-phoneNum']");
        HtmlSpan phonePwdLoginSpan = (HtmlSpan) phonePwdLoginSpans.get(0);
        HtmlPage phonePwdLoginPage = phonePwdLoginSpan.click();

        //3.触发输入手机号码和密码
        HtmlElement phoneNumInput = pwdLoginPage.getElementByName("phoneNum");
        phoneNumInput.click();
        phoneNumInput.type("19940886898");
        HtmlElement pwdInput = pwdLoginPage.getElementByName("password");
        pwdInput.click();
        pwdInput.type("sdh1693522049@");

        //4.触发验证码
        List codeBtns = pwdLoginPage.getByXPath("//div[@id='gt-captcha-1']");
        HtmlDivision codeBtn = (HtmlDivision) codeBtns.get(0);
//        HtmlDivision codeBtn = (HtmlDivision) pwdLoginPage.getElementsById("gt-captcha-1");
        HtmlPage checkPage = codeBtn.click();

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
    }
}
