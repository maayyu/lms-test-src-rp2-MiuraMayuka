package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		//	機能ボタンの押下
		webDriver.findElement(By.className("dropdown-toggle")).click();

		//	プルダウンが開くまでのタイムアウト設定
		visibilityTimeout(By.className("dropdown-menu"), 10);

		//	ヘルプリンクを押下
		webDriver.findElement(By.linkText("ヘルプ")).click();

		//	ページロードタイムアウト設定
		pageLoadTimeout(10);

		//	タイトルの検証
		assertEquals("ヘルプ | LMS", webDriver.getTitle());

		//	URL確認
		assertTrue(webDriver.getCurrentUrl().startsWith("http://localhost:8080/lms/help"));

		//	スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		//	元ページのタブ記録
		String windowTab = webDriver.getWindowHandle();

		//	よくある質問リンクを押下
		webDriver.findElement(By.linkText("よくある質問")).click();

		//	タブが2つになるまで待機
		new WebDriverWait(webDriver, Duration.ofSeconds(20))
				.until(ExpectedConditions.numberOfWindowsToBe(2));

		//	よくある質問タブへの切り替え
		for (String windowHandle : webDriver.getWindowHandles()) {
			if (!windowHandle.equals(windowTab)) {
				webDriver.switchTo().window(windowHandle);
				break;
			}
		}

		//	ページが表示されるまでのタイムアウト設定
		visibilityTimeout(By.id("wrap"), 10);

		//	タイトルの検証
		assertEquals("よくある質問 | LMS", webDriver.getTitle());

		//	URL確認
		assertTrue(webDriver.getCurrentUrl().startsWith("http://localhost:8080/lms/faq"));

		//	スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		// 「【研修関係】」リンクを押下
		webDriver.findElement(By.linkText("【研修関係】")).click();

		//	検索結果が表示されるまでのタイムアウト設定
		visibilityTimeout(By.cssSelector(".sortabletable"), 10);

		//	検索結果が表示されているかの確認
		List<WebElement> showWebElements = webDriver.findElements(By.className("mb10"));
		assertTrue(showWebElements.size() > 0, "検索結果が1件以上表示されている");

		//	検索結果の検証
		assertEquals("Q.キャンセル料・途中退校について", showWebElements.get(0).getText());
		assertEquals("Q.研修の申し込みはどのようにすれば良いですか？", showWebElements.get(1).getText());

		//	スクリーンショット
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		// 検索結果の一つ目を押下
		webDriver.findElements(By.className("mb10")).get(0).click();

		//	質問解答の定数化
		final String FIRST_QUESTION_ANSEWER = "A. 受講者の退職や解雇等、やむを得ない事情による途中終了に関してなど、事情をお伺いした上で、協議という形を取らせて頂きます。 弊社営業担当までご相談下さい。";

		// 回答が表示されるまでのタイムアウト設定
		visibilityTimeout(By.className("fs18"), 10);

		//	回答内容の検証
		assertEquals(FIRST_QUESTION_ANSEWER, webDriver.findElement(By.className("fs18")).getText());

		//	スクリーンショット
		getEvidence(new Object() {
		});

	}

}
