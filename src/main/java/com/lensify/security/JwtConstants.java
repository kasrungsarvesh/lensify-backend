package com.lensify.security;

public final class JwtConstants {

    private JwtConstants() {
    }

    public static final String SECRET_KEY =
            "LensifyOpticalShopManagementSystem2026SecretKeyForJWTAuthentication";

    public static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24; // 24 Hours

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER = "Authorization";
}