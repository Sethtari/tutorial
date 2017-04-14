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
/*
 
 メソッド処理の途中
class ReadFile {

static String dateSpace; // ディレクトリ
BufferedReader br = null;
FileReader fr = null;
String fileType; // branchかcommodityか
String ArrayName = (fileType + "oneTime"); // ArrayList作成の際の名前
String fileType1 = "支店";
String fileType2 = "商品";

	String ReadFile(String fileType) {

		ArrayList<String> ArrayName = new ArrayList<String>();
		return try {
			File file = new File(dateSpace, fileType + ".lst");
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String s;
			while ((s = br.readLine()) != null) {
				ArrayName.add(s);
			}
			br.close();
		} catch (IOException e) {
			if (fileType == "branch") {
				System.out.println(fileType1 + "定義ファイルが存在しません");
		} else if (fileType == "commodity") {
			System.out.println(fileType2 + "定義ファイルが存在しません");
		} else {

		}
			System.exit(1);
		} finally {

		};
	}

} */

public class calculate_sales {
	public static void main(String[] args) {

		try {
			// String dateSpace = args[0];
			BufferedReader br = null;
			FileReader fr = null;

			HashMap<String, String> branchDateMap = new HashMap<String, String>();
			// 支店定義ファイルのマップ作成
			ArrayList<String> branchOneTime = new ArrayList<String>();

			// 支店定義ファイル読み込みスタート

/*			
			//この辺をメソッド処理入力欄に
			 String fileType = "branch";
			 ReadFile branch = new ReadFile();
*/
			
			try {
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
				br.close();
			}

			// 格納後の支店定義ファイルをマップへ
			for (int i = 0; i < branchOneTime.size(); i++) {
				String bot = branchOneTime.get(i);

				String[] branchDateSplit = bot.split(",", 0);

				if (branchOneTime.size() % 2 == 0 && branchDateSplit[1].matches("^[^\n\r]+$")
						&& branchDateSplit[0].matches("^[0-9]{3}$")) {
					branchDateMap.put(branchDateSplit[0], branchDateSplit[1]);
				} else {
					System.out.println("支店定義ファイルのフォーマットが不正です");
					System.exit(1);
				}
			}

			// 支店定義ファイル読み込み終了

			HashMap<String, String> commodityDateMap = new HashMap<String, String>();
			// 商品定義マップ作成
			ArrayList<String> commodityOneTime = new ArrayList<String>();

			// 商品定義ファイル読み込みスタート
			try {

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
				br.close();
			}

			// 格納後の商品定義ファイルをマップへ

			for (int i = 0; i < commodityOneTime.size(); i++) {
				String cot = commodityOneTime.get(i);
				String[] commodityDateSplit = cot.split(",", 0);
				if (commodityOneTime.size() % 2 == 0 && commodityDateSplit[1].matches("^[^\n\r]+$")
						&& commodityDateSplit[0].matches("^[0-9a-zA-Z]{8}")) {
					commodityDateMap.put(commodityDateSplit[0], commodityDateSplit[1]);
				} else {
					System.out.println("商品定義ファイルのフォーマットが不正です");
					System.exit(1);
				}
			}

			// 商品定義ファイル処理終了

			HashMap<String, Long> branchSalesDateMap = new HashMap<String, Long>();
			// 店売り上げのマップ作成

			HashMap<String, Long> commoditySalesDateMap = new HashMap<String, Long>();
			// 商品売り上げのマップ作成

			ArrayList<String> salesName = new ArrayList<String>();

			// ここからrcdかつ八桁名のファイル検索
			File dir = new File(args[0]);
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				String fileName = file.getName().toString();
				//↓ここにfile.isFileの条件を入れるとフォルダを除ける
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
				String[] salesNameCheck = str.split("[.]", 0);
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
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				String s;
				while ((s = br.readLine()) != null) {
					salesOneTime.add(s);
				}
				br.close();

				if (salesOneTime.size() == 3 && (salesOneTime.get(0)).matches("^[0-9]{3}$")
						&& (salesOneTime.get(1)).matches("^[0-9a-zA-Z]{8}$")
						&& Long.parseLong(salesOneTime.get(2)) <= 10000000000l) {

					// ここから読み込んだデータの格納処理。まずは支店データ
					String branchNumber = salesOneTime.get(0);
					if (branchDateMap.get(branchNumber) == null) {
						// 支店コードがリストになければエラー
						System.out.println(salesName.get(i) + "の支店コードが不正です");
						br.close();
						System.exit(1);
					} else if (branchSalesDateMap.get(branchNumber) == null) {
						branchSalesDateMap.put(branchNumber, Long.parseLong(salesOneTime.get(2)));

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
					} else if (commoditySalesDateMap.get(commodityNumber) == null) {

						// 先にデータがなければそのまま代入
						commoditySalesDateMap.put(commodityNumber, Long.parseLong(salesOneTime.get(2)));
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
					br.close();
					System.exit(1);
				}

			}
			// ここまでRCDファイル読み込み処理

			// ここから支店ソート処理
			List<Map.Entry<String, Long>> branchEntries = new ArrayList<Map.Entry<String, Long>>(
					branchSalesDateMap.entrySet());
			Collections.sort(branchEntries, new Comparator<Map.Entry<String, Long>>() {

				public int compare(Entry<String, Long> branchEntry1, Entry<String, Long> branchEntry2) {
					return ((Long) branchEntry2.getValue()).compareTo((Long) branchEntry1.getValue());
				}
			});
			// ここまで支店ソート処理

			// ここから商品ソート処理
			List<Map.Entry<String, Long>> commodityEntries = new ArrayList<Map.Entry<String, Long>>(
					commoditySalesDateMap.entrySet());
			Collections.sort(commodityEntries, new Comparator<Map.Entry<String, Long>>() {

				public int compare(Entry<String, Long> commodityEntry1, Entry<String, Long> commodityEntry2) {
					return ((Long) commodityEntry2.getValue()).compareTo((Long) commodityEntry1.getValue());
				}
			});
			// ここまで商品ソート処理

			// ここから支店別集計ファイル出力処理
			try {
				File file = new File(args[0], "branch.out");
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				for (Entry<String, Long> s : branchEntries) {
					bw.write(s.getKey() + "," + branchDateMap.get(s.getKey()) + "," + branchSalesDateMap.get(s.getKey())
							+ "\r\n");
					// 出力内容
				}

				bw.close();
			} catch (FileNotFoundException e) {
				System.out.println("予期せぬエラーが発生しました");
				System.exit(1);
			}
			// ここまで支店別集計ファイル出力処理

			// ここから商品別集計ファイル出力処理
			try {
				File file = new File(args[0], "commodity.out");
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				for (Entry<String, Long> s : commodityEntries) {
					bw.write(s.getKey() + "," + commodityDateMap.get(s.getKey()) + ","
							+ commoditySalesDateMap.get(s.getKey()) + "\r\n");
					// 出力内容
				}

				bw.close();
			} catch (FileNotFoundException e) {
				System.out.println("予期せぬエラーが発生しました");
				System.exit(1);
			}
			// ここまで支店別集計ファイル出力処理

		} catch (Exception e) {
			System.out.println("予期せぬエラーが発生しました");
			System.exit(1);
		} finally {
			System.exit(0);
		}
	}
}