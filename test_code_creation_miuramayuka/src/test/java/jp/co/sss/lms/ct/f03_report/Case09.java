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
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
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
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		//	「2022年10月2日(日)」の「週報【デモ】」があるか確認
		List<WebElement> rows = webDriver.findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(By.tagName("td"));
			if (cols.size() > 1
					&& cols.get(0).getText().equals("2022年10月2日(日)")
					&& cols.get(1).getText().equals("週報【デモ】")) {

				// 「修正する」ボタンを押下
				List<WebElement> inputs = cols.get(4).findElements(By.tagName("input"));
				for (WebElement input : inputs) {
					if (input.getAttribute("value").equals("修正する")) {
						scrollTo("100");
						input.click();
						break;
					}
				}
				break;
			}
		}
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
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		// 学習項目を空にする
		webDriver.findElement(By.id("intFieldName_0")).clear();

		// ボタンが見える位置にスクロール
		scrollTo("100");

		// 「提出する」ボタンを押下
		webDriver.findElement(By.className("btn-primary")).click();

		// エラーが表示されるまで待機
		visibilityTimeout(By.className("errorInput"), 10);

		// 学習項目にエラーが表示されているか確認
		assertTrue(webDriver.findElement(By.id("intFieldName_0"))
				.getAttribute("class").contains("errorInput"));

		// スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		// 学習項目を入力
		webDriver.findElement(By.id("intFieldName_0")).clear();
		webDriver.findElement(By.id("intFieldName_0")).sendKeys("テスト");

		// 理解度を未入力にする
		webDriver.findElement(By.id("intFieldValue_0"))
				.findElements(By.tagName("option")).get(0).click();

		// ボタンが見える位置にスクロール
		scrollTo("100");

		// 「提出する」ボタンを押下
		webDriver.findElement(By.className("btn-primary")).click();

		// エラーが表示されるまで待機
		visibilityTimeout(By.className("errorInput"), 10);

		// 理解度にエラーが表示されているか確認
		assertTrue(webDriver.findElement(By.id("intFieldValue_0"))
				.getAttribute("class").contains("errorInput"));

		// スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		// 学習項目を入力
		webDriver.findElement(By.id("intFieldName_0")).clear();
		webDriver.findElement(By.id("intFieldName_0")).sendKeys("テスト");

		// 理解度を選択
		webDriver.findElement(By.id("intFieldValue_0"))
				.findElements(By.tagName("option")).get(1).click();

		// 目標の達成度に数値以外を入力
		webDriver.findElement(By.id("content_0")).clear();
		webDriver.findElement(By.id("content_0")).sendKeys("五");

		// ボタンが見える位置にスクロール
		scrollTo("100");

		// 「提出する」ボタンを押下
		webDriver.findElement(By.className("btn-primary")).click();

		// エラーが表示されるまで待機
		visibilityTimeout(By.className("errorInput"), 10);

		// 目標の達成度にエラーが表示されているか確認
		assertTrue(webDriver.findElement(By.id("content_0"))
				.getAttribute("class").contains("errorInput"));

		// スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		// 目標の達成度に範囲外数値以外を入力
		webDriver.findElement(By.id("content_0")).clear();
		webDriver.findElement(By.id("content_0")).sendKeys("11");

		// ボタンが見える位置にスクロール
		scrollTo("100");

		// 「提出する」ボタンを押下
		webDriver.findElement(By.className("btn-primary")).click();

		// エラーが表示されるまで待機
		visibilityTimeout(By.className("errorInput"), 10);

		// 目標の達成度にエラーが表示されているか確認
		assertTrue(webDriver.findElement(By.id("content_0"))
				.getAttribute("class").contains("errorInput"));

		// スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		// 目標の達成度を空にする
		webDriver.findElement(By.id("content_0")).clear();

		// 所感を空にする
		webDriver.findElement(By.id("content_1")).clear();

		// ボタンが見える位置にスクロール
		scrollTo("100");

		// 「提出する」ボタンを押下
		webDriver.findElement(By.className("btn-primary")).click();

		// エラーが表示されるまで待機
		visibilityTimeout(By.className("errorInput"), 10);

		// 目標の達成度にエラーが表示されているか確認
		assertTrue(webDriver.findElement(By.id("content_0"))
				.getAttribute("class").contains("errorInput"));

		// 所感にエラーが表示されているか確認
		assertTrue(webDriver.findElement(By.id("content_1"))
				.getAttribute("class").contains("errorInput"));

		// スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		// 目標の達成度を入力
		webDriver.findElement(By.id("content_0")).clear();
		webDriver.findElement(By.id("content_0")).sendKeys("5");

		// 2001文字の文字列を生成
		String overText = "あ".repeat(2001);

		// 所感に2001文字を入力
		webDriver.findElement(By.id("content_1")).clear();
		webDriver.findElement(By.id("content_1")).sendKeys(overText);

		// 一週間の振り返りに2001文字を入力
		webDriver.findElement(By.id("content_2")).clear();
		webDriver.findElement(By.id("content_2")).sendKeys(overText);

		// ボタンが見える位置にスクロール
		scrollTo("100");

		// 「提出する」ボタンを押下
		webDriver.findElement(By.className("btn-primary")).click();

		// エラーが表示されるまで待機
		visibilityTimeout(By.className("errorInput"), 10);

		// 所感にエラーが表示されているか確認
		assertTrue(webDriver.findElement(By.id("content_1"))
				.getAttribute("class").contains("errorInput"));

		// 一週間の振り返りにエラーが表示されているか確認
		assertTrue(webDriver.findElement(By.id("content_2"))
				.getAttribute("class").contains("errorInput"));

		// スクリーンショット
		getEvidence(new Object() {
		});
	}

}
