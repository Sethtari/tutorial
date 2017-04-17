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

public class calculate_sales {

	public static void main(String[] args) {

		BufferedReader br = null;
		FileReader fr = null;

		HashMap<String, String> branchDateMap = new HashMap<String, String>();
		// 支店定義ファイルのマップ作成

		HashMap<String, Long> branchSalesDateMap = new HashMap<String, Long>();
		// 店売り上げのマップ作成
		// ReadFile(branch);
		ArrayList<String> branchOneTime = new ArrayList<String>();
		// 支店定義ファイル読み込み一時リスト作成

		// 支店定義ファイル読み込みスタート


		if(fileRead(args[0],"branch",branchOneTime,branchDateMap,branchSalesDateMap)){
		}else{
			return;

		}

		/*		try {
			File file = new File(args[0], "branch.lst");
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String s;
			while ((s = br.readLine()) != null) {
				branchOneTime.add(s);
			}
		} catch (IOException e) {
			System.out.println("支店定義ファイルが存在しません");
			System.exit(1);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		// 格納後の支店定義ファイルをマップへ
		for (int i = 0; i < branchOneTime.size(); i++) {
			String bot = branchOneTime.get(i);

			String[] branchDateSplit = bot.split(",");

			if (branchDateSplit.length == 2 && branchDateSplit[1].matches("^[^\n\r]+$")
					&& branchDateSplit[0].matches("^[0-9]{3}$")) {
				branchDateMap.put(branchDateSplit[0], branchDateSplit[1]);
				branchSalesDateMap.put(branchDateSplit[0], 0l);
			} else {
				System.out.println("支店定義ファイルのフォーマットが不正です");
				System.exit(1);
			}
		}

		// 支店定義ファイル読み込み終了
		 * */

		HashMap<String, String> commodityDateMap = new HashMap<String, String>();
		// 商品定義マップ作成

		HashMap<String, Long> commoditySalesDateMap = new HashMap<String, Long>();
		// 商品売り上げのマップ作成

		ArrayList<String> commodityOneTime = new ArrayList<String>();
		// 商品定義ファイル一時保存リスト作成

		if(fileRead(args[0],"commodity",commodityOneTime,commodityDateMap,commoditySalesDateMap)){
			System.out.println("商品定義ファイルが存在しません");
		}
		// 商品定義ファイル読み込みスタート

		/*		try {

			File file = new File(args[0], "commodity.lst");
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String s;
			while ((s = br.readLine()) != null) {
				commodityOneTime.add(s);
			}
		} catch (IOException e) {
			System.out.println("商品定義ファイルが存在しません");
			System.exit(1);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		// 格納後の商品定義ファイルをマップへ

		for (int i = 0; i < commodityOneTime.size(); i++) {
			String cot = commodityOneTime.get(i);
			String[] commodityDateSplit = cot.split(",");
			if (commodityDateSplit.length == 2 && commodityDateSplit[1].matches("^[^\n\r]+$")
					&& commodityDateSplit[0].matches("^[0-9a-zA-Z]{8}")) {
				commodityDateMap.put(commodityDateSplit[0], commodityDateSplit[1]);
				commoditySalesDateMap.put(commodityDateSplit[0], 0l);
			} else {
				System.out.println("商品定義ファイルのフォーマットが不正です");
				System.exit(1);
			}
		}

		// 商品定義ファイル処理終了
		 */


		ArrayList<String> salesName = new ArrayList<String>();

		// ここからrcdかつ八桁名のファイル検索
		File dir = new File(args[0]);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String fileName = file.getName().toString();
			// ↓ここにfile.isFileの条件を入れるとフォルダを除ける
			if (file.isFile() && fileName.matches("^[0-9]{8}.rcd$")) {

				// 検索ヒットしたファイルの格納
				salesName.add(fileName);
			} else {
			}
		}

		// ここまでファイル検索処理

		// ここから連番チェック処理
		for (int i = 0; i < salesName.size(); i++) {
			String str = salesName.get(i);
			String[] salesNameCheck = str.split("[.]");
			int salesNameNumber = Integer.parseInt(salesNameCheck[0]);

			if (i + 1 != salesNameNumber) {
				System.out.println("売り上げファイル名が連番になっていません");
				System.exit(1);
			}
		}

		// ここまで連番チェック処理

