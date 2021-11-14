package tests;


import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import io.restassured.response.Response;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static utilities.PropertyReader.getProperty;

public class PG {

    /*setting up the port. In this example the target uri is "http://api.weatherapi.com/v1/"
    localhost:8089 will direct the requests to the target url. Using wiremock we won't need to
    do repeated requests and will be able to manipulate responses.
    !!!application.properties under the resources needs to be set up for weatherApiKey and baseUri:
     "http://api.weatherapi.com/v1/"
    * */
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);


    //this is just a restassured method making a get request to the specified zip.
    public String getWeatherByZip(Integer zipcode){
        String baseUri = "http://localhost:8089/";
        String apiKey = getProperty("weatherApiKey");

        Response response = given().baseUri(baseUri).queryParam("key",apiKey)
                .queryParam("q",zipcode).when().get("current.json");
        return response.getBody().prettyPrint();
    }

    /*this method records the requests and save the response json files under resources/mapping
    if you don't have a resources/mapping folder you will get an error.
    * */
    @Test
    public void recordTesting(){
        String weatherBaseUri = getProperty("weatherBaseUri");
        startRecording(weatherBaseUri);
        stubFor(get("current.json").willReturn(aResponse().proxiedFrom(weatherBaseUri)));
        System.out.println("result with working zip: \n"+getWeatherByZip(30309));
        System.out.println("result with negative test zip: \n"+getWeatherByZip(00000));

        //line under is optional
        List<StubMapping> recordedMappings = stopRecording().getStubMappings();
    }

    /*now when we call the same requests we won't need to use the exact end point and can manipulate
    the response files.
    */
    @Test
    public void playbackTesting(){
        String weatherBaseUri = getProperty("weatherBaseUri");
        System.out.println("result with working zip: \n"+getWeatherByZip(30309));
        System.out.println("result with negative test zip: \n"+getWeatherByZip(00000));
    }



}
