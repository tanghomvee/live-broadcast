package com.homvee.livebroadcast.live;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;
import org.apache.commons.lang3.StringUtils;
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

    static WebClient webClient=new WebClient(BrowserVersion.CHROME);
    public static void main(String[] args) {

        // 实例化Web客户端
       // WebClient webClient=new WebClient(BrowserVersion.CHROME);
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
//                            if(request.getUrl().toExternalForm().contains("goalBf3.xml")){
//                                String content = response.getContentAsString(Charset.forName("UTF-8"));
//
//                                WebResponseData data = new WebResponseData(content.getBytes("UTF-8"),
//                                        response.getStatusCode(), response.getStatusMessage(), response.getResponseHeaders());
//                                response = new WebResponse(data, request, response.getLoadTime());
//                            }

                            String content = response.getContentAsString(Charset.forName("UTF-8"));
                            if(StringUtils.isNotEmpty(content)){
                                if(content.contains("geetest_item_img")){

                                    WebResponseData data = new WebResponseData(content.getBytes("UTF-8"),
                                            response.getStatusCode(), response.getStatusMessage(), response.getResponseHeaders());
                                    response = new WebResponse(data, request, response.getLoadTime());
                                }
                            }
                            return response;
                        }
                    }

            );

            // 解析获取页面
            HtmlPage page=webClient.getPage("https://www.douyu.com/t/2018KPLAutumn");
            executeScript();

            //设置cookie

//            String cookiesStr = "dy_did=cdd199ee4d4818821006cfd600081501; acf_did=cdd199ee4d4818821006cfd600081501; Hm_lvt_e99aee90ec1b2106afe7ec3b199020a7=1531875582; _dys_lastPageCode=page_studio_normal,; smidV2=201807180902599c9998e22c7813cb2664971827ee99000056ba92ddfc45f10; acf_ccn=45dc98a7a8f6cb74aff307cab2697770; PHPSESSID=iu9uktnqncpif41mmbo1ul0334; acf_auth=a303HZ6tHYP2w1itDHwqeh1ZlxPbltLnV7qrzQi6mA8Wjp1%2FerT0czz5KCt9MVU9wiEwutrLHvOGE88uhK%2F9dKe8EPg5rckg71DlLztAhJ%2FfT8ARf4oMaEw; wan_auth37wan=fd39822a027cbcddDzitv4znkQ3aMY3AV5HzXPvRUHNOfg9kiNNRkwHVbNFsoTe2lTGdauu4BDDFDr2bN4JsK5SkpoV0eTQWOYUur2BWVlfPR1C%2FRGY; acf_uid=212979017; acf_username=212979017; acf_nickname=%E6%96%97%E9%B1%BC%E7%94%A8%E6%88%B73Yd9BIVm; acf_own_room=0; acf_groupid=1; acf_phonestatus=1; acf_avatar=https%3A%2F%2Fapic.douyucdn.cn%2Fupload%2Favatar%2Fdefault%2F01_; acf_ct=0; acf_ltkid=69029933; acf_biz=1; acf_stk=11667cd472e23a34; Hm_lpvt_e99aee90ec1b2106afe7ec3b199020a7=1531904575";
//            String[] cookies = cookiesStr.split(";");
//            for (String cookie : cookies){
//               String[] kv = cookie.split("=");
//               Cookie cookieTmp = new Cookie(".douyu.com" ,kv[0] , kv[1]);
//            }


            webClient.getCookieManager().addCookie(new Cookie("www.douyu.com","acf_auth","9082RoWuaKuBdbGf%2FNWgrPtFg%2FUM9Jgk5zmisvFFdukYEII1%2BEz9IiVVGg1ECoubKNiTlbjmTyH%2Fd5vccXW2%2Bs%2FhciFkRx6jywY7FHJUCUc4zgUcis4ChXo"));
            webClient.getCookieManager().addCookie(new Cookie("www.douyu.com","acf_nickname","%E6%96%97%E9%B1%BC%E7%94%A8%E6%88%B73Yd9BIVm"));
            webClient.getCookieManager().addCookie(new Cookie(".douyu.com","wan_auth37wan","e4b34b778f53Q7oCInjhLFQeLuhMd8GM4uEpGHmYQSAwMEspDz%2BaahyZsKxmsCB3k%2BmRwkRHBxrzHU4rVL3VZ1k9LjT6g%2FkxC3WCHw%2BEIyymmvgm5vk"));



           // page=webClient.getPage("https://www.douyu.com/member/cp");


            sendMsg(page);









            //login(page);

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
        executeScript();

        //3.触发输入手机号码和密码
        HtmlElement phoneNumInput = pwdLoginPage.getElementByName("phoneNum");
        phoneNumInput.click();
        phoneNumInput.type("19940886898");
        HtmlElement pwdInput = pwdLoginPage.getElementByName("password");
        pwdInput.click();
        pwdInput.type("sdh1693522049@");

        //4.触发验证码
        List codeBtns = pwdLoginPage.getByXPath("//div[@class='geetest_radar_tip']");
        HtmlDivision codeBtn = (HtmlDivision) codeBtns.get(0);
        HtmlPage checkPage = codeBtn.click();
        executeScript();
        List images = checkPage.getByXPath("//img[@class='geetest_item_img']");
        HtmlImage valiCodeImg = (HtmlImage) images.get(0);

        //5.输入验证码
        // TODO

        ImageReader imageReader = valiCodeImg.getImageReader();
        BufferedImage bufferedImage = imageReader.read(0);

        JFrame f2 = new JFrame();
        JLabel l = new JLabel();
        l.setIcon(new ImageIcon(bufferedImage));
        f2.getContentPane().add(l);
        f2.setSize(400, 400);
        f2.setTitle("验证码");
        f2.setVisible(true);

        //6.提交
        List submitBtns = pwdLoginPage.getByXPath("//input[@type='submit']");
        HtmlButtonInput submitBtn = (HtmlButtonInput) submitBtns.get(0);
         submitBtn.click();
        executeScript();

    }
    static void sendMsg(HtmlPage page) throws Exception {
        //1.获取输入框
        List  textareas = page.getByXPath("//textarea[@class='cs-textarea']");
        HtmlElement textarea = (HtmlElement) textareas.get(0);
        textarea.click();
        textarea.type("22222222222222222222222222222222");

        //2.点击发送按钮
        List  sendBtns = page.getByXPath("//div[@data-type='send']");
        HtmlElement sendBtn = (HtmlElement) sendBtns.get(0);
        HtmlPage page1 =  sendBtn.click();
        executeScript();
        System.out.println();

    }


    public static void executeScript(){
        while (webClient.waitForBackgroundJavaScript(10000L) < 0){

        }
    }
}
