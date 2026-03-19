package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;

/**
 * 結合テスト ログイン機能①
 * ケース03
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース03 受講生 ログイン 正常系")
public class Case03 {

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
		webDriver.findElement(By.id("password")).sendKeys("StudentAA01");

		//	ログインボタンを押下
		webDriver.findElement(By.className("btn-primary")).click();

		// ページが更新されるまで待機
		visibilityTimeout(By.className("error"), 10);

		// コース詳細画面に遷移しているかURL確認
		assertTrue(webDriver.getCurrentUrl().startsWith("http://localhost:8080/lms/course/detail"));
	}

}
