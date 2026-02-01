package com.xgz.cli.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 基于OkHttp3封装的工具类
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
public class OkHttpUtil {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final MediaType MULTIPART = MediaType.get("multipart/form-data");

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 默认客户端（15s / 30s / 30s）
    private static final OkHttpClient DEFAULT_CLIENT = createClient(15, 30, 30);

    // ---------------------- 客户端构建 ----------------------

    public static OkHttpClient createClient(long connectTimeoutSec, long readTimeoutSec, long writeTimeoutSec) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC); // 生产环境建议改为 NONE 或 BASIC

        return new OkHttpClient.Builder()
                .connectTimeout(connectTimeoutSec, TimeUnit.SECONDS)
                .readTimeout(readTimeoutSec, TimeUnit.SECONDS)
                .writeTimeout(writeTimeoutSec, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .retryOnConnectionFailure(true)
                .build();
    }

    // ---------------------- 同步方法（带自定义客户端支持） ----------------------

    public static String get(String url, Map<String, String> queryParams, Map<String, String> headers,
                             OkHttpClient client) throws IOException {
        if (client == null) client = DEFAULT_CLIENT;

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (queryParams != null) {
            queryParams.forEach(urlBuilder::addQueryParameter);
        }

        Request.Builder req = new Request.Builder()
                .url(urlBuilder.build())
                .get();

        addHeaders(req, headers);

        return execute(req.build(), client);
    }

    public static String postJson(String url, String json, Map<String, String> headers,
                                  OkHttpClient client) throws IOException {
        if (client == null) client = DEFAULT_CLIENT;
        RequestBody body = RequestBody.create(json, JSON);
        return post(url, body, headers, client);
    }

    public static String postForm(String url, Map<String, String> form, Map<String, String> headers,
                                  OkHttpClient client) throws IOException {
        if (client == null) client = DEFAULT_CLIENT;

        FormBody.Builder builder = new FormBody.Builder();
        if (form != null) form.forEach(builder::add);

        return post(url, builder.build(), headers, client);
    }

    private static String post(String url, RequestBody body, Map<String, String> headers,
                               OkHttpClient client) throws IOException {
        Request.Builder req = new Request.Builder()
                .url(url)
                .post(body);

        addHeaders(req, headers);

        return execute(req.build(), client);
    }

    private static String execute(Request request, OkHttpClient client) throws IOException {
        try (Response resp = client.newCall(request).execute()) {
            if (!resp.isSuccessful()) {
                String errBody = resp.body() != null ? resp.body().string() : "";
                throw new IOException("HTTP " + resp.code() + " " + resp.message() + "\n" + errBody);
            }
            ResponseBody body = resp.body();
            return body != null ? body.string() : "";
        }
    }

    // ---------------------- 文件上传（Multipart） ----------------------

    /**
     * 上传单个文件 + 可选表单字段
     *
     * @param url        接口地址
     * @param file       要上传的文件
     * @param fileField  文件字段名（通常为 "file"）
     * @param fileName   上传后服务器看到的文件名（可与本地文件名不同）
     * @param formParams 其他表单参数（可选）
     * @param headers    请求头（可选）
     * @param client     自定义客户端（可选）
     */
    public static String uploadFile(String url,
                                    File file,
                                    String fileField,
                                    String fileName,
                                    Map<String, String> formParams,
                                    Map<String, String> headers,
                                    OkHttpClient client) throws IOException {

        if (client == null) client = DEFAULT_CLIENT;

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        // 添加普通表单字段
        if (formParams != null) {
            formParams.forEach(builder::addFormDataPart);
        }

        // 添加文件
        RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));
        builder.addFormDataPart(fileField, fileName, fileBody);

        RequestBody requestBody = builder.build();

        Request.Builder req = new Request.Builder()
                .url(url)
                .post(requestBody);

        addHeaders(req, headers);

        return execute(req.build(), client);
    }

    // 简易版：只传文件，字段名默认为 "file"
    public static String uploadFile(String url, File file, OkHttpClient client) throws IOException {
        return uploadFile(url, file, "file", file.getName(), null, null, client);
    }

    // ---------------------- 异步调用（enqueue） ----------------------

    public static void getAsync(String url,
                                Map<String, String> queryParams,
                                Map<String, String> headers,
                                OkHttpClient client,
                                Consumer<String> onSuccess,
                                Consumer<Throwable> onFailure) {

        if (client == null) client = DEFAULT_CLIENT;

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (queryParams != null) queryParams.forEach(urlBuilder::addQueryParameter);

        Request.Builder req = new Request.Builder().url(urlBuilder.build()).get();
        addHeaders(req, headers);

        enqueue(req.build(), client, onSuccess, onFailure);
    }

    public static void postJsonAsync(String url,
                                     String json,
                                     Map<String, String> headers,
                                     OkHttpClient client,
                                     Consumer<String> onSuccess,
                                     Consumer<Throwable> onFailure) {

        if (client == null) client = DEFAULT_CLIENT;

        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder req = new Request.Builder().url(url).post(body);
        addHeaders(req, headers);

        enqueue(req.build(), client, onSuccess, onFailure);
    }

    public static void uploadFileAsync(String url,
                                       File file,
                                       String fileField,
                                       String fileName,
                                       Map<String, String> formParams,
                                       Map<String, String> headers,
                                       OkHttpClient client,
                                       Consumer<String> onSuccess,
                                       Consumer<Throwable> onFailure) {

        if (client == null) client = DEFAULT_CLIENT;

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (formParams != null) formParams.forEach(builder::addFormDataPart);

        RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));
        builder.addFormDataPart(fileField, fileName, fileBody);

        Request.Builder reqBuilder = new Request.Builder()
                .url(url)
                .post(builder.build());
        addHeaders(reqBuilder, headers);

        enqueue(reqBuilder.build(), client, onSuccess, onFailure);
    }

    private static void enqueue(Request request, OkHttpClient client,
                                Consumer<String> onSuccess,
                                Consumer<Throwable> onFailure) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (onFailure != null) onFailure.accept(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (Response resp = response) {
                    if (!resp.isSuccessful()) {
                        String err = resp.body() != null ? resp.body().string() : "";
                        if (onFailure != null) {
                            onFailure.accept(new IOException("HTTP " + resp.code() + "\n" + err));
                        }
                        return;
                    }
                    String body = resp.body() != null ? resp.body().string() : "";
                    if (onSuccess != null) onSuccess.accept(body);
                } catch (Exception e) {
                    if (onFailure != null) onFailure.accept(e);
                }
            }
        });
    }

    // ---------------------- 泛型解析（Gson） ----------------------

    /**
     * 解析成对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws IOException {
        return MAPPER.readValue(json, clazz);
    }

    /**
     * 解析成复杂类型（如 List<User>, Map<String,User>, Page<UserVO> 等）
     * <pre>
     * List<User> users = fromJson(json, new TypeReference<List<User>>(){});
     * Map<String, User> map = fromJson(json, new TypeReference<Map<String, User>>(){});
     * </pre>
     */
    public static <T> T fromJson(String json, TypeReference<T> typeRef) throws IOException {
        return MAPPER.readValue(json, typeRef);
    }

    /**
     * 方便解析 List<T>
     */
    public static <T> java.util.List<T> fromJsonList(String json, Class<T> elementClass) throws IOException {
        TypeReference<java.util.List<T>> typeRef = new TypeReference<java.util.List<T>>() {
            // Jackson 会自动识别 elementClass（需配合构造器或额外逻辑，此处简化）
        };
        // 更推荐直接使用 TypeReference
        return fromJson(json, new TypeReference<List<T>>() {
        });
        // 如果需要明确 elementClass，可用 JavaType：
        // return MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(List.class, elementClass));
    }

    /**
     * 序列化对象 → JSON 字符串（如果你后续需要发送对象而非手写 json 字符串）
     */
    public static String toJson(Object obj) throws IOException {
        return MAPPER.writeValueAsString(obj);
    }

    // 示例：post 对象而非字符串
    public static String postJsonObject(String url, Object bodyObj, Map<String, String> headers,
                                        OkHttpClient client) throws IOException {
        if (client == null) client = DEFAULT_CLIENT;
        String json = toJson(bodyObj);
        RequestBody body = RequestBody.create(json, JSON);
        return post(url, body, headers, client);
    }

    // ---------------------- 辅助方法 ----------------------

    private static void addHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::header);
        }
    }

    // ---------------------- 使用示例 ----------------------

    /*
    public static void main(String[] args) throws Exception {
        // 1. 自定义超时 + 同步 GET
        OkHttpClient customClient = createClient(10, 20, 20);
        String resp = get("https://httpbin.org/get", null, null, customClient);

        // 2. 泛型解析
        String json = "{\"code\":200,\"data\":{\"id\":1,\"name\":\"Tom\"}}";
        ApiResult<User> result = fromJson(json, new TypeToken<ApiResult<User>>(){}.getType());

        // 3. 异步 POST JSON
        postJsonAsync("https://httpbin.org/post", "{\"name\":\"async\"}", null, null,
                body -> System.out.println("Success: " + body),
                ex   -> System.err.println("Fail: " + ex.getMessage()));

        // 4. 上传文件
        File file = new File("photo.jpg");
        String uploadResp = uploadFile("https://example.com/upload", file, null);
    }
    */
}
