package cs4330.cs.utep.edu.happypaw;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.Proxy;

import cs4330.cs.utep.edu.happypaw.Model.FoodContainer;
import cs4330.cs.utep.edu.happypaw.Model.Schedule;
import cs4330.cs.utep.edu.happypaw.Model.Token;


public class SchedulerClient {

    /** To notify a requested grade, or an error. */
    public interface SchedulerListener<T> {

        /** Called when a grade is found. This method is
         * invoked in the caller's thread.
         *
         * @param result The posting date of the grade..
         */
        void onSuccess(T result);

        /** Called when an error is encountered. This method is invoked
         * in the caller's thread. */
        void onError(String msg);
    }


    private class JsonResponse<E> {
        @SerializedName("status")
        public String status;

        @SerializedName("message")
        public String message;

        @SerializedName("data")
        public E data;
    }

    /** JSON header **/
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /** JSON response with type FoodContainer **/
    private static final Type foodCotainerType = new TypeToken<JsonResponse<FoodContainer>>(){}.getType();

    /** JSON response with type String **/
    private static final Type stringType = new TypeToken<JsonResponse<String>>(){}.getType();

    /** JSON response with type String **/
    private static final Type tokenType = new TypeToken<JsonResponse<Token>>(){}.getType();

    /** JSON response with type FoodContainer **/
    private static final Type scheduleType = new TypeToken<JsonResponse<Schedule>>(){}.getType();

    /** Authorization header name **/
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /** Status success **/
    private static final String STATUS_SUCCESS = "success";

    /** Status success **/
    private static final String STATUS_FAIL = "fail";

    /** Path to the food api **/
    private static final String CONTAINER_API_PATH = "/food/";

    /** Path to the schedule api **/
    private static final String SCHEDULE_API_PATH = "/schedule/";

    /** Path to the schedule api **/
    private static final String LOGIN_API_PATH = "/login";

    /** Rest API url **/
    private static final String URL = "http://10.0.2.2:5000/api";

    /** Google library to parse JSON  **/
    private static final Gson g = new Gson();

    /** Http client to make rest api calls **/
    private static final OkHttpClient client = new OkHttpClient();

    String auth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1NTU5MDkxNjksInN1YiI6MSwiZXhwIjoxNTU4NTAxMTc0fQ._5FJnqwWN5NxKBmodX3v1OONc3Wpq5gMTk5fRXNST1I";

    /** Sets authenticator   **/
    public SchedulerClient(){

    }

    public enum REQUEST_TYPE{
        GET, POST
    }

    public Response genRequest(String url, String json, REQUEST_TYPE type) throws IOException {

        Request.Builder request = new Request.Builder()
                .url(url)
                .addHeader(AUTHORIZATION_HEADER, auth);

        switch(type){
            case POST:
                RequestBody body = RequestBody.create(JSON, json);
                request.post(body);
                break;
            case GET:
                request.get();
                break;
        }

        return client.newCall(request.build()).execute();
    }

    public Response genRequest(String url, REQUEST_TYPE type) throws Exception {
        if (type != REQUEST_TYPE.GET)
            throw new Exception("Please provide appropriate request type");
        return genRequest(url, "", type);
    }

    /** Performs a post request when provided with a url and a json string **/
    private Response doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader(AUTHORIZATION_HEADER, auth)
                .build();

        return client.newCall(request).execute();
    }

    /** Performs a get request when provided with a url **/
    private Response doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader(AUTHORIZATION_HEADER, auth)
                .build();

        return client.newCall(request).execute();
    }

    /**
     * Sends post request to the Scheduler APi to set Schedule
     * @param json - json to be processed in the server
     * @param listener - callback functions
     */
    public void setSchedule(String json, SchedulerListener<String> listener){
        new Thread(() -> {
            try {
                Response response = doPostRequest(URL + SCHEDULE_API_PATH, json);
                if (response.code() == 404)
                    listener.onError("404 Not Found");
                String jsonString = response.body().string();
                JsonResponse result = g.fromJson(jsonString, scheduleType);

                if (result.status.equals(STATUS_SUCCESS))
                    listener.onSuccess(result.message);
                else
                    listener.onError(result.message);

            } catch (IOException e) {
                listener.onError("Could not communicate with the server");
            }
        }).start();
    }

    public void getSchedule(SchedulerListener<Schedule> listener){
        new Thread(() -> {
            try {
                Response response = doGetRequest(URL + SCHEDULE_API_PATH);
                if (response.code() == 404)
                    listener.onError("404 Not Found");
                String jsonString = response.body().string();
                JsonResponse<Schedule> result = g.fromJson(jsonString, scheduleType);

                if (result.status.equals(STATUS_SUCCESS))
                    listener.onSuccess(result.data);
                else
                    listener.onError(result.message);
            } catch (IOException e) {
                listener.onError("Could not communicate with the server");
            }
        }).start();
    }

    public void getProgress(SchedulerListener<FoodContainer> listener){
        new Thread(() -> {
            try {
                Response response = doGetRequest(URL + CONTAINER_API_PATH);
                if (response.code() == 404)
                    listener.onError("404 Not Found");
                String jsonString = response.body().string();
                JsonResponse<FoodContainer> result = g.fromJson(jsonString, foodCotainerType);

                if (result.status.equals(STATUS_SUCCESS))
                    listener.onSuccess(result.data);
                else
                    listener.onError(result.message);
            } catch (IOException e) {
                listener.onError("Could not communicate with the server");
            }
        }).start();
    }

    public void login(String json, SchedulerListener<Token> listener){
        new Thread(() -> {
            try {
                Response response = genRequest(URL + LOGIN_API_PATH, json, REQUEST_TYPE.POST);
                if (response.code() == 404)
                    listener.onError("404 Not Found");
                String jsonString = response.body().string();
                JsonResponse<Token> result = g.fromJson(jsonString, tokenType);

                if (result.status.equals(STATUS_SUCCESS))
                    listener.onSuccess(result.data);
                else
                    listener.onError(result.message);
            } catch (IOException e) {
                listener.onError("Could not communicate with the server");
            }
        }).start();
    }

}
