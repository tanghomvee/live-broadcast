package com.homvee.livebroadcast.live;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-07-24 15:13
 */
@WebServlet(name = "/xxxx" , urlPatterns = {"/get"})
public class Servlet extends HttpServlet {
    /**
     * {
     * 	"content":"发言内容或者URL",
     * 	"operate":"操作类型"//chat:发言标志;wait:等待发言;go:跳转到指定的房间此时content的数据为URL
     * }
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String content = null;
        try{
            content = getData(request.getParameter("name"),"chat");
        }catch (Exception ex){
            ex.printStackTrace();
            JSONObject retJSON = new JSONObject();
            retJSON.put("content" , "");
            retJSON.put("operate" , "chat");
        }
        response.getWriter().write(content);
        response.getWriter().flush();
        response.getWriter().close();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.doPost(request , response);
    }

    public synchronized String getData(String acct ,String operate){

        String[] randStrs = new String[]{
//                "^_^ "," ԅ(¯㉨¯ԅ) "," （￢㉨￢）   ","  ٩(♡㉨♡ )۶  ","  ヽ(○^㉨^)ﾉ♪ ","  (╥ ㉨ ╥`)   ","  ҉٩(*^㉨^*)  ",
//                " （≧㉨≦） "," （⊙㉨⊙） "," (๑•́ ㉨ •̀๑) "," ◟(░´㉨`░)◜ ",

                "·","^","`",".","_","~",",","、","¯","♡","o_o","I","i","|","l"
        };

        int index = getAcct(acct);

        String content = "";

        if(index != 0){
//            operate = "wait";
//            content = "10000";
            String data  = "" + (System.currentTimeMillis() /1000);
            for (int i = 0 , len = data.length(); i < len ; i++){
                int code =Character.getNumericValue(data.toCharArray()[i]);
                content = content + randStrs[code];
            }
        }else{
            //cnt ++ ;
            if (cnt < contents.length ){
                content = contents[cnt];
            }

            String data  = "" + (System.currentTimeMillis() /1000);
            String randStr = "";
            for (int i = 0 , len = data.length(); i < len ; i++){
                int code =Character.getNumericValue(data.toCharArray()[i]);
                randStr = randStr + randStrs[code];
            }

            content = content + randStr;
        }



        JSONObject retJSON = new JSONObject();
        retJSON.put("content" , content);
        retJSON.put("operate" , operate);
        return retJSON.toJSONString();
    }

    public static int cnt = -1;

    synchronized int getAcct(String acct){
        if(accts == null || accts.length==0){
            accts = new String[]{
                    "小玉熙的男朋友",
                    "呀是你的随风",
                    "芙丶芙",
                    "冲阿我的泥巴大王",
                    "回首已是夜沉沉"
            };
        }
        int index = ArrayUtils.indexOf(accts , acct);
        if(index == 0) {
            if(cnt >= contents.length){
                cnt = -1;
            }
            cnt ++ ;
            accts = ArrayUtils.remove(accts , index);
        }
        return index;
    }

    public static String[] accts = null;
    public static String[] contents = new String[]{
            "报告，发现小仙女一枚",
            "哈哈",
            "的确是小仙女",
            "你们都错了",
            "那是咋？？？",
            "那可是AK小仙女",
            "666",
            "666",
            "兄弟阔以",
            "谢谢同志们夸奖",
            "跨个毛哦，现在的男人都那么会撩的吗",
            "其实我觉得一般般",
            "其实我也就是凑合一下 ",
            "666 不是在赞他 只是貌似不配合一下显得太尴尬",
            "我这是要感谢你们的同情心的节奏吗",
            "没有啦 AK小仙女也是没错的",
            "技术与美貌并存的意思么老铁",
            "没毛病啊哈哈",
            "仙女姐姐，你貌似缺了点东西啊",
            "老铁 你不是同行吧……",
            "两个一直来玩段子套路的是啥意思",
            "看着吧 等等就是说缺一个他自己了",
            "这么尴尬的吗啊哈哈",
            "我就静静地看着你装逼",
            "来 我们不要打断他 缺一个啥 你快说",
            "对",
            "老铁给机会你讲 ",
            "我也好奇我们的小仙女缺了啥",
            "缺了子弹 比心心 ",
            "……",
            "……",
            "来搞笑的吗",
            "zzzz对此有期待的难道是搞错了什么吗",
            "同上",
            "其实我觉得可能是我的错",
            "为啥",
            "估计是他本来是想说缺一个他的 结果被人一语道破",
            "尬的一批",
            "早知道就不说了啊哈哈",
            "我到底进来看了啥玩意儿",
            "我个人觉得还可以啊哈哈",
            "气氛还是不错滴",
            "小仙女技术还真的可以",
            "目测对枪压枪都还可以",
            "那些事基本好不好",
            "菜的一批的我静悄悄的路过一下哈啊哈哈",
            "我也说哈哈 我uzi都还没压得好呢",
            "兄弟 你看那意识 走位才是重要的好不好",
            "哈哈 可以可以",
            "貌似聊天里的人都很牛逼的样子",
            "屁啦 最强是小仙女好不好",
            "貌似排位很高的样子啊哈哈",
            "同感",
            "有点意思啊哈哈",
            "如果有小仙女带就好了",
            "我也想被我家小仙女保护",
            "啥玩意……",
            "老铁 是男人你应该带妹才对啊",
            "囧 被妹子带也是很爽的好不好",
            "求带上车啊哈哈",
            "虽然如果小仙女要带我 我也是毫不介意的",
            "这是什么操作……",
            "我去 还辛苦你了 啊 被小仙女带",
            "那么幸福的吗",
            "懂啥 被带着其实不是重点知道吗",
            "那啥才是重点",
            "好奇宝宝上身",
            "又要来段子了吗",
            "来来来 是不是有人要步我后尘",
            "屁屁屁去去去 重点是能和小仙女姐姐一起经历着枪林弹雨 享受阳光日落 开车兜风 一起打打药 吃吃饱 喝喝可乐啥的",
            "这是什么操作……",
            "溜得一批",
            "我觉还是不错啊哈哈哈",
            "有点情趣啊 啊哈哈 阔以",
            "羡慕死了zzz",
            "不用羡慕我那么有才华哦兄弟",
            "这又是啥情况",
            "说个段子 一起玩有啥好羡慕",
            "我只是羡慕我们家小仙女啊哈哈哈",
            "为啥",
            "zzzzzzzzzz",
            "自己加油才对啊 兄弟",
            "我尴尬地 一位有人羡慕我啊哈哈哈",
            "老铁们 你们到底在想什么东西 开玩笑哦",
            "我们貌似都 走错了路啊哈哈",
            "尬的一批",
            "其实不是啦",
            "那是啥",
            "观望一下啊哈哈哈",
            "我的是羡慕 如果我有小仙女那么漂亮 那我的技术就不会白费了啊哈哈",
            "什么技术",
            "单身回首已是夜沉沉0年的技术吗",
            "错了 那是单身回首已是夜沉沉0年的手速",
            "zzzz 要是我有那么美 我早就来直播了啊哈哈",
            "小仙女无可代替",
            "老子铁粉",
            "就算 其他的女主播 其实也就有个样子 没啥好看的",
            "哎呀 那么完美的就只有我们家的小仙女好不好",
            "也是也是",
            "囧 反正其他的我没啥兴趣就是了",
            "我觉得比较特别的是我小仙女说话的声音……",
            "竟然有人和我想的一样",
            "大家是不是觉得有那种。。。Mimi的感觉",
            "啥是mimi啊哈哈",
            "我个人感觉就是娇娇的",
            "啊 没错 娇娇的",
            "我貌似看到了很多人在盯着我们的小仙女流口水啊哈哈",
            "大锅 你为什么要说出来！！！",
            "苗条淑女 ",
            "啊哈哈哈哈哈",
            "君子好逑",
            "对对对 说得对~！~~！",
            "接的不错啊哈哈哈",
            "基本操作谢谢夸奖~~~",
            "这两人是哪里来的默契……",
            "默契个啥 苗条淑女 君子好逑 这八个字是基本好不好 电影都看过多少次了啊哈哈",
            "对不起兄弟们 我补充一下 流口水是因为我见到了我们家小仙女的 技术",
            "有分别么",
            "目测木有啊哈哈哈",
            "没有啦 其实都是我们的 小仙女 ",
            "铁粉的我表示 必须天天来看",
            "基本操作",
            "那是必须的好不好",
            "下班累了就来看看啊哈哈",
            "还能学一下技术",
            "我主要是来看妹子 技术老子早就有了哈哈哈哈",
            "回首已是夜沉沉0年的单身技术 我们懂得",
            "我也是觉得尴尬啊哈哈",
            "自从看了小仙女 对其他的妹子都没啥兴趣了",
            "有那么夸张的吗",
            "这貌似就……",
            "这是铁粉的证明好不好 我就笑笑",
            "老子就是觉得 貌似找不到一个完美的女人 ",
            "要求那么高的吗",
            "眼前不就有一个那么完美的了吗",
            "我个人觉得每天来看一下 就很足够了啊哈哈",
            "只可远观不可亵玩",
            "这么有文化的吗",
            "目测 是爱莲说",
            "这竟然是个那么有文化内涵的直播",
            "同志们 好好学习 天天向上",
            "对的对的",
            "这是什么体验啊哈哈 一遍看小仙女直播 一遍学习",
            "我只能说 这福利太好了吧啊哈哈哈 赶紧办卡",
            "办卡 是基本的好不好",
            "这是交学费的意思吗哈哈哈",
            "还算可以啊哈哈哈",
            "最重要是大家 开心 有一个愉快的时光",
            "有道理",
            "小仙女 没带AK啊",
            "没办法 AK要的材料太多了",
            "我要给仙女 速递一个",
            "啥快递",
            "顺丰吗啊哈哈哈",
            "是爱心速递",
            "肉麻zzz",
            "真爱的节奏啊哈哈哈",
            "貌似 只有我在认真对话啊哈哈哈哈哈哈",
            "兄弟 没事 不关你事 只是他有点痴痴的啊哈哈",
            "日常~~~花痴没办法",
            "厉害厉害 铁粉以上",
            "那个难道就是传说中的 痴粉吗？",
            "凉粉皮er 呢我还",
            "这个流批啊哈哈哈哈",
            "痴粉是我",
            "我们不如实际一点吧",
            "多实际",
            "怎么实际",
            "直接买一个吗",
            "貌似 我家主播的家里就有 哈哈 ",
            "那是必须买的啊哈哈",
            "这不实际",
            "那怎么才叫实际",
            "求教",
            "支个招来看看",
            "那当然是要我们一起 集气 用我们对小仙女的爱 ",
            "这叫实际 我就笑笑",
            "我觉得还可以",
            "我觉得很一般",
            "啊岳不要那么严格啦啊哈哈",
            "其实我是在freestyle",
            "你有freestyle吗？？？？？？",
            "skrrrrr",
            "吴亦凡上身啊哈哈",
            "6666666",
            "兄弟走一波啊哈哈哈",
            "其实中国有嘻哈不是重点",
            "重点是吴亦凡！",
            "热狗肠也不错",
            "皮几万就不可以了",
            "gai头才是潮流哈哈哈",
            "zzz这帮人 真的 重点是中国有有有仙女！！！！",
            "错了 中国有你才对……",
            "国宝……",
            "这都被你想到了啊哈哈",
            "没办法小仙女就是那么迷人啊哈哈",
            "国宝是指小仙女哦~~~",
            "阔以",
            "溜得一批"
    };
}