package com.smattme.demoserver.filters;

import static org.springframework.security.web.authentication.www.BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smattme.demoserver.config.BasicAuthenticationToken;
import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.exceptions.CustomApplicationException;

import io.vavr.control.Try;


public class BasicAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationFilter.class);

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }



    @Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {


        String uri = request.getRequestURI();
        if(logger.isDebugEnabled())
            logger.debug("PATH IN BasicAuthenticationFilter : " + uri);

        //if any of the included URI matches the request uri perform filter
        //otherwise skip
        return !uri.startsWith(Routes.SECURED_BASIC);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = request;
        HttpServletResponse httpServletResponse = response;

        if(shouldNotFilter(httpServletRequest)) {
            logger.debug("skipping BasicAuthenticationFilter");
            chain.doFilter(request, response);
            return;
        }

        //excluding paths
        logger.debug("Running BasicAuthenticationFilter");
        String authorization = httpServletRequest.getHeader("Authorization");

        //validate the authorisation header and get an authenticated token
        var clientAuthenticationTokenEither = Try.of(() -> doBasicAuthentication(authorization)).toEither();
        if(clientAuthenticationTokenEither.isLeft()) {
            Throwable throwable = clientAuthenticationTokenEither.getLeft();
            logger.debug("Error Throwable " + throwable.getMessage());
            if(throwable instanceof CustomApplicationException) {
                CustomApplicationException exception = (CustomApplicationException) throwable;
                httpServletResponse.sendError(exception.getHttpStatus().value(), exception.getMessage());
                return;
            }
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Client authentication failure");
            return;
        }

        //set the basic authentication to security context holder for downstream processing
        BasicAuthenticationToken vendorBasicAuthenticationToken = clientAuthenticationTokenEither.get();
        SecurityContextHolder.getContext().setAuthentication(vendorBasicAuthenticationToken);

        //otherwise everything went well, let's proceed with the request
        chain.doFilter(request, response);
    }
    
	private BasicAuthenticationToken doBasicAuthentication(String authorizationHeader) {

		// validate client credentials
		if (!StringUtils.startsWithIgnoreCase(authorizationHeader, AUTHENTICATION_SCHEME_BASIC)) {
			throw new CustomApplicationException(HttpStatus.UNAUTHORIZED, "Invalid authorization header supplied");
		}

		// remove the basic prefix
		authorizationHeader = authorizationHeader.replace(AUTHENTICATION_SCHEME_BASIC, "").trim();

		byte[] decodedAuthorization = Base64.getDecoder().decode(authorizationHeader);
		String token = new String(decodedAuthorization, StandardCharsets.UTF_8);

		int delim = token.indexOf(":");
		if (delim == -1) {
			throw new CustomApplicationException(HttpStatus.UNAUTHORIZED, "Invalid Authorization header");
		}

		BasicAuthenticationToken basicAuthenticationToken = new BasicAuthenticationToken(token.substring(0, delim),
				token.substring(delim + 1));

		Try.of(() -> authenticationManager.authenticate(basicAuthenticationToken))
				.onFailure(throwable -> logger
						.error("Error while performing BasicAuthentication: " + throwable.getMessage(), throwable))
				.getOrElseThrow(
						throwable -> new CustomApplicationException(HttpStatus.UNAUTHORIZED, throwable.getMessage()));

		return basicAuthenticationToken;
	}


}