		// ここからRCDファイル読み込み処理
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
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}

			if (salesOneTime.size() == 3 && (salesOneTime.get(0)).matches("^[0-9]{3}$")
					&& (salesOneTime.get(1)).matches("^[0-9a-zA-Z]{8}$")
					&& Long.parseLong(salesOneTime.get(2)) <= 10000000000l) {

				// ここから読み込んだデータの格納処理。まずは支店データ
				String branchNumber = salesOneTime.get(0);
				if (branchDateMap.get(branchNumber) == null) {
					// 支店コードがリストになければエラー
					System.out.println(salesName.get(i) + "の支店コードが不正です");

					return;
				} else {
					// 支店データが存在する場合、合計して代入
					long sumOfBranchSales = branchSalesDateMap.get(branchNumber)
							+ Long.parseLong(salesOneTime.get(2));

					// 合計金額が10桁を超えたらエラー
					if (sumOfBranchSales >= 10000000000l) {
						System.out.println("合計金額が10桁を超えました");
						System.exit(1);
					}

					// 10桁未満なら格納
					branchSalesDateMap.put(branchNumber, sumOfBranchSales);
				}

				// ここから商品データと売り上げの格納
				String commodityNumber = salesOneTime.get(1);
				if (commodityDateMap.get(commodityNumber) == null) {
					// 商品コードがリストになければエラー
					System.out.println(salesName.get(i) + "の商品コードが不正です");
					System.exit(1);
				} else {

					// 商品データが存在する場合、合計して代入
					long sumOFcommoditySales = commoditySalesDateMap.get(commodityNumber)
							+ Long.parseLong(salesOneTime.get(2));

					// 合計金額が10桁を超えたらエラー
					if (sumOFcommoditySales >= 10000000000l) {
						System.out.println("合計金額が10桁を超えました");
						System.exit(1);
					}

					// 10桁未満なら格納
					commoditySalesDateMap.put(commodityNumber, sumOFcommoditySales);
				}

			} else {
				System.out.println(salesName.get(i) + "のフォーマットが不正です");

				return;
			}


			// ここまでRCDファイル読み込み処理

			// 支店出力処理
			if (fileWright(args[0], "branch", branchDateMap, branchSalesDateMap)) {
			} else {
				System.out.println("予期せぬエラーが発生しました");
				return;
			}

			// 商品出力処理
			if (fileWright(args[0], "commodity", commodityDateMap, commoditySalesDateMap)) {
			} else {
				System.out.println("予期せぬエラーが発生しました");
				return;
			}
		}

	}

	public static boolean fileRead(String dirPath,String fileName, ArrayList<String> oneTime,HashMap<String,String> nameMap,HashMap<String,Long> salesMap){

		// 定義ファイル読み込みスタート
		BufferedReader br = null;
		try {
			File file = new File(dirPath, fileName + ".lst");
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);

			String s;
			while ((s = br.readLine()) != null) {
				oneTime.add(s);
			}
		} catch (IOException e) {
			if(fileName == "branch"){
			System.out.println("支店定義ファイルが存在しません");
			return false;
			}else{
			System.out.println("商品定義ファイルが存在しません");
			return false;
			}
			
		} finally {
			try {
				br.close();
			} catch (IOException e) {

			}
		}

		// 格納後の定義ファイルをマップへ
		for (int i = 0; i < oneTime.size(); i++) {
			String bot = oneTime.get(i);

			String[] DateSplit = bot.split(",");
			if(fileName == "branch"){
				if (DateSplit.length == 2 && DateSplit[1].matches("^[^\n\r]+$")
						&& DateSplit[0].matches("^[0-9]{3}$")) {
					nameMap.put(DateSplit[0], DateSplit[1]);
					salesMap.put(DateSplit[0], 0l);
				} else {
					return false; //"支店定義フォーマット不正";
				}
			}else{
				if (DateSplit.length == 2 && DateSplit[1].matches("^[^\n\r]+$")
						&& DateSplit[0].matches("^[0-9a-zA-Z]{8}")) {
					nameMap.put(DateSplit[0], DateSplit[1]);
					salesMap.put(DateSplit[0], 0l);
				} else {
					return false; //"商品定義フォーマット不正";
				}
				}
				
			}


			// 定義ファイル読み込み終了


			return true;
		}



		public static boolean fileWright(String dirPath, String fileName, HashMap<String, String> nameMap,
				HashMap<String, Long> salesMap) {

			// ここからソート処理

			List<Map.Entry<String, Long>> fileNameEntries = new ArrayList<Map.Entry<String, Long>>(salesMap.entrySet());
			Collections.sort(fileNameEntries, new Comparator<Map.Entry<String, Long>>() {

				public int compare(Entry<String, Long> entry1, Entry<String, Long> entry2) {
					return ((Long) entry2.getValue()).compareTo((Long) entry1.getValue());
				}
			});

			// ここまでソート処理

			// ここからファイル出力処理
			try {
				File file = new File(dirPath, fileName + ".out");
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				for (Entry<String, Long> s : fileNameEntries) {
					bw.write(s.getKey() + "," + nameMap.get(s.getKey()) + "," + s.getValue() + "\r\n");
					// 出力内容
				}

				bw.close();
			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
			// ここまでファイル出力処理
			return true;
		}
	}