package jp.alhinc.kadono_setsu.calculate_sales;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CalculateSales {

	public static void main(String[] args) {

		if(args.length != 1){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		BufferedReader br = null;
		FileReader fr = null;

		// 支店定義ファイルのマップ作成
		HashMap<String, String> branchNameMap = new HashMap<String, String>();

		// 店売り上げのマップ作成
		HashMap<String, Long> branchSalesMap = new HashMap<String, Long>();



		// ファイル読み込み処理
		if(!fileRead(args[0],"branch.lst","支店",branchNameMap,branchSalesMap,"^[0-9]{3}$")){
			return;
		}

		// 商品定義マップ作成
		HashMap<String, String> commodityNameMap = new HashMap<String, String>();

		// 商品売り上げのマップ作成
		HashMap<String, Long> commoditySalesMap = new HashMap<String, Long>();



		// ファイル読み込み処理
		if(!fileRead(args[0],"commodity.lst","商品",commodityNameMap,commoditySalesMap,"^[0-9a-zA-Z]{8}")){
			return;
		}

		ArrayList<String> salesName = new ArrayList<String>();

		// ここからrcdかつ八桁名のファイル検索
		File dir = new File(args[0]);
		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String fileName = file.getName();
			if (file.isFile() && fileName.matches("^[0-9]{8}.rcd$")) {

				// 検索ヒットしたファイルの格納
				salesName.add(fileName);
				
			}
		}

		// ここまでファイル検索処理

		// ここから連番チェック処理
		
		
		for (int i = 0; i < salesName.size(); i++) {
			String str = salesName.get(i);
			String[] salesNameCheck = str.split("[.]");
			int salesNameNumber = Integer.parseInt(salesNameCheck[0]);

			if (i + 1 != salesNameNumber) {
				System.out.println("売上ファイル名が連番になっていません");
				return;
			}
		}
		// ここまで連番チェック処理

		// ここからrcdファイル読み込み処理
		for (int i = 0; i < salesName.size(); i++) {
			ArrayList<String> salesOneTime = new ArrayList<String>();

			File file = new File(args[0], salesName.get(i));
			try{
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				String s;
				while ((s = br.readLine()) != null) {
					salesOneTime.add(s);
				}
			}catch(IOException e){
				System.out.println("予期せぬエラーが発生しました");
				return;
			}finally{
				try {
					br.close();
				} catch (IOException e) {
					return;
				}
			}

			if(salesOneTime.size() != 3 ){
				System.out.println(salesName.get(i) + "のフォーマットが不正です");
				return;
			}
			

			if(!salesOneTime.get(2).matches("^[0-9]*$")){
				System.out.println("予期せぬエラーが発生しました");
				return;
			}
			Long salesFileMoney = Long.parseLong(salesOneTime.get(2));
			if (salesFileMoney >= 10000000000L) {
				System.out.println(salesName.get(i) + "のフォーマットが不正です");
				return;
			}



			// ここから読み込んだデータの格納処理。まずは支店データ
			String branchNumber = salesOneTime.get(0);
			if (!(branchSalesMap.containsKey(salesOneTime.get(0)))) {
				// 支店コードがリストになければエラー
				System.out.println(salesName.get(i) + "の支店コードが不正です");
				return;
			}

			// 支店データが存在する場合、合計して代入
			long sumOfBranchSales = branchSalesMap.get(branchNumber)
					+ salesFileMoney;

			// 合計金額が10桁を超えたらエラー
			if (sumOfBranchSales >= 10000000000L) {
				System.out.println("合計金額が10桁を超えました");
				return;
			}

			// 10桁未満なら格納
			branchSalesMap.put(branchNumber, sumOfBranchSales);


			// ここから商品データと売り上げの格納
			String commodityNumber = salesOneTime.get(1);
			if (!commoditySalesMap.containsKey(salesOneTime.get(1))) {
				// 商品コードがリストになければエラー
				System.out.println(salesName.get(i) + "の商品コードが不正です");
				return;
			}
			// 商品データが存在する場合、合計して代入
			long sumOFcommoditySales = commoditySalesMap.get(commodityNumber)
					+ salesFileMoney;

			// 合計金額が10桁を超えたらエラー
			if (sumOFcommoditySales >= 10000000000L) {
				System.out.println("合計金額が10桁を超えました");
				return;
			}

			// 10桁未満なら格納
			commoditySalesMap.put(commodityNumber, sumOFcommoditySales);
		}


		// ここまでRCDファイル読み込み処理

		// 支店出力処理
		if (!fileWright(args[0], "branch.out", branchNameMap, branchSalesMap)) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		}

		// 商品出力処理
		if (!fileWright(args[0], "commodity.out", commodityNameMap, commoditySalesMap)) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
	}


	public static boolean fileRead(String dirPath,String fileName, String fileType,
			HashMap<String,String> nameMap,HashMap<String,Long> salesMap,String nameLimit){

		// 定義ファイル読み込みスタート
		BufferedReader br = null;
		File file = new File(dirPath, fileName);
		if(file.exists() == false){
			System.out.println(fileType +"定義ファイルが存在しません");
			return false;
		}
		try {

			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);

			String s = null;
			while((s = br.readLine()) != null){
				String[] dateSplit = s.split(",");
				if (dateSplit.length == 2 && dateSplit[0].matches(nameLimit)) {
					nameMap.put(dateSplit[0], dateSplit[1]);
					salesMap.put(dateSplit[0], 0l);
				} else {
					System.out.println(fileType + "定義ファイルのフォーマットが不正です");
					return false;
				}
			}
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return false;
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				return false;
			}
		}


		// 定義ファイル読み込み終了
		return true;
	}



	public static boolean fileWright(String dirPath, String fileName, HashMap<String, String> nameMap,
			HashMap<String, Long> salesMap ) {

		// ここからソート処理

		List<Map.Entry<String, Long>> fileNameEntries = new ArrayList<Map.Entry<String, Long>>(salesMap.entrySet());
		Collections.sort(fileNameEntries, new Comparator<Map.Entry<String, Long>>() {

			public int compare(Entry<String, Long> entry1, Entry<String, Long> entry2) {
				return ((Long) entry2.getValue()).compareTo((Long) entry1.getValue());
			}
		});

		// ここまでソート処理
		String sep = System.getProperty("line.separator");
		BufferedWriter bw = null;
		// ここからファイル出力処理
		try {
			File file = new File(dirPath, fileName);
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			for (Entry<String, Long> s : fileNameEntries) {
				bw.write(s.getKey() + "," + nameMap.get(s.getKey()) + "," + s.getValue() + sep);
				// 出力内容
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}finally{
			try {
				if(bw != null)
					bw.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				return false;

			}
		}
		return true;
	}
}