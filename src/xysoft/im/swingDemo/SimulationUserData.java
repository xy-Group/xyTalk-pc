package xysoft.im.swingDemo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xysoft.im.app.Launcher;
import xysoft.im.db.model.ContactsUser;
import xysoft.im.panels.RightPanel;
import xysoft.im.utils.JsonUtil;


public class SimulationUserData {
	
	String[] sName = new String[95];
	String[] Name = new String[79];

	public SimulationUserData() {
		sName[0] = "白|bai";
		sName[1] = "白|bai";
		sName[2] = "蔡|cai";
		sName[3] = "曹|cao";
		sName[4] = "陈|chen";
		sName[5] = "戴|dai";
		sName[6] = "窦|dou";
		sName[7] = "邓|deng";
		sName[8] = "狄|di";
		sName[9] = "杜|du";
		sName[10] = "段|duan";
		sName[11] = "范|fan";
		sName[12] = "樊|fan";
		sName[13] = "房|fang";
		sName[14] = "风|feng";
		sName[15] = "符|fu";
		sName[16] = "福|fu";
		sName[17] = "高|gao";
		sName[18] = "古|gu";
		sName[19] = "关|guan";
		sName[20] = "郭|guo";
		sName[21] = "毛|mao";
		sName[22] = "韩|han";
		sName[23] = "胡|hu";
		sName[24] = "花|hua";
		sName[25] = "洪|hong";
		sName[26] = "侯|hou";
		sName[27] = "黄|huang";
		sName[28] = "贾|jia";
		sName[29] = "蒋|jiang";
		sName[30] = "金|jin";
		sName[31] = "廖|liao";
		sName[32] = "梁|liang";
		sName[33] = "李|li";
		sName[34] = "林|lin";
		sName[35] = "刘|liu";
		sName[36] = "龙|long";
		sName[37] = "陆|lu";
		sName[38] = "卢|lu";
		sName[39] = "罗|luo";
		sName[40] = "马|ma";
		sName[41] = "牛|niu";
		sName[42] = "庞|pang";
		sName[43] = "裴|pei";
		sName[44] = "彭|peng";
		sName[45] = "戚|qi";
		sName[46] = "齐|qi";
		sName[47] = "钱|qian";
		sName[48] = "乔|qiao";
		sName[49] = "秦|qin";
		sName[50] = "邱|qiu";
		sName[51] = "裘|qiu";
		sName[52] = "仇|qiu";
		sName[53] = "沙|sha";
		sName[54] = "商|shang";
		sName[55] = "尚|shang";
		sName[56] = "邵|shao";
		sName[57] = "沈|shen";
		sName[58] = "师|shi";
		sName[59] = "施|shi";
		sName[60] = "宋|song";
		sName[61] = "孙|sun";
		sName[62] = "童|tong";
		sName[63] = "万|wan";
		sName[64] = "王|wang";
		sName[65] = "魏|wei";
		sName[66] = "卫|wei";
		sName[67] = "吴|wu";
		sName[68] = "武|wu";
		sName[69] = "萧|xiao";
		sName[70] = "肖|xiao";
		sName[71] = "项|xiang";
		sName[72] = "许|xu";
		sName[73] = "徐|xu";
		sName[74] = "薛|xue";
		sName[75] = "杨|yang";
		sName[76] = "羊|yang";
		sName[77] = "阳|yang";
		sName[78] = "易|yi";
		sName[79] = "尹|yin";
		sName[80] = "俞|yu";
		sName[81] = "赵|zhao";
		sName[82] = "钟|zhong";
		sName[83] = "周|zhou";
		sName[84] = "郑|zheng";
		sName[85] = "朱|zhu";
		sName[86] = "东方|dongfang";
		sName[87] = "独孤|dugu";
		sName[88] = "慕容|murong";
		sName[89] = "欧阳|ouyang";
		sName[90] = "司马|sima";
		sName[91] = "西门|ximen";
		sName[92] = "尉迟|yuchi";
		sName[93] = "长孙|zhangsun";
		sName[94] = "诸葛|zhuge";

		Name[0] = "ai|皑艾哀";
		Name[1] = "an|安黯谙";
		Name[2] = "ao|奥傲敖骜翱";
		Name[3] = "ang|昂盎";
		Name[4] = "ba|罢霸";
		Name[5] = "bai|白佰";
		Name[6] = "ban|斑般";
		Name[7] = "bang|邦";
		Name[8] = "bei|北倍贝备";
		Name[9] = "biao|表标彪飚飙";
		Name[10] = "bian|边卞弁忭";
		Name[11] = "bu|步不";
		Name[12] = "cao|曹草操漕";
		Name[13] = "cang|苍仓";
		Name[14] = "chang|常长昌敞玚";
		Name[15] = "chi|迟持池赤尺驰炽";
		Name[16] = "ci|此次词茨辞慈";
		Name[17] = "du|独都";
		Name[18] = "dong|东侗";
		Name[19] = "dou|都";
		Name[20] = "fa|发乏珐";
		Name[21] = "fan|范凡反泛帆蕃";
		Name[22] = "fang|方访邡昉";
		Name[23] = "feng|风凤封丰奉枫峰锋";
		Name[24] = "fu|夫符弗芙";
		Name[25] = "gao|高皋郜镐";
		Name[26] = "hong|洪红宏鸿虹泓弘";
		Name[27] = "hu|虎忽湖护乎祜浒怙";
		Name[28] = "hua|化花华骅桦";
		Name[29] = "hao|号浩皓蒿浩昊灏淏";
		Name[30] = "ji|积极济技击疾及基集记纪季继吉计冀祭际籍绩忌寂霁稷玑芨蓟戢佶奇诘笈畿犄";
		Name[31] = "jian|渐剑见建间柬坚俭";
		Name[32] = "kan|刊戡";
		Name[33] = "ke|可克科刻珂恪溘牁";
		Name[34] = "lang|朗浪廊琅阆莨";
		Name[35] = "li|历离里理利立力丽礼黎栗荔沥栎璃";
		Name[36] = "lin|临霖林琳";
		Name[37] = "ma|马";
		Name[38] = "mao|贸冒貌冒懋矛卯瑁";
		Name[39] = "miao|淼渺邈";
		Name[40] = "nan|楠南";
		Name[41] = "pian|片翩";
		Name[42] = "qian|潜谦倩茜乾虔千";
		Name[43] = "qiang|强羌锖玱";
		Name[44] = "qin|亲琴钦沁芩矜";
		Name[45] = "qing|清庆卿晴";
		Name[46] = "ran|冉然染燃";
		Name[47] = "ren|仁刃壬仞";
		Name[48] = "sha|沙煞";
		Name[49] = "shang|上裳商";
		Name[50] = "shen|深审神申慎参莘";
		Name[51] = "shi|师史石时十世士诗始示适炻";
		Name[52] = "shui|水";
		Name[53] = "si|思斯丝司祀嗣巳";
		Name[54] = "song|松颂诵";
		Name[55] = "tang|堂唐棠瑭";
		Name[56] = "tong|统通同童彤仝";
		Name[57] = "tian|天田忝";
		Name[58] = "wan|万宛晚";
		Name[59] = "wei|卫微伟维威韦纬炜惟玮为";
		Name[60] = "wu|吴物务武午五巫邬兀毋戊";
		Name[61] = "xi|西席锡洗夕兮熹惜";
		Name[62] = "xiao|潇萧笑晓肖霄骁校";
		Name[63] = "xiong|熊雄";
		Name[64] = "yang|羊洋阳漾央秧炀飏鸯";
		Name[65] = "yi|易意依亦伊夷倚毅义宜仪艺译翼逸忆怡熠沂颐奕弈懿翊轶屹猗翌";
		Name[66] = "yin|隐因引银音寅吟胤訚烟荫";
		Name[67] = "ying|映英影颖瑛应莹郢鹰";
		Name[68] = "you|幽悠右忧猷酉";
		Name[69] = "yu|渔郁寓于余玉雨语预羽舆育宇禹域誉瑜屿御渝毓虞禺豫裕钰煜聿";
		Name[70] = "zhi|制至值知质致智志直治执止置芝旨峙芷挚郅炙雉帜";
		Name[71] = "zhong|中忠钟衷";
		Name[72] = "zhou|周州舟胄繇昼";
		Name[73] = "zhu|竹主驻足朱祝诸珠著竺";
		Name[74] = "zhuo|卓灼灼拙琢濯斫擢焯酌";
		Name[75] = "zi|子资兹紫姿孜梓秭";
		Name[76] = "zong|宗枞";
		Name[77] = "zu|足族祖卒";
		Name[78] = "zuo|作左佐笮凿";
	}

