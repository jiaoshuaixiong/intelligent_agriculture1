package com.topwulian.message;

import cn.hutool.http.HttpUtil;
import com.topwulian.constants.WXConstants;
import com.topwulian.utils.SendWxTemplateMessageUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class SendMessageTest {

//
    public static void main(String[] args) {
        //19_0tbP06kJibpKA4szgV8RpNvhBw1G8kvdcFsRoa-yKv_NV4v_IZ3Y56aHmZdltSOUXI7h4zcN52JIkf_AwqvsjVj4c3hw8bBfbPid-6owWDqOWMUmrA0cO9x9RxEAJHLdw_VzUJAHOrOXSK-aJRYcAEABJQ
     // System.out.println(getToken());
//        List<TemplateParam> paras=new ArrayList<TemplateParam>();
//        paras.add(new TemplateParam("first","您好，你有新的任务","#FF3333"));
//        paras.add(new TemplateParam("keyword1","杨振华","#0044BB"));
//        paras.add(new TemplateParam("keyword2","2019-03-13","#0044BB"));
//        paras.add(new TemplateParam("keyword3","除草!!!","#0044BB"));
//        paras.add(new TemplateParam("remark","请尽快处理！","#0044BB"));
//        Template tem = new Template("oXi8I1hrWQctDuJWaF7PO2NJbQEM",paras);
//        boolean result=sendTemplateMsg(tem);
//        String access_token = "19_HoYG1tD0h6nCgYxQe23Ba_0HfqN2X8y5FkTyaytgggpY78ROSazagy_eTi5WA2VgUIT_ZiYc2XGTTIJG_SpbsSAa--MrKl3YtuG-Yiv1gyp7KrPPqUFq1uv3aRt6xTdQzLuLslkE_Cz3gGZgPJKfAIAFZA";
//        Map<String,TemplateParam> map = new HashMap<String,TemplateParam>();
//        map.put("first",new TemplateParam("您好，您有新的任务需要!","#FF3333"));
//        map.put("keyword1",new TemplateParam("您好，你有新的任务","#0044BB"));
//        map.put("keyword2",new TemplateParam("您好，你有新的任务","#0044BB"));
//        map.put("keyword3",new TemplateParam("您好，你有新的任务","#0044BB"));
//        map.put("remark",new TemplateParam("您好，你有新的任务","#FF3333"));
//        SendWxTemplateMessageUtil.sendMessage(map,"oXi8I1hrWQctDuJWaF7PO2NJbQEM",WXConstants.TASK_MESSAGE_ID,access_token);
    }



    public static String getToken(){
       return HttpUtil.get(MessageFormat.format(WXConstants.REQUEST_ACCESS_TOKEN_URL,"wxe4eed8c67a42c5f0","b2a931eb3daa11c3d6b872043b71fb79"));
       // return "19_EComtDVjVdmpSk9NI0fvtJ1CdqrneWewbmxQwihqCVIGU-2gUyx_db6Q1sxyXBcTj1lpQXJeNJExc0HUJh47SYOvePyNpSYyZVU28E7-j276pm6RbaoPDa41TG02h7KG5nfTS3noDPvM17gZQCGaAHAMYC";


     //   Connection connection = DriverManager.getConnection();
    }


//    public static void main(String[] args) throws Exception {
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:mysql:///test", "root", "994397");
//        conn.prepareStatement("");
//    }

}


