package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		//	指定URLへ遷移
		webDriver.get("http://localhost:8080/lms/");

		// タイトルの検証
		assertEquals("ログイン | LMS", webDriver.getTitle());

		// 見出しの検証
		assertEquals("ログイン", webDriver.findElement(By.tagName("h2")).getText());

		// ログインIDフォームの確認
		assertTrue(webDriver.findElement(By.id("loginId")).isDisplayed());

		// パスワードフォームの確認
		assertTrue(webDriver.findElement(By.id("password")).isDisplayed());

		// スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		//	ログインID入力
		webDriver.findElement(By.id("loginId")).clear();
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA01");

		//	パスワード入力
		webDriver.findElement(By.id("password")).clear();
		webDriver.findElement(By.id("password")).sendKeys("ItTest2023");

		//	ログインボタンを押下
		webDriver.findElement(By.className("btn-primary")).click();

		// ページが更新されるまで待機
		visibilityTimeout(By.id("open-all-panel"), 10);

		// コース詳細画面に遷移しているかURL確認
		assertTrue(webDriver.getCurrentUrl().startsWith("http://localhost:8080/lms/course/detail"));

		// タイトルの検証
		assertEquals("コース詳細 | LMS", webDriver.getTitle());

		// スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		//	2022年10月2日(日)のステータスが「提出済み」か確認
		List<WebElement> rows = webDriver.findElement(
				By.className("sctionList")).findElements(By.tagName("tr"));

		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(By.tagName("td"));
			if (cols.get(0).getText().equals("2022年10月2日(日)")) {
				assertEquals("提出済み", cols.get(2).getText());
				break;
			}
		}

		// 「提出済み」の場合は2022年10月2日(日)の「詳細」ボタンを押下
		webDriver.findElements(By.className("btn-default")).get(2).click();

		//	ページが更新されるまで待機
		visibilityTimeout(By.className("breadcrumb"), 10);

		// セクション詳細画面に遷移しているかURL確認
		assertTrue(webDriver.getCurrentUrl().startsWith("http://localhost:8080/lms/section/detail"));

		// タイトルの検証
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		// スクリーンショット
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		//	「提出済み週報【デモ】を確認する」ボタンを押下
		webDriver.findElement(By.cssSelector("input[value='提出済み週報【デモ】を確認する']")).click();

		// ページが更新されるまで待機
		visibilityTimeout(By.tagName("h2"), 10);

		// レポート登録画面に遷移しているかURL確認
		assertTrue(webDriver.getCurrentUrl().startsWith("http://localhost:8080/lms/report/regist"));

		// タイトルの検証
		assertEquals("レポート登録 | LMS", webDriver.getTitle());

		// スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		// 報告内容を修正（所感：レポート修正(週報) 正常系のテスト）
		webDriver.findElement(By.id("content_1")).clear();
		webDriver.findElement(By.id("content_1")).sendKeys("レポート修正(週報) 正常系のテスト");

		// ボタンが見える位置にスクロール
		scrollTo("100");

		//	「提出する」ボタンを押下
		webDriver.findElement(By.className("btn-primary")).click();

		//	ページが更新されるまで待機
		visibilityTimeout(By.className("breadcrumb"), 10);

		// セクション詳細画面に遷移しているかURL確認
		assertTrue(webDriver.getCurrentUrl().startsWith("http://localhost:8080/lms/section/detail"));

		// タイトルの検証
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		// スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		// 「ようこそ受講生ＡＡ１さん」リンクを押下
		webDriver.findElement(By.partialLinkText("ようこそ受講生ＡＡ１さん")).click();

		// ページが更新されるまで待機
		visibilityTimeout(By.tagName("h2"), 10);

		// ユーザー詳細画面に遷移しているかURL確認
		assertTrue(webDriver.getCurrentUrl().startsWith("http://localhost:8080/lms/user/detail"));

		// タイトルの検証
		assertEquals("ユーザー詳細", webDriver.getTitle());

		// スクリーンショット
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		//	「2022年10月2日(日)」の「週報【デモ】」があるか確認
		List<WebElement> rows = webDriver.findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(By.tagName("td"));
			if (cols.size() > 1
					&& cols.get(0).getText().equals("2022年10月2日(日)")
					&& cols.get(1).getText().equals("週報【デモ】")) {

				// 「詳細」ボタンを押下
				List<WebElement> inputs = cols.get(4).findElements(By.tagName("input"));
				for (WebElement input : inputs) {
					if (input.getAttribute("value").equals("詳細")) {
						// ボタンが見える位置にスクロール
						scrollTo("500");
						input.click();
						break;
					}
				}
				break;
			}
		}

		// ページが更新されるまで待機
		visibilityTimeout(By.tagName("h2"), 10);

		// レポート詳細画面に遷移しているかURL確認
		assertTrue(webDriver.getCurrentUrl().startsWith("http://localhost:8080/lms/report/detail"));

		// タイトルの検証
		assertEquals("レポート詳細 | LMS", webDriver.getTitle());

		//	修正内容が反映されているか確認（所感：レポート修正(週報) 正常系のテスト）
		List<WebElement> tables = webDriver.findElements(By.tagName("table"));
		List<WebElement> reportRows = tables.get(2).findElements(By.tagName("tr"));
		for (WebElement row : reportRows) {
			// th（項目名）を取得
			List<WebElement> headers = row.findElements(By.tagName("th"));
			// 所感の行を探す
			if (!headers.isEmpty() && headers.get(0).getText().equals("所感")) {
				List<WebElement> cols = row.findElements(By.tagName("td"));
				assertEquals("レポート修正(週報) 正常系のテスト", cols.get(0).getText().trim());
				break;
			}
		}

		// スクリーンショット
		getEvidence(new Object() {
		});

	}

}
