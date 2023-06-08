package kr.eddi.demo.redis;

public interface RedisService {

    void setKeyAndValue(String token, Long accountId);
    // 토큰과 어카운트를 저장 한다.
    Long getValueByKey(String token);
    void deleteByKey(String token);
}