	public String getName() {
		Random random = new Random();
		int i = random.nextInt(94);
		String mName = sName[i].split("[|]")[0];
		mName += getNameKey();
		if (random.nextInt(2) == 1) {
			mName += getNameKey();
		}
		return mName;
	}

	public String getNameP() {
		Random random = new Random();
		int i = random.nextInt(94);
		String mName = sName[i].split("[|]")[1];
		mName += getNameKeyP();
		if (random.nextInt(2) == 1) {
			mName += getNameKeyP();
		}
		return mName;
	}

	private String getNameKey() {
		Random random = new Random();
		int i = random.nextInt(78);
		return getKey(Name[i].split("[|]")[1]);
	}

	private String getNameKeyP() {
		Random random = new Random();
		int i = random.nextInt(78);
		return getKey(Name[i].split("[|]")[0]);
	}

	private String getKey(String name) {
		if (name == null)
			return null;
		int counti = name.length();
		Random random = new Random();
		int i = random.nextInt(counti);
		return name.substring(i, i + 1);
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
	
	public static JSONArray readJsonsFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			String jsonText = readAll(rd);
			// System.out.println(jsonText);
			JSONArray jsons = new JSONArray(jsonText);
			return jsons;
		} finally {
			is.close();
		}
	}
	
	public static String readJsonFile(String filename) throws IOException {
	    try (InputStream is = new FileInputStream(filename)) {
	        return IOUtils.toString(is, StandardCharsets.UTF_8);
	    }    
	}

	public static void main(String[] args) {

//		randomUserMake();
		
		syncAllUser();

	}

	public static void randomUserMake() {
		SimulationUserData chineseName = new SimulationUserData();
		for (int i = 0; i < 5001; i++) {
			ContactsUser user = new ContactsUser();
			user.setMail(chineseName.getNameP() + "@email.com");
			user.setName(chineseName.getName());
			user.setPhone("1370000" + i);
			user.setUserId(chineseName.getNameP()+i);
			user.setUsername(chineseName.getNameP()+i);
			user.setSp(chineseName.getNameP());
			Launcher.contactsUserService.insert(user);
		}
	}

	
