package manage;

public class AuthenticationResponse {

	private Jwt jwt;

	public AuthenticationResponse(Jwt jwt) {
		super();
		this.jwt = jwt;
	}

	public Jwt getJwt() {
		return jwt;
	}

	public void setJwt(Jwt jwt) {
		this.jwt = jwt;
	}

	@Override
	public String toString() {
		return "AuthenticationResponse [jwt=" + jwt + "]";
	}
	
}
