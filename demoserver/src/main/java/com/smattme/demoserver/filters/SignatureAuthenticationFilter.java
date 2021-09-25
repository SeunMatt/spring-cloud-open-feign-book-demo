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

import com.smattme.demoserver.config.CachingHttpRequestWrapper;
import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.config.SignatureAuthenticationToken;
import com.smattme.demoserver.exceptions.CustomApplicationException;

import io.vavr.control.Try;


public class SignatureAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(SignatureAuthenticationFilter.class);

    public SignatureAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }



    @Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {


        String uri = request.getRequestURI();
        if(logger.isDebugEnabled())
            logger.debug("PATH IN SignatureAuthenticationFilter : " + uri);

        //only run this filter if the URL starts with the /core/sig
        return !uri.startsWith(Routes.SECURED_SIGNATURE);
    }



    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain chain) throws IOException, ServletException {


        if(shouldNotFilter(httpServletRequest)) {
            logger.debug("skipping SignatureAuthenticationFilter");
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        //excluding paths
        logger.debug("Running SignatureAuthenticationFilter");
        
        //this will allow us read the request body more than once
        var requestWrapper = new CachingHttpRequestWrapper(httpServletRequest);
        
        var clientSignature = httpServletRequest.getHeader("signature");
        var clientId = httpServletRequest.getHeader("clientId");
        var timestamp = httpServletRequest.getHeader("timestamp");
        var requestBody = httpServletRequest.getRequestURI() + requestWrapper.getRequestBodyString() + timestamp;

        //We'll use the same token as principal and credentials
        var signatureToken = new SignatureAuthenticationToken(clientId, clientSignature, requestBody);
 
        var tokenAuthenticationEither = Try.of(() -> authenticationManager.authenticate(signatureToken)).toEither();
        if(tokenAuthenticationEither.isLeft()) {
            Throwable throwable = tokenAuthenticationEither.getLeft();
            if(throwable instanceof CustomApplicationException) {
                CustomApplicationException exception = (CustomApplicationException) throwable;
                httpServletResponse.sendError(exception.getHttpStatus().value(), exception.getMessage());
                return;
            }
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Client signature authentication failure");
            return;
        }

        //set the authenticated token as security context holder authentication for downstream processing
        var authenticationToken = tokenAuthenticationEither.get();
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //otherwise everything went well, let's proceed with the request
        //not that we're using requestWrapper and not httpServletRequest because we've read the body
        //once
        chain.doFilter(requestWrapper, httpServletResponse);
    }


}
