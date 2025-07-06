package com.baro.jwt_auth.user.jwt;

import com.baro.jwt_auth.user.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    public void doFilterInternal(HttpServletRequest request,
                         HttpServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        String authHeader = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);

        if(StringUtils.hasText(authHeader) && authHeader.startsWith(JwtUtil.BEARER_PREFIX)) {
            try {
                // "Bearer " 제거
                String token = jwtUtil.substringToken(authHeader);

                // 토큰 검증
                if(jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.getUserInfoFromToken(token);
                    String username = claims.getSubject();

                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                    //인증 객체 생성해서 SecurityContext에 등록
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                logger.error("[JWT 인증 필터] JWT 처리 중 오류 발생", e);
//                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