//	public static void main(String[] args) {
//		String[] chineseArr = new String[2001];
//		String[] pinyinArr = new String[2001];
//		SimulationUserData chineseName = new SimulationUserData();
//		for (int i = 0; i < 2001; i++) {
//			chineseArr[i] = chineseName.getName();
//			pinyinArr[i] = chineseName.getNameP();
//
//		}
//
//		JSONArray jsonArray = new JSONArray();
//		for (int i = 0; i < 21; i++) {
//			JSONObject obj = new JSONObject();
//
//			obj.put("u", new String("Test" + i));// username
//			obj.put("n", chineseArr[i]);// name
//			obj.put("p", "1370000" + i);// phone
//			obj.put("e", pinyinArr[i] + "@email.com");// email
//			obj.put("l", "");// 位置
//			obj.put("d", "总部>>广州营销中心>>一部");// 部门
//			obj.put("s", "abc"); // 缩拼
//
//			jsonArray.put(obj);
//
//		}
//		
//		long start200 = System.currentTimeMillis();
//
//		JSONArray jsons;
//		try {
//			jsons = readJsonsFromUrl("http://111.230.157.216/user200.html");
//			//查询3个项目
//		    System.out.println(jsons.get(0));
//		    System.out.println(jsons.get(100));
//		    System.out.println(jsons.get(199));
//		} catch (JSONException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		long time200 = System.currentTimeMillis() - start200  ;
//	    System.out.println("200个对象查询耗时（毫秒）："+time200 );
//		
//
//		long start2000 = System.currentTimeMillis();
//
//		JSONArray jsons2000;
//		try {
//			jsons2000 = readJsonsFromUrl("http://111.230.157.216/allusers.html");
//			//查询3个项目
//		    System.out.println(jsons2000.get(0));
//		    System.out.println(jsons2000.get(1000));
//		    System.out.println(jsons2000.get(1999));
//		} catch (JSONException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		long time2000 = System.currentTimeMillis() - start2000  ;
//	    System.out.println("2000个对象查询耗时（毫秒）："+time2000 );
//
//
//	}

	private static void syncAllUser() {
		// TODO Auto-generated method stub
    	Launcher.contactsUserService.deleteAll();

    	long start200 = System.currentTimeMillis();

		JSONArray jsons;
		try {
			jsons = JsonUtil.readJsonsFromUrl("http://111.230.157.216/200jsonUTF8.txt");
			int count = jsons.length();
			double step = count/100;
			int i = 0;

			Iterator<Object> it = jsons.iterator();
            List<ContactsUser> list=new ArrayList<ContactsUser>();
            while (it.hasNext()) {
                JSONObject ob = (JSONObject) it.next();
                ContactsUser model = null;
                if(ob.getString("n")!=null){
                    model=new ContactsUser();
                    model.setName(ob.getString("n"));
                }
                if(ob.getString("u")!=null){
                    model.setUsername(ob.getString("u"));
                    model.setUserId(ob.getString("u"));
                }
                if(ob.getString("p")!=null){
                    model.setPhone(ob.getString("p"));
                }
                if(ob.getString("s")!=null){
                    model.setSp(ob.getString("s"));
                }
                if(ob.getString("d")!=null){
                    model.setDept(ob.getString("d"));
                }
                if(ob.getString("e")!=null){
                    model.setMail(ob.getString("e"));
                }
                if(ob.getString("l")!=null){
                    model.setLocation(ob.getString("l"));
                }
                i++;
                if(model!=null){
                    list.add(model);
                    Launcher.contactsUserService.insert(model);

                }

            }			 

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		long time200 = System.currentTimeMillis() - start200  ;
	    System.out.println("200个对象查询耗时（毫秒）："+time200 );


	}
	
}
