package com.smattme.demoserver.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.config.TokenAuthenticationToken;
import com.smattme.demoserver.exceptions.CustomApplicationException;

import io.vavr.control.Try;


public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }



    @Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {


        String uri = request.getRequestURI();
        if(logger.isDebugEnabled())
            logger.debug("PATH IN TokenAuthenticationFilter : " + uri);

        //if any of the included URI matches the request uri perform filter
        //otherwise skip
        return !uri.startsWith(Routes.SECURED_TOKEN);
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = request;
        HttpServletResponse httpServletResponse = response;

        if(shouldNotFilter(httpServletRequest)) {
            logger.debug("skipping TokenAuthenticationFilter");
            chain.doFilter(request, response);
            return;
        }

        //excluding paths
        logger.debug("Running TokenAuthenticationFilter");
        var authorizationToken = httpServletRequest.getHeader("Auth-Token");

        //We'll use the same token as principal and credentials
        TokenAuthenticationToken token = new TokenAuthenticationToken(authorizationToken, authorizationToken);
 
        var tokenAuthenticationEither = Try.of(() -> authenticationManager.authenticate(token)).toEither();
        if(tokenAuthenticationEither.isLeft()) {
            Throwable throwable = tokenAuthenticationEither.getLeft();
            if(throwable instanceof CustomApplicationException) {
                CustomApplicationException exception = (CustomApplicationException) throwable;
                httpServletResponse.sendError(exception.getHttpStatus().value(), exception.getMessage());
                return;
            }
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Client authentication failure");
            return;
        }

        //set the authenticated token as security context holder authentication for downstream processing
        var authenticationToken = tokenAuthenticationEither.get();
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //otherwise everything went well, let's proceed with the request
        chain.doFilter(request, response);
    }


}
