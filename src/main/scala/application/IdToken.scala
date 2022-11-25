package application

import com.auth0.jwk.{GuavaCachedJwkProvider, UrlJwkProvider}
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.{JWT, JWTVerifier}
import com.auth0.jwt.interfaces.DecodedJWT

import java.net.URL
import java.security.interfaces.RSAPublicKey

class IdToken {
  def verify(token: String): DecodedJWT = {
    val decodedToken = JWT.decode(token)
    val iss = decodedToken.getIssuer
    if (!jwtTokenIssuer.equals(iss)) {
      throw new IllegalArgumentException("issの検証に失敗しました")
    }

    val tokenUse = decodedToken.getClaim("token_use").asString()
    if (!"id".equals(tokenUse)) {
      throw new IllegalArgumentException("IDトークンの用途がIDではありません")
    }

    val alg = decodedToken.getAlgorithm
    if (!"RS256".equals(alg)) {
      throw new IllegalArgumentException("IDトークンの署名アルゴリズムが対応していないものです。")
    }

    val decodeJWT = tokenVerify(decodedToken)

    if(decodeJWT == null) {
      throw new IllegalArgumentException("ID Tokenの検証に失敗しました")
    }

    decodeJWT
  }

  import com.auth0.jwt.interfaces.DecodedJWT

  private def tokenVerify(jwToken: DecodedJWT) = try {
    val verified = getVerifier(jwToken.getKeyId).verify(jwToken.getToken)
    verified
  } catch {
    case e: Exception =>
      throw new IllegalArgumentException(e)
  }

  private val verifier: JWTVerifier = null
  private val VERIFIER_LIVE_MILISEC = 10 * 60 * 1000 //10分
  private var verifierExpire: Long = -1

  private def getVerifier(kid: String): JWTVerifier = {
    if (verifier != null && System.currentTimeMillis() < verifierExpire) {
      return verifier
    }

    synchronized {
      if (verifier != null && System.currentTimeMillis() < verifierExpire) {
        return verifier
      }
      val http = new UrlJwkProvider(new URL(jwksUrl))
      val provider = new GuavaCachedJwkProvider(http)
      val jwk = provider.get(kid)
      val algorithm = Algorithm.RSA256(jwk.getPublicKey.asInstanceOf[RSAPublicKey], null)
      val newVerifier = JWT.require(algorithm)
        .withIssuer(jwtTokenIssuer)
        .build()

      verifierExpire = System.currentTimeMillis() + VERIFIER_LIVE_MILISEC

      newVerifier
    }
  }

  private val AWS_REGION = "ap-northeast-1"
  private val AWS_COGNITO_USER_POOL_ID = "ap-northeast-1_W6lVjXpLc"

  /**
   * ID トークンの発行者を取得します
   *
   * @return
   */
  private def jwtTokenIssuer
  = String.format("https://cognito-idp.%s.amazonaws.com/%s",
    AWS_REGION, AWS_COGNITO_USER_POOL_ID)

  /**
   * JSON Web トークン (JWT) セットのURLを取得します。
   *
   * @return
   */
  private def jwksUrl = jwtTokenIssuer + "/.well-known/jwks.json"

}
