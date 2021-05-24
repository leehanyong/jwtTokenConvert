package manage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private final String ACCESS_SECRET_KEY;
	private final String REFRESH_SECRET_KEY;
	private final long ACCESS_EXPIRE_MINUTES;
	private final long REFRESH_EXPIRE_MINUTES;

	/**
	 * 토큰 초기값 세팅
	 * 
	 * @param aCCESS_SECRET_KEY
	 * @param aCCESS_EXPIRE_MINUTES
	 */
	public JwtUtil(@Value("${jwt.access.token.secure.key}") String aCCESS_SECRET_KEY,
			@Value("${jwt.refresh.token.secure.key}") String REFRESH_SECRET_KEY,
			@Value("${jwt.access.token.expire.time}") long aCCESS_EXPIRE_MINUTES,
			@Value("${jwt.refresh.token.expire.time}") long rEFRESH_EXPIRE_MINUTES) {
		super();
		
		this.ACCESS_SECRET_KEY = aCCESS_SECRET_KEY;
		this.REFRESH_SECRET_KEY = REFRESH_SECRET_KEY;
		this.ACCESS_EXPIRE_MINUTES = aCCESS_EXPIRE_MINUTES;
		this.REFRESH_EXPIRE_MINUTES = rEFRESH_EXPIRE_MINUTES;
		System.out.println("ACCESS_SECRET_KEY : " + ACCESS_SECRET_KEY);
		System.out.println("REFRESH_SECRET_KEY : " + REFRESH_SECRET_KEY);
		System.out.println("ACCESS_EXPIRE_MINUTES : " + ACCESS_EXPIRE_MINUTES);
		System.out.println("REFRESH_EXPIRE_MINUTES : " + REFRESH_EXPIRE_MINUTES);
	}

	// 토큰타입
	public enum TOKEN_TYPE {
		ACCESS_TOKEN, REFRESH_TOKEN
	}

	// 토큰 타입 데이터
	private class TokenTypeData {

		private final String key;
		private final long time;

		public TokenTypeData(String key, long time) {
			super();
			this.key = key;
			this.time = time;
		}

		public String getKey() {
			return key;
		}

		public long getTime() {
			return time;
		}
	}

	/**
	 * 
	 * @param tokenType
	 * @return
	 */
	private TokenTypeData makeTokenTypeData(TOKEN_TYPE tokenType) {

		String key = tokenType == TOKEN_TYPE.ACCESS_TOKEN ? this.ACCESS_SECRET_KEY : this.REFRESH_SECRET_KEY;
		long time = tokenType == TOKEN_TYPE.ACCESS_TOKEN ? this.ACCESS_EXPIRE_MINUTES : this.REFRESH_EXPIRE_MINUTES;
		return new TokenTypeData(key, time);
	}

	/**
	 * OAUTH
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws CommonException
	 */
	public Jwt makeJwt() throws Exception {
		try {
			final Jwt jwt = this.generateToken();
			return jwt;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 토큰 생성
	 * 
	 * @param userDetails
	 * @return
	 */
	private Jwt generateToken() {

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("발급기관", "하나카드");
		claims.put("발급요청기관", "하나은행");
		claims.put("발급요청기관", "하나은행");
		claims.put("scope", "manage");
		String accessToken = this.createToken(claims, "지원API용 토큰발급", TOKEN_TYPE.ACCESS_TOKEN);
		//String refreshToken = this.createToken(claims, "hanyong", TOKEN_TYPE.REFRESH_TOKEN);
		return new Jwt(accessToken, "");
	}

	/**
	 * 토큰 생성
	 * 
	 * @param claims
	 * @param subject
	 * @return
	 */
	private String createToken(Map<String, Object> claims, String subject, TOKEN_TYPE tokenType) {

		TokenTypeData ttd = this.makeTokenTypeData(tokenType);

		LocalDateTime d = LocalDateTime.now().plusMinutes(ttd.getTime());

		return Jwts.builder().setClaims(claims).setSubject(subject)
				.setExpiration(Date.from(d.atZone(ZoneId.systemDefault()).toInstant()))
				.setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS256, ttd.getKey()).compact();
	}

}
