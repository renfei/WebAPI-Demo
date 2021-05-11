package net.renfei.webapi.interceptor;

import com.alibaba.fastjson.JSON;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.DateUtils;
import net.renfei.sdk.utils.NumberUtils;
import net.renfei.sdk.utils.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 签名验签
 *
 * @author renfei
 */
public class SignatureInterceptor implements HandlerInterceptor {
    /**
     * 秘钥列表，此处直接写死，不查库了
     */
    private final static List<Map<String, String>> SECRET_LIST =
            new CopyOnWriteArrayList<Map<String, String>>() {{
                this.add(new ConcurrentHashMap<String, String>() {{
                    this.put("fagaiwei", "cM5jN5vV3jH0zP2oK5qN9rC4eR8jL5zO");
                }});
                this.add(new ConcurrentHashMap<String, String>() {{
                    this.put("renshiju", "qL2oF6qQ9cX0uS1kB1yY6gY4kY4xH4bT");
                }});
            }};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String appid = request.getParameter("appid");
        String nonce = request.getParameter("nonce");
        APIResult apiResult;
        if (BeanUtils.isEmpty(signature)) {
            apiResult = APIResult.builder()
                    .code(StateCode.Unauthorized)
                    .message("参数[signature]缺失")
                    .build();
            send(apiResult, response);
            return false;
        } else if (BeanUtils.isEmpty(timestamp)) {
            apiResult = APIResult.builder()
                    .code(StateCode.Unauthorized)
                    .message("参数[timestamp]缺失")
                    .build();
            send(apiResult, response);
            return false;
        } else if (BeanUtils.isEmpty(appid)) {
            apiResult = APIResult.builder()
                    .code(StateCode.Unauthorized)
                    .message("参数[appid]缺失")
                    .build();
            send(apiResult, response);
            return false;
        } else if (BeanUtils.isEmpty(nonce)) {
            apiResult = APIResult.builder()
                    .code(StateCode.Unauthorized)
                    .message("参数[nonce]缺失")
                    .build();
            send(apiResult, response);
            return false;
        }
        // 验证时间戳，如果有条件可以存起来防止重放攻击
        int nowTimestamp = DateUtils.getUnixTimestamp();
        int clientTimestamp = NumberUtils.parseInt(timestamp, 1);
        if (clientTimestamp == 1 || clientTimestamp < nowTimestamp) {
            apiResult = APIResult.builder()
                    .code(StateCode.Unauthorized)
                    .message("参数[timestamp]不正确")
                    .build();
            send(apiResult, response);
            return false;
        }
        // 客户端和服务器时间戳相差30秒以上
        if (nowTimestamp - clientTimestamp > 30) {
            apiResult = APIResult.builder()
                    .code(StateCode.Unauthorized)
                    .message("客户端时间[timestamp]于服务器相差过大")
                    .build();
            send(apiResult, response);
            return false;
        }
        String secret = getSecret(appid);
        if (BeanUtils.isEmpty(secret)) {
            apiResult = APIResult.builder()
                    .code(StateCode.Unauthorized)
                    .message("参数[appid]不正确")
                    .build();
            send(apiResult, response);
            return false;
        }
        // 签名
        String serverSignature = StringUtils.signature(timestamp, appid, nonce, secret);
        if (serverSignature.equals(signature)) {
            return true;
        }
        apiResult = APIResult.builder()
                .code(StateCode.Unauthorized)
                .message("[signature]签名验签失败")
                .build();
        send(apiResult, response);
        return false;
    }

    /**
     * 根据AppID获取秘钥
     *
     * @param appid APPID
     * @return 秘钥
     */
    private String getSecret(String appid) {
        List<Map<String, String>> secretList =
                SECRET_LIST.stream().filter(list -> list.get(appid) != null).collect(Collectors.toList());
        if (BeanUtils.isEmpty(secretList)) {
            return null;
        }
        return secretList.get(0).get(appid);
    }

    private void send(APIResult apiResult, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSON.toJSONString(apiResult));
        response.getWriter().flush();
    }
}
