import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class test {
	public static void main(String[] args){


		
		HashMap<String,String> BranchDateMap = new HashMap<String,String>();
	//支店定義ファイルのマップ作成
		
	//支店定義ファイル読み込みスタート
			try{
				File file = new File(args[0],"\\branch.lst");
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);

				String s;
				while((s = br.readLine()) != null){

					
					String str = s	;
					String[] BranchDateSplit = str.split(",",0);

					if(BranchDateSplit[0].length() != 3){   //エラーを投げる
						System.out.println("支店定義ファイルのフォーマットが不正です。");
						return;
					}else{
						BranchDateMap.put(BranchDateSplit[0],BranchDateSplit[1]);
					}
				}
				br.close();
		}catch(IOException e){
			System.out.println("支店定義ファイルが存在しません");
		}

	//支店定義ファイル読み込み終了

			HashMap<String,String> CommodityDateMap = new HashMap<String,String>();
			//支店定義マップ作成
			
	//商品定義ファイル読み込みスタート
			try{
				File file = new File(args[0],"\\commodity.lst");
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);

				String s;
				while((s = br.readLine()) != null){
					
					String str = s	;
					String[] CommodityDateSplit = str.split(",",0);

					if(CommodityDateSplit[0].length() != 8){   //エラーを投げる
						System.out.println("商品定義ファイルのフォーマットが不正です。");
						return;
					}else{
						CommodityDateMap.put(CommodityDateSplit[0],CommodityDateSplit[1]);
					}
				}
				br.close();
		}catch(IOException e){
			System.out.println("商品定義ファイルが存在しません");
		}
	//商品定義ファイル読み込み終了
//System.out.println(BranchDateMap.entrySet());
//System.out.println(CommodityDateMap.entrySet());

		HashMap<String,Integer> RCDBranchDateMap = new HashMap<String,Integer>();
		//店売り上げのマップ作成

		HashMap<String,Integer> RCDCommodityDateMap = new HashMap<String,Integer>();
		//商品売り上げのマップ作成

		String[] RCDDateSplit = new String[3];
		
		ArrayList<String> RCDName = new ArrayList<String>();
		
		//rcdかつ八桁名のファイル検索
			File dir = new File(args[0]);
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				String fileName = file.getName().toString();
				if(fileName.endsWith("rcd")){
					//trueの場合データをマップに格納する
					RCDName.add(fileName);;
				}else{
				}
			}
			
		//ここからRCDファイル読み込み処理
			try{
				for(int i = 0; i < RCDName.size(); i++){
				File file = new File(args[0],RCDName.get(i));
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);

				String s;
				int n = 0;
				while((s = br.readLine()) != null){
			
					String str = s	;
					RCDDateSplit[n] = s;
					n++;
					}
				
		//ここから読み込んだデータの格納処理。まずは支店データ
					String x =RCDDateSplit[0];
					if(RCDBranchDateMap.get(x) == null){
						//先にデータがなければそのまま代入
						RCDBranchDateMap.put(RCDDateSplit[0],Integer.parseInt(RCDDateSplit[2]));
					}else if(RCDBranchDateMap.get(x) != null){
						//支店データが存在する場合、合計して代入
						int y = RCDBranchDateMap.get(x) + Integer.parseInt(RCDDateSplit[2]);
						RCDBranchDateMap.put(RCDDateSplit[0],y);
					}
					
		//次は商品データと売り上げの格納
					String a =RCDDateSplit[1];
					if(RCDCommodityDateMap.get(a) == null){
						//先にデータがなければそのまま代入
						RCDCommodityDateMap.put(RCDDateSplit[1],Integer.parseInt(RCDDateSplit[2]));
					}else if(RCDCommodityDateMap.get(a) != null){
						//支店データが存在する場合、合計して代入
						int b = RCDCommodityDateMap.get(a) + Integer.parseInt(RCDDateSplit[2]);
						RCDCommodityDateMap.put(RCDDateSplit[1],b);
					}

				br.close();
				}
		}catch(IOException e){
			System.out.println("読み込み処理失敗");
		}
		//ここまでRCDファイル読み込み処理

			
			
		//ここにソート処理を入れる
			
			
			
		//ここから支店別集計ファイル出力処理
		try{
			File file = new File(args[0],"branch.out");
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i = 0; i < BranchDateMap.size() ; i++){
				List<String> BranchAllKeyList = new ArrayList<String>(BranchDateMap.keySet());

				bw.write(BranchAllKeyList.get(i)+","+BranchDateMap.get(BranchAllKeyList.get(i))+","+RCDBranchDateMap.get(BranchAllKeyList.get(i))+"\r\n");	
				//出力内容
			}
			
			
			bw.close();
		}catch(IOException e){
			System.out.println(e);
		}
		//ここまで支店別集計ファイル出力処理
		
		
		

		//ここから商品別集計ファイル出力処理
		try{
			File file = new File(args[0],"commodity.out");
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i = 0; i < CommodityDateMap.size() ; i++){
				List<String> CommodityAllKeyList = new ArrayList<String>(CommodityDateMap.keySet());

				bw.write(CommodityAllKeyList.get(i)+","+CommodityDateMap.get(CommodityAllKeyList.get(i))+","+RCDCommodityDateMap.get(CommodityAllKeyList.get(i))+"\r\n");	
				//出力内容
			}
			
			
			bw.close();
		}catch(IOException e){
			System.out.println(e);
		}
			//ここまで支店別集計ファイル出力処理
		
		

		}
		

















	//System.out.println("予期せぬエラーが発生しました。");
	//return;
		}