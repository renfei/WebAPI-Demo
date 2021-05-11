# WebAPI 的一个小 Demo
## 签名验签
为了接口鉴权，双方应该约定一个秘钥【secret】，所以在服务器端维护这一个【appid/secret】的列表。  
客户端和服务器端执行相同的签名，如果一致就验签成功，注意【secret】秘钥不在接口中传递。  
### 签名逻辑
客户端生成一个随机字符串【nonce】、当前时间戳【timestamp】，然后再加上【appid】和【secret】四个参数，先对四个参数进行字典排序，然后再执行【SHA1】，代码参考：
```java
public static String signature(String... arr) {
    Arrays.sort(arr);
    StringBuilder sb = new StringBuilder();
    //将参数拼接成一个字符串进行sha1加密
    for (String param : arr) {
        sb.append(param);
    }
    return EncryptionUtils.encrypt("SHA1", sb.toString());
}
```
服务器端同样执行相同的计算逻辑，因为之前约定好了【secret】，所以根据【appid】即可查询到【secret】，所以不需要传递【secret】秘钥。
