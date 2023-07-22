package com.hadit1993.eshopback.security.jwt

import com.hadit1993.eshopback.utils.error.UnAuthorizedException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.util.*


@Service
class JwtServiceImpl(private val keyPair: KeyPair?) : JwtService {

    private val signingKey = keyPair?.private
    override fun generateToken(username: String, authorities: List<String>, tokenType: JwtTokenType): String {
        val now = Date()
        val claims = Jwts.claims(mapOf("token-type" to tokenType.type)).setSubject(username)
        claims["authorities"] = authorities
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(tokenType.expiration)
            .signWith(signingKey, SignatureAlgorithm.RS256)
            .compact()
    }



    override fun verify(token: String, tokenType: JwtTokenType): Claims {
        val claims = Jwts.parserBuilder().setSigningKey(keyPair?.public).build().parseClaimsJws(token).body
        if (claims["token-type"] != tokenType.type) throw UnAuthorizedException("Invalid token type")
        return claims

    }
}