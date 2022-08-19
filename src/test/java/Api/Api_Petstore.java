package Api;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class Api_Petstore {

	public static JsonPath js;
	public String url = "https://petstore.swagger.io/v2";
	public static RestAssured s;
	public int group;
	public String tag;
	public int id;
	@Test(priority=1)
	public void create(){
		s.baseURI = url;
		String res = given().log().all().header("Content-Type","application/json").body("{\r\n"
				+ "    \"id\": 1585,\r\n"
				+ "    \"category\": {\r\n"
				+ "        \"id\": 22,\r\n"
				+ "        \"name\": \"wit\"\r\n"
				+ "    },\r\n"
				+ "    \"name\": \"photon\",\r\n"
				+ "    \"photoUrls\": [\r\n"
				+ "        \"string\"\r\n"
				+ "    ],\r\n"
				+ "    \"tags\": [\r\n"
				+ "        {\r\n"
				+ "            \"id\": 1,\r\n"
				+ "            \"name\": \"neon\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"status\": \"available\"\r\n"
				+ "}").
		when().post("/pet").
		then().log().all().assertThat().statusCode(200).body("id", equalTo(1585)).extract().asString();
		
		js = new JsonPath(res);
		group = js.get("category.id");
		System.out.println(group);
		
		tag = js.get("tags[0].name");
		System.out.println(tag);
		
		id = js.get("id");
		System.out.println(id);
		
	}
	
	@Test(priority=2)
	public void confirm() {
		s.baseURI=url;
		String getres = given().log().all().pathParam("id", id).header("Content-Type","application/json").
		when().get("/pet/{id}").
		then().log().all().assertThat().statusCode(200).extract().asString();
		
		js= new JsonPath(getres);
		String name = js.get("tags[0].name");
		System.out.println(name);

		int n = js.getInt("id");
		System.out.println(n);
		
		assertEquals(n,id);
  
	}

	@DataProvider(name="status")
	public Object[][] getStatus(){
		return new Object[][] {{"sold"},{"available"},{"pending"}};
	}
	@Test(priority = 3,dataProvider = "status")
	public void verifyByStatus(String status  ) {
		s.baseURI=url;
		String stres = given().log().all().queryParam("status",status).header("Content-Type","application/json").
		when().get("/pet/findByStatus").
		then().log().all().assertThat().statusCode(200).extract().asString();
		
		
	}
}
